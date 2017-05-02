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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Random;
import org.allnix.test.TestJsonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TestH2JsonDao extends TestJsonDao {
  static private final Logger logger = LoggerFactory.getLogger(TestH2JsonDao.class);
  
  private H2JsonDao dao;
  static private final String JOB_INPUT = "JobInput";
  private AnnotationConfigApplicationContext ctx;
  
  @BeforeClass
  void beforeClass() throws Exception {
    logger.debug("beforeTest()");
    ctx = new AnnotationConfigApplicationContext();
    ctx.register(
      H2JsonDaoConfig.class
    );
    ctx.refresh();
    ctx.registerShutdownHook();
    
    dao = ctx.getBean(H2JsonDao.class);
    dao.createTable(JOB_INPUT);
  }
  
  @Test(threadPoolSize = 1, invocationCount = 1)
  public void testCRUD() {
    super.testCRUD(dao, JOB_INPUT);
  }
  
  @Test(threadPoolSize = 4, invocationCount = 4)
  public void testMultipleCRUD() throws InterruptedException, IOException {
    super.testMultipleCRUD(dao, JOB_INPUT, 1000);
  }
}
