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
package org.allnix.sql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.allnix.test.TestJsonDao;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestSqliteJsonDao extends TestJsonDao {

  static private final Logger logger = LoggerFactory.getLogger(TestSqliteJsonDao.class);
  
  private SQLiteJsonDao dao;
  static private final String JOB_INPUT = "JobInput";

  private String databaseFileName;
  private AnnotationConfigApplicationContext ctx;

  @BeforeClass(alwaysRun = false)
  void beforeClass() throws Exception {
    logger.debug("beforeTest()");
    
    databaseFileName = "job.sqlite.db";
    
    // > Set database name
    logger.info("SQLite database name property key: {}", SqliteJdbcConfig.DATABASE);
    logger.info("SQLite database name: {}", databaseFileName);
    
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map myMap = new HashMap();
    myMap.put(SqliteJdbcConfig.DATABASE, databaseFileName);
    propertySources.addFirst(new MapPropertySource("MY_MAP", myMap));
    
    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(environment);
    ctx.register(
      SqliteJdbcConfig.class
    );
    ctx.refresh();
    ctx.registerShutdownHook();
    
    dao = ctx.getBean(SQLiteJsonDao.class);
    dao.createTable(JOB_INPUT);
  }

  @AfterClass
  void afterClass() throws IOException {
    FileUtils.deleteQuietly(new File(databaseFileName));
  }
  
  @Test(threadPoolSize = 4, invocationCount = 4)
  public void testCRUD() {
    super.testCRUD(dao, JOB_INPUT);
  }
  
  @Test(threadPoolSize = 4, invocationCount = 4)
  public void testMultipleCRUD() throws InterruptedException, IOException {
    super.testMultipleCRUD(dao, JOB_INPUT, 1000);
  }
  
  @Test
  public void testReadUpdate() throws InterruptedException {
    super.testReadUpdate(dao, JOB_INPUT, 50);
  }
}
