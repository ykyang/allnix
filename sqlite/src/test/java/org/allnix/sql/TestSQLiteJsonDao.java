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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteJDBCLoader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestSQLiteJsonDao {
  static private final Logger logger = LoggerFactory.getLogger(TestSQLiteJsonDao.class);
  
  private SQLiteJsonDao dao;
  private JdbcTemplate jdbcTemplate;
  static private final String JOB_INPUT = "JobInput";

//  private String text;
  private ObjectMapper mapper;
  private String databaseFileName;
  private String template;
  private Random random;
  private AnnotationConfigApplicationContext ctx;

  @BeforeTest(alwaysRun = true)
  void beforeTest() throws Exception {
    ctx = new AnnotationConfigApplicationContext();
    ctx.register(SQLiteJsonDaoConfig.class, TestDatabaseNameConfig.class);
    ctx.refresh();
    ctx.registerShutdownHook();
    
    
//    databaseFileName = "job.db";
//    FileUtils.deleteQuietly(new File(databaseFileName));
    
    template = "{\"id\":\"%s\"}";
    
    random = new Random();
    
//    BasicDataSource basicDataSource = new BasicDataSource();
//    basicDataSource.setUrl("jdbc:sqlite:" + databaseFileName);
//    
//    // > Memory database file
//    // basicDataSource.setUrl("jdbc:sqlite::memory:");
//
//    // > Maximum number of connection
//    // > SQLite cannot have more than 1 connection
//    // > in multi-thread mode
//    basicDataSource.setMaxTotal(1);
//    
//    // > Initialize SQLite database
//    boolean success = SQLiteJDBCLoader.initialize();
//    
//    jdbcTemplate = new JdbcTemplate();
//    jdbcTemplate.setDataSource(basicDataSource);
//    
//    // > Not waiting for data actually writing to the disk
//    // > The performance is not acceptable for my use case
//    // > without this setting.
//    jdbcTemplate.execute("pragma synchronous = off;");
//    
//    dao = new SQLiteJsonDao();
//    dao.setJdbcTemplate(jdbcTemplate);
    dao = ctx.getBean(SQLiteJsonDao.class);
    dao.createTable(JOB_INPUT);

    mapper = new ObjectMapper();
  }

  @AfterTest(alwaysRun = true)
  void afterTest() throws IOException {
//    FileUtils.deleteQuietly(new File(databaseFileName));
  }
    
//  @Test
//  public void testSwitchDataSource() {
//    BasicDataSource basicDataSource = new BasicDataSource();
//    basicDataSource.setUrl("jdbc:sqlite:" + "tmp.db");
//    basicDataSource.setMaxTotal(1);
//    
//    jdbcTemplate.setDataSource(basicDataSource);
//    dao.createTable(JOB_INPUT);
//    
//    testCRUD();
//    
//    FileUtils.deleteQuietly(new File("tmp.db"));
//    
//    basicDataSource = new BasicDataSource();
//    basicDataSource.setUrl("jdbc:sqlite:" + databaseFileName);
//    basicDataSource.setMaxTotal(1);
//    jdbcTemplate.setDataSource(basicDataSource);
//  }
  
  
  @Test
  public void testCRUD() {
    String id = "1234567890987654321";
    String expectedValue = String.format(template, id);
    boolean result;
    String actualValue;

    // > Create
    result = dao.create(JOB_INPUT, id, expectedValue);
    Assert.assertTrue(result);

    // > Second create should fail
    result = dao.create(JOB_INPUT, id, expectedValue);
    Assert.assertFalse(result);

    // > Read
    actualValue = dao.read(JOB_INPUT, id);
    Assert.assertEquals(actualValue, expectedValue);

    expectedValue = String.format(template, "1357924680");

    // > Update
    result = dao.update(JOB_INPUT, id, expectedValue);
    Assert.assertTrue(result);

    // > Check updated value
    actualValue = dao.read(JOB_INPUT, id);
    Assert.assertEquals(actualValue, expectedValue);

    // > Delete
    result = dao.delete(JOB_INPUT, id);
    Assert.assertTrue(result);

    // > Read should return null
    actualValue = dao.read(JOB_INPUT, id);
    Assert.assertNull(actualValue);

    // > Delete should fail
    result = dao.delete(JOB_INPUT, id);
    Assert.assertFalse(result);
  }

  @Test(threadPoolSize = 4, invocationCount = 128)
  public void testMultipleThreadCRUD() throws IOException, InterruptedException {
    // > Make threads start at different time
    TimeUnit.MILLISECONDS.sleep(random.nextInt(100));
    
    // > Create a million entries
    List<String> ids = new ArrayList<>();
    int count = 100;
    
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
    
    // > Update: Make id = index
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      String value = String.format(template, Integer.toString(i));
      boolean ans = dao.update(JOB_INPUT, id, value);
      Assert.assertTrue(ans);
    }
    
    // > Check if the updated value = index
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      String value = dao.read(JOB_INPUT, id);
      ObjectNode objectNode = mapper.readValue(value, ObjectNode.class);
      Assert.assertEquals(objectNode.get("id").asText(), Integer.toString(i));
    }
    
    // > Delete
    for ( int i = 0; i < count; i++) {
      String id = ids.get(i);
      boolean ans = dao.delete(JOB_INPUT, id);
      Assert.assertTrue(ans);
    }
  }

  /**
   * Use for generate SQL commands for testing in SQLite command line.
   * @throws java.io.FileNotFoundException
   */
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
