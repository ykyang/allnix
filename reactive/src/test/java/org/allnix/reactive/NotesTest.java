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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    /**
     * Run a main thread
     */
    @Test
    @Tag("unit")
    public void test1() {
        Flux.just("red", "white", "blue").log().map(String::toUpperCase)
            .subscribe(System.out::println);
    }

    /**
     * Run a worker thread
     */
    @Test
    @Tag("unit")
    public void test2() {
        Flux<String> flux = Flux.just("red", "white", "blue").log()
                                .map(String::toUpperCase)
                                .subscribeOn(Schedulers.parallel())
                                .doOnNext(System.out::println);

        //        flux.subscribe();
        flux.blockLast();
    }

    /**
     * Process on separate thread
     */
    @Test
    @Tag("unit")
    public void test3() {
        Flux.just("red", "white", "blue").log() //
            .flatMap(
                value -> Mono.just(value.toUpperCase())
                             .subscribeOn(Schedulers.parallel()), //
                2)
            .subscribe(value -> {
                logger.info("Consumed: {}", value);
            });
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
        flux.subscribe((x)->{
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
}
