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

import java.util.List;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestFunctionalOperation {

  static private final Logger logger = LoggerFactory.getLogger(
    TestFunctionalOperation.class);

  private List<Integer> intList;
  private int intMin, intMax;

  @BeforeTest(alwaysRun = true)
  void beforeTest() {
    intMin = 0;
    intMax = 1000;
    intList = new ArrayList<>();
    for (int i = intMax; i >= intMin; i--) {
      intList.add(i);
    }
  }

  @Test
  public void test() {
    List<Integer> list = intList; //Arrays.asList(7, 6, 5, 4, 3, 2, 1);
    list = list.stream()
      .filter((x) -> {
        out.println("Filtering: " + x);
        return x % 2 == 0;
      })
      .map((x) -> {
        out.println("Mapping: " + x);
        return x * x;
      })
      .limit(2)
      .collect(Collectors.toList());

    list.forEach((i) -> {
      out.println(i);
    });
  }

  @Test
  public void testAllMatch() {
    List<Integer> list = intList;
    boolean ans = list.stream()
      .allMatch((x) -> {
        return x < 500;
      });

    Assert.assertFalse(ans);

    ans = list.stream().allMatch((x) -> {
      return x <= 1000;
    });

    Assert.assertTrue(ans);
  }

  @Test
  public void testFinaAny() {
    List<Integer> list = intList;

    Optional<Integer> ans = list.stream().findAny();

    Assert.assertTrue(intMin <= ans.get());
    Assert.assertTrue(ans.get() <= intMax);

    ans.ifPresent(out::println);
  }

  @Test
  public void testReduce() {
    List<Integer> list = intList;

    int ans = list.stream()
      .reduce(0, (a, b) -> {
        return a + b;
      });
    
    Assert.assertEquals(ans, (intMin+intMax)*(intMax-intMin+1)/2);
    
    ans = list.stream()
      .reduce(Integer.MIN_VALUE, Integer::max);
    
    Assert.assertEquals(ans, intMax);
  }
}
