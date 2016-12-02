/*
 * Copyright 2016 .
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
package org.allnix.sqlite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestSQLiteJsonDao {

  private SQLiteJsonDao dao;
  static private final String JOB_INPUT = "JobInput";

  private String text;
  private ObjectNode objectNode;
  private ObjectMapper mapper;
  private ExecutorService es;
  
  @BeforeTest(alwaysRun = true)
  void beforeTest() throws Exception {
    dao = new SQLiteJsonDao();
    dao.init();

    dao.createTable(JOB_INPUT);
    
    mapper = new ObjectMapper();
    
  }

  @Test
  public void testBasic() {
    String id = "1";
    String json = "{}";
    boolean result;
    String text;

    result = dao.create(JOB_INPUT, id, json);
    Assert.assertTrue(result);

    // > Second create should fail
    result = dao.create(JOB_INPUT, id, json);
    Assert.assertFalse(result);

    text = dao.read(JOB_INPUT, id);
    Assert.assertEquals(text, json);

    json = "{\"method\":\"run\"}";

    result = dao.update(JOB_INPUT, id, json);
    Assert.assertTrue(result);

    text = dao.read(JOB_INPUT, id);
    Assert.assertEquals(text, json);

    result = dao.delete(JOB_INPUT, id);
    Assert.assertTrue(result);

    // > Read returns null
    text = dao.read(JOB_INPUT, id);
    Assert.assertNull(text);
    
    // > Second delete should fail
    result = dao.delete(JOB_INPUT, id);
    Assert.assertFalse(result);
  }
  
  @Test(threadPoolSize = 10, invocationCount = 10)
  public void testMultipleThread() throws IOException {
    // > Create a million entries
    List<String> ids = new ArrayList<>();
    int count = 10;
    for ( int i = 0; i < count; i++) {
      String id = UUID.randomUUID().toString();
      ids.add(id);
      String text = String.format("{\"id\":\"%s\"}", id);
      dao.create(JOB_INPUT, id, text);
    }
    
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      String text = dao.read(JOB_INPUT, id);
      ObjectNode objectNode = mapper.readValue(text, ObjectNode.class);
      Assert.assertEquals(objectNode.get("id").asText(), id);
    }
  }
}
