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
package org.allnix.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestTestNG {

  static private final Logger logger = LoggerFactory.getLogger(TestTestNG.class);

  @BeforeTest(alwaysRun = true)
  void beforeTest() {

  }

  @AfterClass(alwaysRun = true)
  void afterClass() {
    // > Do not delete me
    // > for doc reason
  }

  @Test(groups = {"short", "medium", "long"})
  public void testEquals() {
    int i = 5;
    Assert.assertEquals(i, 5);

    // > Double with tolerance
    double v = 5.6;
    Assert.assertEquals(v, 5.5, 0.1);
  }

  @Test(groups = {"short"})
  public void testThrows() {

    Assert.assertThrows(RuntimeException.class, () -> {
      throw new RuntimeException();
    });
    Throwable result = Assert.expectThrows(RuntimeException.class, () -> {
      throw new RuntimeException();
    });
    Assert.assertTrue(result instanceof RuntimeException);

  }

  @Test(groups = {"long"}, expectedExceptions = {RuntimeException.class})
  void testAnnotationException() {
    throw new RuntimeException();
  }

  // >>>
  // > Data Provider
  // >>>
  @DataProvider(name = "names")
  public Object[][] nameList() {
    return new Object[][]{
      new Object[]{new String("alpha")},
      new Object[]{new String("beta")}
    };
  }

  @Test(dataProvider = "names")
  public void testNameList(String value) {
    logger.debug("Name: {}", value);
    Assert.assertNotNull(value);
  }

  // >>> 
  // > Name of Data Provider using method name
  // >>>
  @DataProvider
  public Object[][] nameProvider() {
    return new Object[][]{
      new Object[]{"gamma"},
      new Object[]{"delta"}
    };
  }

  @Test(groups = {"short"}, dataProvider = "nameProvider")
  public void testNameProvider(String value) {
    logger.debug("Name: {}", value);
    Assert.assertNotNull(value);
  }
}
