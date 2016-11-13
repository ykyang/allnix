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

import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestFunctionalOperation {

  @Test
  public void test() {
    List<Integer> list = Arrays.asList(7, 6, 5, 4, 3, 2, 1);
    list = list.stream()
      .filter((x) -> {
        out.println("Filtering: " + x);
        return x % 2 == 0;
      })
      .map((x) -> {
        out.println("Mapping: " + x);
        return x*x;
      })
      .limit(2)
      .collect(Collectors.toList());
    
    list.forEach((i) -> {
      out.println(i);
    });
  }
}
