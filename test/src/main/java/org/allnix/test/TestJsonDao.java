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

import java.util.UUID;
import org.allnix.core.JsonDao;
import org.testng.Assert;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJsonDao {

  protected String jsonTemplate;

  public TestJsonDao() {
    jsonTemplate = "{\"id\":\"%s\"}";
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
}
