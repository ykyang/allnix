package org.allnix.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
//@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@RestController
@EnableAutoConfiguration
public class Application {
  @RequestMapping("/")
  String home() {
    return "Hello World!";
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
