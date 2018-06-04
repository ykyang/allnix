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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private String strlist;
  private String uuid;
  private String uuid2;
  private Pojo pojo;
  private List<Pojo> pojoList;

  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
    mapper = new ObjectMapper();
    
    uuid = "300a9aa6-4f9d-4286-b7be-f23a20374a7d";
    uuid2 = "cb3b2944-d59d-4337-96c8-a19c1321c19b";
    
    map = new HashMap<>();
    map.put("id", uuid);
    
    string = String.format("{\"id\":\"%s\"}", uuid);
    //< a String that has a list of POJOs >// 
    strlist = String.format("[{\"id\":\"%s\"}, {\"id\":\"%s\"}]", uuid, uuid2);
    
    objectNode = new ObjectNode(JsonNodeFactory.instance);
    objectNode.put("id", uuid);
    
    pojo = new Pojo();
    pojo.setId(uuid);
    
    pojoList = new ArrayList<Pojo>();
    {
        Pojo p = new Pojo();
        p.setId(uuid);
        pojoList.add(p);
        p = new Pojo();
        p.setId(uuid2);
        pojoList.add(p);
    }
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
  public void testConvertValue() throws JsonParseException, JsonMappingException, IOException {
    { 
      // > Map -> POJO
      Pojo value = mapper.convertValue(map, Pojo.class);
      Assert.assertEquals(value.getId(), uuid);
      
      //> use readValue for String -> POJO
      try {
          value = mapper.convertValue(string, Pojo.class);
          Assert.fail();
      } catch(IllegalArgumentException e) {
          
      }
      
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
      
      // > use readValue for String -> ObjectNode
      try {
          value = mapper.convertValue(string, ObjectNode.class);
          Assert.fail();
      } catch(IllegalArgumentException e) {

      }
    }
    
    {
      // > POJO -> Map
      Map<String,Object> value = mapper.convertValue(pojo, Map.class);
      Assert.assertEquals((String)value.get("id"), uuid);
      
      // > ObjectNode -> Map
      value = mapper.convertValue(objectNode, Map.class);
      Assert.assertEquals((String)value.get("id"), uuid);
      
      // > String -> Map, use readValue
      TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>(){};
      value = mapper.readValue(string, typeRef);
      Assert.assertEquals((String)value.get("id"), uuid);
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
            JavaType type = mapper.getTypeFactory()//
                .constructMapType(Map.class, String.class, Object.class);
            Map<String, Object> value = //
                mapper.readValue(string, type);

            Assert.assertEquals((String) value.get("id"), uuid);
        }

        {
            // > String -> ObjectNode
            ObjectNode value = mapper.readValue(string, ObjectNode.class);
            Assert.assertEquals(value.get("id").asText(), uuid);
        }
        
        {  
            //> String -> List<POJO>
//           TypeReference<List<Pojo>> typeRef = new TypeReference<List<Pojo>>() {};
            JavaType type = mapper.getTypeFactory()//
                .constructCollectionType(List.class, Pojo.class);
//           List<Pojo> pojoList = mapper.readValue(strlist, typeRef);
            List<Pojo> pojoList = mapper.readValue(strlist, type);
           Assert.assertEquals(pojoList.size(), 2);
           Pojo value = pojoList.get(0);
           Assert.assertEquals(value.getId(), uuid);
           value = pojoList.get(1);
           Assert.assertEquals(value.getId(), uuid2);
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
    
    //> List<Pojo> -> String
    value = mapper.writeValueAsString(pojoList);
    //> How am I supposed to test this?
    //> Get it from the log
//    logger.info("List<Pojo>: {}", value);
    Assert.assertEquals(value, "[{\"id\":\"300a9aa6-4f9d-4286-b7be-f23a20374a7d\"},{\"id\":\"cb3b2944-d59d-4337-96c8-a19c1321c19b\"}]");
  }
}
