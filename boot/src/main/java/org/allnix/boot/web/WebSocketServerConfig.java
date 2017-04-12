/*
 * Copyright 2017 Yi-Kun Yang.
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketServerConfig //extends WebMvcConfigurerAdapter 
  implements WebSocketConfigurer {
  static private final Logger logger = LoggerFactory.getLogger(WebSocketServerConfig.class);
//  @Autowired
//  private NotificationWebSocketHandler handler;
//
  @Bean
  public NotificationWebSocketHandler handler() {
    NotificationWebSocketHandler bean = new NotificationWebSocketHandler();
    return bean;
  }
    
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    logger.info("Notification");
//    registry.addHandler(handler(), "/notification").withSockJS();
//    registry.addHandler(handler(), "/notification").setAllowedOrigins("*");
    registry.addHandler(handler(), "/notification");
  }
  
//  @Override
//  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) { 
//    configurer.enable(); 
//  }
}
