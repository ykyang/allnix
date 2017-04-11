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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class NotificationWebSocketHandler extends TextWebSocketHandler {

  static private final Logger logger = LoggerFactory.getLogger(
    NotificationWebSocketHandler.class);
  
  private List<WebSocketSession> sessions = new ArrayList<>();
  
  @Override
  public void handleTransportError(WebSocketSession session, Throwable throwable)
    throws Exception {
    logger.error(ExceptionUtils.getStackTrace(throwable));
  }
  
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
    throws Exception {
    logger.info("afterConnectionClosed");
    logger.info("Session: {}", session.toString());
    logger.info("Status: {}", status.toString());
  }
  
  @Override
  public void afterConnectionEstablished(WebSocketSession session)
    throws Exception {
    super.afterConnectionEstablished(session);
    // TODO: Save the session
    sessions.add(session);
    
    logger.info("Established: {}", session.toString());
  }
  
  @Override
  protected void handleTextMessage(WebSocketSession session,
    TextMessage jsonTextMessage) throws Exception {
    logger.info("TextMessage: {}", jsonTextMessage.getPayload());
  }
}
