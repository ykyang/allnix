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
package org.allnix.restjson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@Configuration
@EnableAutoConfiguration
@Import(value = {
  org.allnix.restjson.web.JsonController.class, 
  org.allnix.restjson.web.Config.class})
//@ComponentScan // Use this to auto-scan classes
public class Application {
  static private final Logger logger = LoggerFactory.getLogger(Application.class);
  public static void main(String[] args) {
    logger.info("START");
    SpringApplication.run(Application.class, args);
  }
}
