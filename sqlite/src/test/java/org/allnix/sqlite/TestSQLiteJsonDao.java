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

  @BeforeTest(alwaysRun = true)
  void beforeTest() throws Exception {
    dao = new SQLiteJsonDao();
    dao.init();

    dao.createTable(JOB_INPUT);
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
  
  @Test
  public void testMultipleThread() {
    ObjectMapper mapper = new ObjectMapper();
    // > Create a million entries
  }
}
