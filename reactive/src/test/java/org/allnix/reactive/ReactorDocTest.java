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


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@TestInstance(Lifecycle.PER_CLASS)
public class ReactorDocTest {
    static final private Logger logger = LoggerFactory
        .getLogger(ReactorDocTest.class);

    @Test
    @Tag("unit")
    public void testConnectableFlux() {
        Flux<Integer> source = Flux.range(1, 5)
            .doOnSubscribe(x -> logger.info("Subscribed to source"));

        ConnectableFlux<Integer> co = source.publish();

        co.subscribe(i -> logger.info("int[1]: {}", i), e -> logger.error(e.toString()),
            () -> logger.info("Complete[1]"));
        co.subscribe(i -> logger.info("int[2]: {}", i), e -> logger.error(e.toString()),
            () -> logger.info("Complete[2]"));
        logger.info("DONE: Subscribing");
        
        co.connect();
    }
    
    @Test
    @Tag("unit")
    public void testParallelFlux() {
//        ParallelFlux<Integer> source =
            Flux.range(1, 100)
            .parallel(4)
            .runOn(Schedulers.parallel())
//            .publishOn(Schedulers.parallel())
            .doOnSubscribe(x -> logger.info("Subscribed to source"))
            .subscribe(i -> logger.info("int[1]: {}", i), e -> logger.error(e.toString()),
                () -> logger.info("Complete[1]"));
            
            ;

//        ConnectableFlux<Integer> co = source.publish();

//        co.subscribe(i -> logger.info("int[1]: {}", i), e -> logger.error(e.toString()),
//            () -> logger.info("Complete[1]"));
        
//        co.subscribe(i -> logger.info("int[2]: {}", i), e -> logger.error(e.toString()),
//            () -> logger.info("Complete[2]"));
        logger.info("DONE: Subscribing");
        
//        co.connect();
    }
}
