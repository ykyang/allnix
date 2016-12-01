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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJackson {
  private ObjectMapper mapper;

  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
    mapper = new ObjectMapper();
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
  
  /**
   * Show how to use valueToTree method
   */
  @Test
  public void testValuetoTree() {
    Map<String,Object> value = new HashMap<>();
    String uuid = "300a9aa6-4f9d-4286-b7be-f23a20374a7d";
    value.put("id", uuid);
    
    JsonNode node = mapper.valueToTree(value);
    
    Assert.assertEquals(node.get("id").asText(), uuid);
  }
}
