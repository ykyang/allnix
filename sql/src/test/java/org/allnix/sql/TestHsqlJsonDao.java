/*
 * Copyright 2017 Yi-Kun Yang.
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class TestHsqlJsonDao extends TestJsonDao {
 static private final Logger logger = LoggerFactory.getLogger(TestHsqlJsonDao.class);
  
  private SqlJsonDao dao;
  static private final String JOB_INPUT = "JobInput";
  
  private Path databaseFolder;
  private Path databaseFile;
  private String databaseFileName;
  
  private String url;
  private AnnotationConfigApplicationContext ctx;
  
  @BeforeClass
  void beforeClass() throws Exception {
    logger.debug("beforeTest()");
    
    databaseFileName = "job";
    databaseFolder = Paths.get("hsql").toAbsolutePath();
    FileUtils.forceMkdir(databaseFolder.toFile());
    databaseFile = databaseFolder.resolve(databaseFileName);
    url = String.format("jdbc:hsqldb:file:%s", databaseFile.toString());
    
    // > Set database name
    logger.info("HSQL database name property key: {}", HsqlJdbcConfig.DATABASE_URL);
    logger.info("HSQL database URL: {}", url);
    
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map myMap = new HashMap();
    myMap.put(HsqlJdbcConfig.DATABASE_URL, url);
    propertySources.addFirst(new MapPropertySource("MY_MAP", myMap)); 
    
    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(environment);
    ctx.register(
      HsqlJdbcConfig.class
    );
    ctx.refresh();
    ctx.registerShutdownHook();
    
    dao = ctx.getBean(SqlJsonDao.class);
    dao.createTable(JOB_INPUT);
  }
  
  @AfterClass
  void afterClass() {
    ctx.close();
    FileUtils.deleteQuietly(databaseFolder.toFile());
  }
  
  @Test(threadPoolSize = 4, invocationCount = 4)
  public void testCRUD() {
    super.testCRUD(dao, JOB_INPUT);
  }
  
  /**
   * Slow 
   * 
   * @throws InterruptedException
   * @throws IOException 
   */
  @Test(enabled=false, threadPoolSize = 4, invocationCount = 4)
  public void testMultipleCRUD() throws InterruptedException, IOException {
    super.testMultipleCRUD(dao, JOB_INPUT, 1000);
  }
  
  /**
   * Slow
   * 
   * @throws InterruptedException 
   */
  @Test(enabled = false)
  public void testReadUpdate() throws InterruptedException {
    super.testReadUpdate(dao, JOB_INPUT, 50);
  } 
}
