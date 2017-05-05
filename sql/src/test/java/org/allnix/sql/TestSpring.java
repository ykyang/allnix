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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Use Spring to load ApplicationContext
 * 
 * Is this better? I don't know yet.
 * 
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@TestExecutionListeners(inheritListeners = false, 
  listeners = {
    DependencyInjectionTestExecutionListener.class
//    DirtiesContextTestExecutionListener.class, 
//    TransactionalTestExecutionListener.class
  })
@ContextConfiguration(classes = {
//  SQLiteJsonDaoConfig.class,
  TestDatabaseNameConfig.class
})
public class TestSpring extends AbstractTestNGSpringContextTests {

  static private final Logger logger = LoggerFactory.getLogger(TestSpring.class);
  
  private ApplicationContext ctx;

  @BeforeClass
  void beforeClass() {
    logger.debug("beforeTest()");
    ctx = applicationContext;
  }

  @Test
  public void testDatabaseName() {
    String name = ctx.getBean("jobDatabaseName", String.class);
    Assert.assertEquals(name, "job.db");
  }
}
