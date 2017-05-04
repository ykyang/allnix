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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TestH2JsonDao extends TestJsonDao {
  static private final Logger logger = LoggerFactory.getLogger(TestH2JsonDao.class);
  
  private SqlJsonDao dao;
  static private final String JOB_INPUT = "JobInput";
  private Path databaseFile;
  private AnnotationConfigApplicationContext ctx;
  
  @BeforeClass
  void beforeClass() throws Exception {
    logger.debug("beforeTest()");
    
    Path database = Paths.get("h2-job").toAbsolutePath();
//    databaseFileName = 
    
    // > Set database name
    logger.info("H2 database name property key: {}", H2JdbcConfig.DATABASE);
    logger.info("H2 database name: {}", database.toString());
    
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map myMap = new HashMap();
    myMap.put(H2JdbcConfig.DATABASE, database.toString());
    propertySources.addFirst(new MapPropertySource("MY_MAP", myMap)); 
    
    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(environment);
    ctx.register(
      H2JdbcConfig.class
    );
    ctx.refresh();
    ctx.registerShutdownHook();
    
    dao = ctx.getBean(SqlJsonDao.class);
    dao.createTable(JOB_INPUT);
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
