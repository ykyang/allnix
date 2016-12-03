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
package org.allnix.sql;

import org.allnix.sql.SQLiteJsonDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteJDBCLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestSQLiteJsonDao {
  static private final Logger logger = LoggerFactory.getLogger(TestSQLiteJsonDao.class);
  
  private SQLiteJsonDao dao;
  static private final String JOB_INPUT = "JobInput";

  private String text;
  private ObjectNode objectNode;
  private ObjectMapper mapper;
  private ExecutorService es;

  @BeforeTest(alwaysRun = true)
  void beforeTest() throws Exception {
    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setUrl("jdbc:sqlite:job.db");
//    basicDataSource.setUrl("jdbc:sqlite::memory:");

    // > Maximum number of connection
    // > SQLite cannot have more than 1 connection
    // > in multi-thread mode
    basicDataSource.setMaxTotal(1);
    
    boolean success = SQLiteJDBCLoader.initialize();
    
    JdbcTemplate jdbcTemplate = new JdbcTemplate(basicDataSource);
    
    // > Not waiting for data actually writing to the disk
    jdbcTemplate.execute("pragma synchronous = off;");
    
    
    dao = new SQLiteJsonDao();
    dao.setJdbcTemplate(jdbcTemplate);
//    dao.init();
    
    dao.createTable(JOB_INPUT);

    mapper = new ObjectMapper();
  }

  @Test
  public void testCRUD() {
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

  @Test(threadPoolSize = 4, invocationCount = 16)
  public void testMultipleThreadCRUD() throws IOException {
    // > Create a million entries
    List<String> ids = new ArrayList<>();
    int count = 1000;
    
    // > Create
    for (int i = 0; i < count; i++) {
      String id = UUID.randomUUID().toString();
      ids.add(id);
      String text = String.format("{\"id\":\"%s\"}", id);
      dao.create(JOB_INPUT, id, text);
    }

    // > Read
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      String text = dao.read(JOB_INPUT, id);
      ObjectNode objectNode = mapper.readValue(text, ObjectNode.class);
      Assert.assertEquals(objectNode.get("id").asText(), id);
    }
    
    // > Update
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      boolean ans = dao.update(JOB_INPUT, id, "{}");
      Assert.assertTrue(ans);
    }
    
    // > Delete
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      boolean ans = dao.delete(JOB_INPUT, id);
      Assert.assertTrue(ans);
    }
  }

  @Test(enabled = false)
  public void generateInsert() throws FileNotFoundException {
    String template = "INSERT OR IGNORE INTO %s VALUES ('%s', '{\"%s\":\"%s\"}');";
    int count = 1_000;
    try (PrintWriter out = new PrintWriter("insert.sql")) {
      for (int i = 0; i < count; i++) {
        String uuid = UUID.randomUUID().toString();
        String sql = String.format(template, JOB_INPUT, uuid, "id", uuid);
        out.println(sql);
      }
    } 
  }
}
