/*
 * Copyright 2016 Yi-Kun Yang.
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
package org.allnix.boot.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
// @SpringBootApplication 
// same as @Configuration @EnableAutoConfiguration @ComponentScan

//@RestController
@Configuration
@EnableAutoConfiguration
@Import(value ={
  Config.class,
  Controller.class,
  WebSocketServerConfig.class
})
public class Application {
  static private final Logger logger = LoggerFactory.getLogger(Application.class);
  public static void main(String[] args) throws Exception {
    logger.info("START");
    SpringApplication.run(Application.class, args);
  }
}
