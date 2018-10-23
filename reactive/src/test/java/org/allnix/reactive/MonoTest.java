/*
 * Copyright 2018 Yi-Kun Yang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
public class MonoTest {

    @Test
    @Tag("unit")
    public void testMono() {
        Mono<String> mono = Mono.empty();
        StepVerifier.create(mono).expectComplete().verify();
        
        mono = Mono.never();
        //> how do i test
        
        mono = Mono.just("foo");
        StepVerifier.create(mono).expectNext("foo").expectComplete().verify();
        
        mono = Mono.error(new IllegalStateException());
        StepVerifier.create(mono).expectError(IllegalStateException.class).verify();
    }
}
