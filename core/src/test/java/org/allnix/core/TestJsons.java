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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;
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
  private HashMap<String, Object> template;
  private HashMap<String, Object> data;

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
//    Map<String, Object> a, b;
    db = new HashMap<>();
//    a = Collections.synchronizedMap(new HashMap<>());
//    b = Collections.synchronizedMap(new HashMap<>());
//
//    db.put("a", a);
//    a.put("b", b);
//    b.put("c", theString);
//    b.put("d", theDouble);

    Jsons.set(db, theString, "a", "b", "c");
    Jsons.set(db, theDouble, "a", "b", "d");

    // > 
    template = new HashMap<>();
    data = new HashMap<>();
    
    Jsons.set(template, "A", "a");
    Jsons.set(template, "B", "b");
    Jsons.set(template, "E", "c", "e");
    Jsons.set(template, "G", "c", "f", "g");
    Jsons.set(template, "H", "d", "h");
    Jsons.set(template, Arrays.asList("j", "k"), "i");
    
    Jsons.set(data, "AA", "a");
    Jsons.set(data, "EE", "c", "e");
    Jsons.set(data, Arrays.asList("l", "m"), "i");
    Jsons.set(data, "extra", "w");
  }

  @Test
  public void testData() {
    Map<String,Object> t = SerializationUtils.clone(template);
    Map<String,Object> d = SerializationUtils.clone(data);
    
    { // > Test template
      String ans = Jsons.get(t, "c", "f", "g");
      Assert.assertEquals(ans, "G");
      
      ans = Jsons.get(t, "d", "h");
      Assert.assertEquals(ans, "H");
    }
    
    { // > Test template
      List<String> ans = Jsons.get(t, "i");
      Assert.assertEquals(ans.get(0), "j");
      Assert.assertEquals(ans.get(1), "k");
    }
    
    { // > Test data
      String ans = Jsons.get(d, "c", "e");
      Assert.assertEquals(ans, "EE");
    }
    { // > Test data
      List<String> ans = Jsons.get(d, "i");
      Assert.assertEquals(ans.get(0), "l");
      Assert.assertEquals(ans.get(1), "m");
    }
  }
  
  @Test
  public void testMerge() {
    Map<String,Object> t = SerializationUtils.clone(template);
    Map<String,Object> d = SerializationUtils.clone(data);
    
    Jsons.merge(d, t);

    // > t should have data from d
    
    { // > Test data
      String ans = Jsons.get(t, "c", "e");
      Assert.assertEquals(ans, "EE");
      
      ans = Jsons.get(t, "c", "f", "g");
      Assert.assertEquals(ans, "G");
      
      ans = Jsons.get(t, "d", "h");
      Assert.assertEquals(ans, "H");
      
      ans = Jsons.get(t, "w");
      Assert.assertEquals(ans, "extra");
    }
    
    { // > Test data
      List<String> ans = Jsons.get(t, "i");
      Assert.assertEquals(ans.get(0), "l");
      Assert.assertEquals(ans.get(1), "m");
    }
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
