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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@SpringBootTest(
  classes = {org.allnix.boot.web.Application.class},
  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class TestWebSocket extends AbstractTestNGSpringContextTests {

  static private final Logger logger = LoggerFactory.getLogger(
    TestWebSocket.class);

  private AnnotationConfigEmbeddedWebApplicationContext ctx;
  private Customization customization;
  private int port;

  @BeforeClass
  public void beforeClass() {
    customization = applicationContext.getBean(Customization.class);
    port = customization.getPort();
    logger.info("port = {}", port);
  }

  @Test
  public void testWebSocket() throws URISyntaxException, IOException, InterruptedException, ExecutionException {

    URI base = new URI("ws", null, "localhost", port, "/notification", null,
      null);
    logger.info("URI: {}", base.toString());
    WebSocketClientHandler handler = new WebSocketClientHandler();
    StandardWebSocketClient client = new StandardWebSocketClient();
    
    ListenableFuture<WebSocketSession> sessionFuture = client.doHandshake(
      handler, base.toString());
    WebSocketSession session = sessionFuture.get();
    session.sendMessage(new TextMessage("This is a test".getBytes()));
    
    
//     List<Transport> transports = Arrays.asList(
//			new WebSocketTransport(new StandardWebSocketClient()),
//			new RestTemplateXhrTransport(new RestTemplate()));
//	SockJsClient sockJsClient = new SockJsClient(transports);
//    ListenableFuture<WebSocketSession> sessionFuture = sockJsClient.doHandshake(handler, base.toString());
//    WebSocketSession session = sessionFuture.get();
//    session.sendMessage(new TextMessage("This is a test".getBytes()));
    
    TimeUnit.SECONDS.sleep(3);
    
//    handler.setSession(session);
    
//    WebSocketConnectionManager manager = new WebSocketConnectionManager(
//      client, handler, base.toString());
    
//    manager.start();
//    handler.sendMessage(new TextMessage("Another test".getBytes()));
  }
}
