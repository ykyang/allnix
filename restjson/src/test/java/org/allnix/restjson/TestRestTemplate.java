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

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@SpringBootTest(classes = org.allnix.restjson.Application.class, 
  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestRestTemplate extends AbstractTestNGSpringContextTests {
  
  static private final Logger logger = LoggerFactory.getLogger(
    TestRestTemplate.class);
  
  private RestTemplate restTemplate;
  
  private String template;
  
  @BeforeClass
  public void beforeClass() {
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    template = "{\"id\":\"%s\"}";
  }

  /**
   * <pre>
   * {
   *   "type":"success",
   *   "value":{
   *     "id":10,
   *     "quote":"Really loving Spring Boot, makes stand alone Spring apps easy."
   *   }
   * }
   * </pre>
   */
  @Test
  public void testSpringQuote() {
    ObjectNode ans = restTemplate.getForObject(
      "http://gturnquist-quoters.cfapps.io/api/random",
      ObjectNode.class);
    
    Assert.assertEquals("success", ans.get("type").asText());
    logger.info("Quote: " + ans.get("value").get("quote").asText());
  }
  
  @Test
  public void testJsonRest() {
    String id = UUID.randomUUID().toString();
    UriComponents uc = UriComponentsBuilder.fromUriString(
      "http://localhost:8080/json/{table}/{id}")
      .build()
      .expand("job", id)
      .encode();
    
//    logger.info("URI: " + uc.toString());
    String request = String.format(template, id);
    
    try { // GET/Read fail
      ObjectNode ans = restTemplate.getForObject(uc.toUri(), ObjectNode.class);
      Assert.fail();
    } catch ( HttpClientErrorException e) {
      Assert.assertEquals(e.getStatusCode().toString(), "404");
      Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    try { // PUT/Update fail
      restTemplate.put(uc.toUri(), request);
      Assert.fail();
    } catch ( HttpClientErrorException e) {
      Assert.assertEquals(e.getStatusCode().toString(), "404");
      Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    try { // DELETE/Delete fail
      restTemplate.delete(uc.toUri());
      Assert.fail();
    } catch (HttpClientErrorException e) {
      Assert.assertEquals(e.getStatusCode().toString(), "404");
      Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    try { // POST/Create success
      boolean ans = restTemplate.postForObject(uc.toUri(), request,
        Boolean.class);
      Assert.assertTrue(ans);
    } catch (HttpClientErrorException e) {
      logger.error(e.getMessage());
      Assert.fail();
    }
    
    try { // POST fail
      boolean ans = restTemplate.postForObject(uc.toUri(), request,
        Boolean.class);
      Assert.fail();
    } catch (HttpClientErrorException e) {
      Assert.assertEquals(e.getStatusCode().toString(), "409");
      Assert.assertEquals(e.getStatusCode(), HttpStatus.CONFLICT);
    }

    try { // GET success
      ObjectNode ans = restTemplate.getForObject(uc.toUri(), ObjectNode.class);
      Assert.assertEquals(ans.get("id").asText(), id);
    } catch ( HttpClientErrorException e) {
      logger.error(e.getMessage());
      Assert.fail();
    }
    
    try { // Put success
      restTemplate.put(uc.toUri(), "{}");
      ObjectNode ans = restTemplate.getForObject(uc.toUri(), ObjectNode.class);
      Assert.assertFalse(ans.has("id")); // no "id"
      restTemplate.put(uc.toUri(), request);
      ans = restTemplate.getForObject(uc.toUri(), ObjectNode.class); // id
      Assert.assertEquals(ans.get("id").asText(), id);
    } catch ( HttpClientErrorException e) {
      logger.error(e.getMessage());
      Assert.fail();
    }
    
    try { // DELETE/Delete success
      restTemplate.delete(uc.toUri());
    } catch (HttpClientErrorException e) {
      logger.error(e.getMessage());
      Assert.fail();
    }
//    ObjectNode node = restTemplate.getForObject(uc.toUri(), ObjectNode.class);
  }
}
