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
package org.allnix.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.allnix.core.JsonDao;
import org.testng.Assert;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJsonDao {

  protected String jsonTemplate;
  private Random random;
  private ObjectMapper mapper;

  public TestJsonDao() {
    jsonTemplate = "{\"id\":\"%s\"}";
    random = new Random();
    mapper = new ObjectMapper();
  }

  public void testCRUD(JsonDao dao, String tableName) {
    String id = UUID.randomUUID().toString();
    String json = String.format(jsonTemplate, id);
    boolean ans;
    String actual;

    // Create
    ans = dao.create(tableName, id, json);
    Assert.assertTrue(ans);

    // Read an existing record
    actual = dao.read(tableName, id);
    Assert.assertEquals(actual, json);

    // Read an non-existent record
    actual = dao.read(tableName, "non-existent ID");
    Assert.assertNull(actual);

    // > Update < 
    // Update existing
    json = String.format(jsonTemplate, "new ID");
    ans = dao.update(tableName, id, json);
    Assert.assertTrue(ans);

    // Update non-existent
    ans = dao.update(tableName, "non existent ID", json);
    Assert.assertFalse(ans);
    // Read back to make sure it wasn't saved
    actual = dao.read(tableName, "non existent ID");
    Assert.assertNull(actual);

    // > Delete <
    ans = dao.delete(tableName, id);
    Assert.assertTrue(ans);

    // Delete non-existent
    ans = dao.delete(tableName, id);
    Assert.assertFalse(ans);
  }

  public void testMultipleCRUD(JsonDao dao, String tableName, int count) throws InterruptedException, IOException {
    // > Make threads start at different time
    TimeUnit.MILLISECONDS.sleep(random.nextInt(100));

    // > Create a million entries
    List<String> ids = new ArrayList<>();

    // > Create
    for (int i = 0; i < count; i++) {
      String id = UUID.randomUUID().toString();
      ids.add(id);
      String text = String.format("{\"id\":\"%s\"}", id);
      dao.create(tableName, id, text);
    }

    // > Read
    for (int i = 0; i < count; i++) {
      String id = ids.get(i);
      String text = dao.read(tableName, id);
      ObjectNode objectNode = mapper.readValue(text, ObjectNode.class);
      Assert.assertEquals(objectNode.get("id").asText(), id);
    }

    // > Update: Make id = index
    for (int i = 0; i < count; i++) {
      String id = ids.get(i);
      String value = String.format(jsonTemplate, Integer.toString(i));
      boolean ans = dao.update(tableName, id, value);
      Assert.assertTrue(ans);
    }

    // > Check if the updated value = index
    for (int i = 0; i < count; i++) {
      String id = ids.get(i);
      String value = dao.read(tableName, id);
      ObjectNode objectNode = mapper.readValue(value, ObjectNode.class);
      Assert.assertEquals(objectNode.get("id").asText(), Integer.toString(i));
    }

    // > Delete
    for (int i = 0; i < count; i++) {
      String id = ids.get(i);
      boolean ans = dao.delete(tableName, id);
      Assert.assertTrue(ans);
    }
  }
}
