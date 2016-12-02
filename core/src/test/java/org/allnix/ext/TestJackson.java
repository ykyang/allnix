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
package org.allnix.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJackson {
  
  static private final Logger logger = LoggerFactory.getLogger(TestJackson.class);
  static class Pojo {
    private String id;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

  private ObjectMapper mapper;
  private Map<String,Object> map;
  private ObjectNode objectNode;
  private String string;
  private String uuid;
  private Pojo pojo;

  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
    mapper = new ObjectMapper();
    
    uuid = "300a9aa6-4f9d-4286-b7be-f23a20374a7d";
    
    map = new HashMap<>();
    map.put("id", uuid);
    
    string = String.format("{\"id\":\"%s\"}", uuid);
    
    objectNode = new ObjectNode(JsonNodeFactory.instance);
    objectNode.put("id", uuid);
    
    pojo = new Pojo();
    pojo.setId(uuid);
  }
  
  
  @Test
  public void testObjectNode() {
    ObjectNode node = new ObjectNode(JsonNodeFactory.instance);

    {
      String value = "12345";
      node.put("id", value);
      Assert.assertEquals(node.get("id").asText(), value);
    }
  }
  
  @Test
  public void testConvertValue() {
    { 
      // > Map -> POJO
      Pojo value = mapper.convertValue(map, Pojo.class);
      Assert.assertEquals(value.getId(), uuid);
      
      // > String -> POJO, use readValue

      // > ObjectNode -> POJO
      value = mapper.convertValue(objectNode, Pojo.class);
      Assert.assertEquals(value.getId(), uuid);
    }
    
    {
      // > POJO -> ObjectNode
      ObjectNode value = mapper.convertValue(pojo, ObjectNode.class);
      Assert.assertEquals(value.get("id").asText(), uuid);
      
      // > Map -> ObjectNode
      value = mapper.convertValue(map, ObjectNode.class);
      Assert.assertEquals(value.get("id").asText(), uuid);
      
      // > String -> ObjectNode, use readValue
    }
    
    {
      // > POJO -> Map
      Map<String,Object> value = mapper.convertValue(pojo, Map.class);
      Assert.assertEquals((String)value.get("id"), uuid);
      
      // > ObjectNode -> Map
      value = mapper.convertValue(objectNode, Map.class);
      Assert.assertEquals((String)value.get("id"), uuid);
      
      // > String -> Map, use readValue
    }
  }

  @Test
  public void testReadValue() throws IOException {
    {
      // > String -> POJO
      Pojo value = mapper.readValue(string, Pojo.class);
      Assert.assertEquals(value.getId(), uuid);
    }
    
    {
      // > String -> Map
      Map<String,Object> value = mapper.readValue(string, Map.class);
      Assert.assertEquals((String)value.get("id"), uuid);
    }
    
    {
      // > String -> ObjectNode
      ObjectNode value = mapper.readValue(string, ObjectNode.class);
      Assert.assertEquals(value.get("id").asText(), uuid);
    }
  }
  
  /**
   * Show how to use valueToTree method
   */
  @Test
  public void testValuetoTree() {
    JsonNode node = mapper.valueToTree(map);
    Assert.assertEquals(node.get("id").asText(), uuid);
    
    // > A String is converted into a text node
    node = mapper.valueToTree(string);
    logger.debug("node: {}", node.toString());
    Assert.assertTrue(node.isTextual());
    Assert.assertEquals(node.asText(), string);

    // > Map -> JsonNode
    node = mapper.valueToTree(map);
    Assert.assertEquals(node.get("id").asText(), uuid);
    Assert.assertTrue(node.isObject());
    Assert.assertTrue(node instanceof ObjectNode);

    // > POJO -> JsonNode 
    node = mapper.valueToTree(pojo);
    Assert.assertEquals(node.get("id").asText(), uuid);
    Assert.assertTrue(node.isObject());
    Assert.assertTrue(node instanceof ObjectNode);
  }
  @Test
  public void testWriteValueAsString() throws JsonProcessingException {
    // > Map -> String
    String value = mapper.writeValueAsString(map);
    Assert.assertEquals(value, string);
    // > ObjectNode -> String
    value = mapper.writeValueAsString(objectNode);
    Assert.assertEquals(value, string);
    // > POJO -> String
    value = mapper.writeValueAsString(pojo);
    Assert.assertEquals(value, string);
  }
}
