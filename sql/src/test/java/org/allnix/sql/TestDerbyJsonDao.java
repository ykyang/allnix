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
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestDerbyJsonDao extends TestJsonDao {
  static private final Logger logger = LoggerFactory.getLogger(TestDerbyJsonDao.class);
  
  private SqlJsonDao dao;
  static private final String JOB_INPUT = "JobInput";
  private AnnotationConfigApplicationContext ctx;
  
  @BeforeClass
  void beforeClass() throws Exception {
    logger.debug("beforeTest()");
    
    String database = Paths.get("derby-job.db").toAbsolutePath().toString();
    
    // > Set database name
    logger.info("Derby database name property key: {}", DerbyJdbcConfig.DATABASE);
    logger.info("Derby database name: {}", database);
    
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map myMap = new HashMap();
    myMap.put(DerbyJdbcConfig.DATABASE, database);
    propertySources.addFirst(new MapPropertySource("MY_MAP", myMap)); 
    
    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(environment);
    ctx.register(
      DerbyJdbcConfig.class
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
  
  /**
   * Very slow
   * 
   * @throws InterruptedException
   * @throws IOException 
   */
  @Test(enabled = false, threadPoolSize = 4, invocationCount = 4)
  public void testMultipleCRUD() throws InterruptedException, IOException {
    super.testMultipleCRUD(dao, JOB_INPUT, 1000);
  }
  
  /**
   * Very slow
   * 
   * @throws InterruptedException 
   */
  @Test(enabled = false)
  public void testReadUpdate() throws InterruptedException {
    super.testReadUpdate(dao, JOB_INPUT, 50);
  }
}
