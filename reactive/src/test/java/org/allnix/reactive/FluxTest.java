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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class FluxTest {

    @Test
    @Tag("unit")
    public void testFlux() {
        { //> empty flux
            Flux<String> flux = Flux.empty();
            StepVerifier.create(flux).expectComplete().verify();
        }
        {//> hard code
            Flux<String> flux = Flux.just("foo", "bar");
            StepVerifier.create(flux).expectNext("foo").expectNext("bar")
                .expectComplete().verify();
        }
        {//> iteratable
            Flux<String> flux = Flux.fromIterable(Arrays.asList("foo", "bar"));
            StepVerifier.create(flux).expectNext("foo").expectNext("bar")
                .expectComplete().verify();
        }
        {
            Flux<String> flux = Flux.error(new IllegalStateException());
            StepVerifier.create(flux).expectError(IllegalStateException.class)
                .verify();
        }
        {
            Flux<Long> flux = Flux.interval(Duration.ofMillis(100)).take(10);
            StepVerifier.create(flux).expectNextCount(10).verifyComplete();
        }

        {        //> additional error
            Flux<String> flux = Flux.just("foo", "bar")
                .concatWith(Mono.error(new RuntimeException()));
            StepVerifier.create(flux).expectNext("foo").expectNext("bar")
                .expectError(RuntimeException.class).verify();
        }

        {
            Flux<User> flux = Flux.just(//
                new User("swhite", null, null),
                new User("jpinkman", null, null));

            StepVerifier.create(flux)
                .assertNext(
                    x -> assertThat(x.getUsername()).isEqualTo("swhite"))
                .assertNext(
                    x -> assertThat(x.getUsername()).isEqualTo("jpinkman"))
                .expectComplete().verify();
        }

        //        void expect3600Elements(Supplier<Flux<Long>> supplier) {
        //            StepVerifier.withVirtualTime(supplier)
        //            .thenAwait(Duration.ofSeconds(3600))
        //            .expectNextCount(3600)
        //            .expectComplete()
        //            .verify(Duration.ofSeconds(3600));
        //        }

        //        Mono<User> capitalizeOne(Mono<User> mono) {
        //            return mono.map((t)->{ return 
        //            new User(
        //                t.getUsername().toUpperCase(),
        //                t.getFirstname().toUpperCase(),
        //                t.getLastname().toUpperCase()
        //            );
        //            });
        //        }

        //        Flux<User> capitalizeMany(Flux<User> flux) {
        //            return flux.map(t->new User(
        //                 t.getUsername().toUpperCase(),
        //                t.getFirstname().toUpperCase(),
        //                t.getLastname().toUpperCase()
        //                ));
        //        }

        //        Flux<User> asyncCapitalizeMany(Flux<User> flux) {
        //            return flux.flatMap(
        //                t->Mono.just(
        //                    new User(
        //                        t.getUsername().toUpperCase(),
        //                        t.getFirstname().toUpperCase(),
        //                        t.getLastname().toUpperCase()
        //                        )
        //                    )
        //                );
        //            
        //        }

        //        StepVerifier requestAllExpectFour(Flux<User> flux) {
        //            return StepVerifier.create(flux).thenRequest(Long.MAX_VALUE)
        //            .expectNextCount(4)
        //            .expectComplete();
        //        }

        //        StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
        //            return StepVerifier.create(flux)
        //                .expectNext(User.SKYLER).expectNext(User.JESSE)
        //                .thenCancel()
        //                ;
        //        }
    }
    
    @Test
    @Tag("unit")
    public void learnFlux() {
        List<Integer> list = new ArrayList<>();
        
        Flux.range(1, 5)
        .log()
        .subscribe(list::add);
        
        assertThat(list).hasSize(5);
    }
}
