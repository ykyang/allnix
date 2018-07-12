/*
 * Copyright 2018 Yi-Kun Yang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.allnix.reactive;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static org.assertj.core.api.Assertions.*;

/**
 * Examples from
 * <p>
 * <a href=
 * "https://spring.io/blog/2016/06/13/notes-on-reactive-programming-part-ii-writing-some-code">Examples
 * from here</a>
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class NotesTest {
    static final private Logger logger = LoggerFactory.getLogger(
        NotesTest.class);

    List<String> list, list2;

    @BeforeAll
    public void beforeAll() {
        list = Arrays.asList("red", "white", "blue");
        list2 = Arrays.asList("black", "green", "yellow");
    }

    /**
     * Run on main thread so it is synchronous
     * <p>
     * Sleep in subscribe() has the same effect as in map().
     * <pre>
     * 2018-07-12 07:06:22.455 [Test worker] INFO  reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
     * 2018-07-12 07:06:22.459 [Test worker] INFO  reactor.Flux.Iterable.1 - | request(unbounded)
     * 2018-07-12 07:06:22.460 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(red)
     * 2018-07-12 07:06:22.460 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapping: red
     * 2018-07-12 07:06:22.460 [Test worker] INFO  org.allnix.reactive.NotesTest - Received: RED
     * 2018-07-12 07:06:22.660 [Test worker] INFO  org.allnix.reactive.NotesTest - Consumed: RED
     * 2018-07-12 07:06:22.660 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(white)
     * 2018-07-12 07:06:22.660 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapping: white
     * 2018-07-12 07:06:22.660 [Test worker] INFO  org.allnix.reactive.NotesTest - Received: WHITE
     * 2018-07-12 07:06:22.860 [Test worker] INFO  org.allnix.reactive.NotesTest - Consumed: WHITE
     * 2018-07-12 07:06:22.861 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(blue)
     * 2018-07-12 07:06:22.861 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapping: blue
     * 2018-07-12 07:06:22.861 [Test worker] INFO  org.allnix.reactive.NotesTest - Received: BLUE
     * 2018-07-12 07:06:23.061 [Test worker] INFO  org.allnix.reactive.NotesTest - Consumed: BLUE
     * 2018-07-12 07:06:23.061 [Test worker] INFO  reactor.Flux.Iterable.1 - | onComplete()
     * 2018-07-12 07:06:23.062 [Test worker] INFO  org.allnix.reactive.NotesTest - Blocking
     * </pre>
     * 
     */
    @Test
    @Tag("unit")
    public void testMainThreadSlowInSubscribe() {

        //> Blocking
        Flux.fromIterable(list).log().map(value -> {
            logger.info("Mapping: {}", value);
            return value.toUpperCase();
        }).subscribe(value -> {
            logger.info("Received: {}", value);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
            }
            logger.info("Consumed: {}", value);
        });
        logger.info("Blocking");
    }

    /**
     * Run on the main thread so it is synchronous
     * <p>
     * Sleep in map() has the same effect as in subscribe().
     * <pre>
     * 2018-07-12 07:07:13.373 [Test worker] INFO  reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
     * 2018-07-12 07:07:13.377 [Test worker] INFO  reactor.Flux.Iterable.1 - | request(unbounded)
     * 2018-07-12 07:07:13.377 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(red)
     * 2018-07-12 07:07:13.377 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapping: red
     * 2018-07-12 07:07:13.578 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapped: RED
     * 2018-07-12 07:07:13.578 [Test worker] INFO  org.allnix.reactive.NotesTest - Received: RED
     * 2018-07-12 07:07:13.578 [Test worker] INFO  org.allnix.reactive.NotesTest - Consumed: RED
     * 2018-07-12 07:07:13.578 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(white)
     * 2018-07-12 07:07:13.578 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapping: white
     * 2018-07-12 07:07:13.778 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapped: WHITE
     * 2018-07-12 07:07:13.778 [Test worker] INFO  org.allnix.reactive.NotesTest - Received: WHITE
     * 2018-07-12 07:07:13.778 [Test worker] INFO  org.allnix.reactive.NotesTest - Consumed: WHITE
     * 2018-07-12 07:07:13.778 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(blue)
     * 2018-07-12 07:07:13.778 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapping: blue
     * 2018-07-12 07:07:13.978 [Test worker] INFO  org.allnix.reactive.NotesTest - Mapped: BLUE
     * 2018-07-12 07:07:13.978 [Test worker] INFO  org.allnix.reactive.NotesTest - Received: BLUE
     * 2018-07-12 07:07:13.979 [Test worker] INFO  org.allnix.reactive.NotesTest - Consumed: BLUE
     * 2018-07-12 07:07:13.979 [Test worker] INFO  reactor.Flux.Iterable.1 - | onComplete()
     * 2018-07-12 07:07:13.980 [Test worker] INFO  org.allnix.reactive.NotesTest - Blocking
     * </pre>
     */
    @Test
    @Tag("unit")
    public void testMainThreadSlowInMap() {
        //> Blocking
        Flux.fromIterable(list).log().map(value -> {
            logger.info("Mapping: {}", value);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
            }
            String ans = value.toUpperCase();
            logger.info("Mapped: {}", ans);
            return ans;
        }).subscribe(value -> {
            logger.info("Received: {}", value);
            logger.info("Consumed: {}", value);
        });
        logger.info("Blocking");
    }

    @Test
    @Tag("unit")
    public void testMainThreadFlatMap() {

        //> This is a blocking call
        Flux.fromIterable(list2).log() //
            .flatMap(
                (value) -> {
                    logger.info("flatMap: {}", value);
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    return Mono.just(value.toUpperCase());
                }
               )
            .doOnNext(value -> {
                logger.info("Test subscribe: {}", value);
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                }
                logger.info("Test Consumed: {}", value);
            }).subscribe();
    }
    /**
     * Run a worker thread
     */
    @Test
    @Tag("unit")
    public void test2() {
        //> Non-blocking
        Flux<String> flux = Flux.fromIterable(list).log()
                                .map(String::toUpperCase)
                                .subscribeOn(Schedulers.parallel())
                                .doOnNext(System.out::println);

        flux.subscribe();
        //        flux.blockLast();
        logger.info("test2 subscribeOn non-blocking");
    }

    /**
     * Use blockLast() to block an asynchronous call
     */
    @Test
    @Tag("unit")
    public void testBlocking() {

        //> This is a blocking call
        Flux.fromIterable(list2).log() //
            .flatMap(value -> Mono.just(value.toUpperCase()))
            .doOnNext(value -> {
                logger.info("Test 3 subscribe: {}", value);
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                }
                logger.info("Test 3 Consumed: {}", value);
            }).subscribe();


        //> replace blockLast with subscribe()
        //> make this non-blocking
        String last = Flux.fromIterable(list2).log() //
                          .flatMap(
                              value -> Mono.just(value.toUpperCase())
                                           .subscribeOn(Schedulers.parallel()))
                          .doOnNext(value -> {
                              logger.info("Test 3 subscribe: {}", value);
                              try {
                                  TimeUnit.MILLISECONDS.sleep(300);
                              } catch (InterruptedException e) {
                              }
                              logger.info("Test 3 Consumed: {}", value);
                          }).blockLast() //> Use this for blocking
        ;

        assertThat(last).isEqualTo(list2.get(list2.size() - 1).toUpperCase());

        Flux<String> flux = Flux.fromIterable(list2).log() //
                                .flatMap(
                                    value -> Mono.just(value.toUpperCase())
                                                 .subscribeOn(
                                                     Schedulers.parallel()))
                                .doOnNext(value -> {
                                    logger.info("Test 3 subscribe: {}", value);
                                    try {
                                        TimeUnit.MILLISECONDS.sleep(300);
                                    } catch (InterruptedException e) {
                                    }
                                    logger.info("Test 3 Consumed: {}", value);
                                });

        flux.subscribe(); // non-blocking
        logger.info("See, I am non-blocking");

        List<String> ans = flux.collectList().block(); // blocking
        assertThat(ans).hasSize(list2.size());
    }

    /**
     * Read the log
     * <p>
     * and pay attention to how onComplete() called before the last item is
     * consumed.
     * 
     * <pre>
     * 2018-07-11 08:59:58.474 [Test worker] INFO  org.allnix.reactive.NotesTest - Calling block() to start subscription
     * 2018-07-11 08:59:58.484 [Test worker] INFO  reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
     * 2018-07-11 08:59:58.486 [Test worker] INFO  reactor.Flux.Iterable.1 - | request(256)
     * 2018-07-11 08:59:58.487 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(black)
     * 2018-07-11 08:59:58.495 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(green)
     * 2018-07-11 08:59:58.495 [parallel-1] INFO  org.allnix.reactive.NotesTest - Test 3 subscribe: BLACK
     * 2018-07-11 08:59:58.495 [Test worker] INFO  reactor.Flux.Iterable.1 - | onNext(yellow)
     * 2018-07-11 08:59:58.496 [Test worker] INFO  reactor.Flux.Iterable.1 - | onComplete()
     * 2018-07-11 08:59:58.795 [parallel-1] INFO  org.allnix.reactive.NotesTest - Test 3 Consumed: BLACK
     * 2018-07-11 08:59:58.796 [parallel-1] INFO  org.allnix.reactive.NotesTest - Test 3 subscribe: GREEN
     * 2018-07-11 08:59:59.096 [parallel-1] INFO  org.allnix.reactive.NotesTest - Test 3 Consumed: GREEN
     * 2018-07-11 08:59:59.096 [parallel-1] INFO  org.allnix.reactive.NotesTest - Test 3 subscribe: YELLOW
     * 2018-07-11 08:59:59.396 [parallel-1] INFO  org.allnix.reactive.NotesTest - Test 3 Consumed: YELLOW
     * </pre>
     */
    @Test
    @Tag("unit")
    public void testCollectNonblocking() {
        Flux<String> flux = //
            Flux.fromIterable(list2).log().flatMap((value) -> {
                return Mono.just(value.toUpperCase())
                           //> Parallelism happens here
                           .subscribeOn(Schedulers.parallel());
            }).doOnNext(value -> {
                logger.info("Test 3 subscribe: {}", value);
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                }
                logger.info("Test 3 Consumed: {}", value);
            });

        //        flux.subscribe(); // non-blocking
        //        logger.info("See, I am non-blocking");

        //> This does not start subscription
        Mono<List<String>> mono = flux.collectList();
        //        logger.info("After collectList(), notice the subscription started");

        logger.info("Calling block() to start subscription");
        List<String> ans = mono.block(); // blocking
        assertThat(ans).hasSize(list2.size());
    }


    @Test
    @Tag("unit")
    public void test4() {
        Flux.just("red", "white", "blue").log() //
            .flatMap((value) -> {
                return Mono.just(value.toUpperCase())
                           .subscribeOn(Schedulers.parallel());
            } //
              //             ,   2
            ).subscribe(value -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info("Consumed: {}", value);
            });

        logger.info("Flux called, do something else now");
        try {
            Thread.currentThread().join(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Use my own executor and non-blocking subscribe, blocked by executor to
     * complete
     * 
     * @throws InterruptedException
     */
    @Test
    @Tag("unit")
    public void test5() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(4);
        Scheduler sche = Schedulers.fromExecutor(exec);

        Flux<String> flux = Flux.just("red", "white", "blue", "yellow").log()
                                .flatMap((value) -> {
                                    logger.info("flatMap: {}", value);
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    return Mono.just(value.toUpperCase());
                                }, 1).subscribeOn(sche);

        flux.subscribe(System.out::println); //non blocking
        logger.info("Flux called");
        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    @Tag("unit")
    public void test6() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(4);
        Scheduler sche = Schedulers.fromExecutor(exec);

        ParallelFlux<String> flux = //
            Flux.just("red", "white", "blue", "yellow").log()
                .flatMap((value) -> {
                    //> Processed by the main thread
                    logger.info("flatMap: {}", value);
                    return Mono.just(value.toUpperCase());
                }

                ).parallel(4);

        //> Notice how flux is re-assigned
        flux = flux.runOn(sche);

        //> subscribe on main thread so it is blocking
        logger.info("Before subscribe");
        //> Main processing done here by threads
        flux.subscribe((x) -> {
            logger.info("Processing: {}", x);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("Finished: {}", x);
        });
        logger.info("After subscribe");

        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.DAYS);

    }

    /**
     * Process on separate thread
     */
    @Test
    @Tag("unit")
    public void test3() {
        Flux.fromIterable(list).log() //
            .flatMap(
                value -> Mono.just(value.toUpperCase())
                             .subscribeOn(Schedulers.parallel()), //
                2)
            .doOnNext(value -> {
                logger.info("Test 3 subscribe: {}", value);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info("Test 3 Consumed: {}", value);
            }).subscribe() //> Use this for non-blocking
        //            .blockLast() //> Use this for blocking
        ;


        Flux.fromIterable(list2).log() //
            .flatMap(
                value -> Mono.just(value.toUpperCase())
                             .subscribeOn(Schedulers.parallel()), //
                2)
            .doOnNext(value -> {
                logger.info("Test 3 subscribe: {}", value);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info("Test 3 Consumed: {}", value);
            })
            //        .subscribe() //> Use this for non-blocking
            .blockLast() //> Use this for blocking
        ;

    }
}
