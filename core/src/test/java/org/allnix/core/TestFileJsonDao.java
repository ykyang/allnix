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
package org.allnix.core;

import org.allnix.test.TestJsonDao;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestFileJsonDao extends TestJsonDao {
  private FileJsonDao dao;
  
  public TestFileJsonDao() {
  }
//  private JsonDao dao;
  
//  @DataProvider
//  public Object[][] daoList() {
//    return new Object[][] {
//      new Object[] {FileJsonDao.class}
//    };
//  }
  
  @BeforeClass
  public void beforeClass() {
    dao = new FileJsonDao();
    dao.setDatabaseFolder("FileJsonDao");
  }
  
  @Test
  public void testCRUD() {
    super.testCRUD(dao, "JobInput");
  }
}
