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
package org.allnix.oil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

/**
 * Necessary for Spring Boot Test to work
 * 
 * Remember 
 * @EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
 * @ComponentScan: enable @Component scan on the package where the application is located (see the best practices)
 * @Configuration:
 *
 * @author Yi-Kun Yang ykyang@gmail.com
 */
@SpringBootApplication(exclude = { MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class })
public class TestSpringApplication {
    static private final Logger logger = //
        LoggerFactory.getLogger(TestSpringApplication.class);;

    static public void main(String[] args) {
        logger.info("SpringApplication!!!");
        SpringApplication.run(TestSpringApplication.class, args) // .close()
        ;
    }
}
