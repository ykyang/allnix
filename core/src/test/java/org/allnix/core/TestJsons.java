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
package org.allnix.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJsons {

  private String theString;
  private Double theDouble;
  private Map<String, Object> db;

  /**
   * Construct
   *
   * db.a.b.c = "This is a string" db is
   * <pre>
   * {
   *   "a": {
   *     "b":{
   *       "c":"theString",
   *       "d":theDouble
   *
   *     }
   *   }
   * }
   * </pre>
   */
  @BeforeClass
  public void beforeClass() {
    theString = "This is a string";
    theDouble = 13.17;
    Map<String, Object> a, b;
    db = Collections.synchronizedMap(new HashMap<>());
    a = Collections.synchronizedMap(new HashMap<>());
    b = Collections.synchronizedMap(new HashMap<>());

    db.put("a", a);
    a.put("b", b);
    b.put("c", theString);
    b.put("d", theDouble);
  }

  @Test
  public void testGet() {
    {
      // > No keys
      String ans = Jsons.get(db);
      Assert.assertNull(ans);
    }
    
    {
      // > Get a string
      String ans = Jsons.get(db, "a", "b", "c");
      Assert.assertEquals(ans, theString);

      // > no such object
      ans = Jsons.get(db, "d", "e", "f");
      Assert.assertNull(ans);
    }
    {
      // > Get a double
      Double ans = Jsons.get(db, "a", "b", "d");
      Assert.assertEquals(ans, theDouble);
      
      // > type mismatch
      try { 
        ans = Jsons.get(db, "a", "b", "c");
        Assert.fail();
      } catch (ClassCastException e) {
        // > Passed
      }
    }
    {
      // > Convert Double to double
      double ans = Jsons.get(db, "a", "b", "d");
      Assert.assertEquals(ans, theDouble);

      // > type mismatch
      try {
        ans = Jsons.get(db, "a", "b", "c");
        Assert.fail();
      } catch (ClassCastException e) {
        // > Passed
      }
    }
  }

  @Test
  public void testGetMap() {
    Map<String, Object> b = Jsons.get(db, "a", "b");
    Assert.assertNotNull(b);

    String ans = Jsons.get(b, "c");
    Assert.assertEquals(ans, theString);
  }

  /**
   * <pre>
   * {
   *   "abc": {
   *     "def":{
   *       "xyz": 3.14
   *     }
   *   }
   * }
   * </pre>
   */
  @Test
  public void testSet() {
    
    Map<String,Object> json = new HashMap<>();
    
    
    
    Jsons.set(json, 3.14, "abc", "def", "xyz");

    Double ans = Jsons.get(json, "abc", "def", "xyz");
    Assert.assertEquals(ans, 3.14);
  }
}
