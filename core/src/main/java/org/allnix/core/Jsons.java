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

import java.util.Map;

/**
 * Retrieve db.key[0].key[1]...key[n-1]
 * 
 * The type of key[n-1] is T and all others are Map<String,Object>
 * 
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class Jsons {

  static public <T> T get(Map<String, Object> db, String... keys) {
    try {
      Map<String, Object> obj = db;
      int length = keys.length;
      for (int i = 0; i < length - 1; i++) {
        obj = (Map<String, Object>) obj.get(keys[i]);
      }

      return (T) obj.get(keys[length - 1]); 
    } catch (NullPointerException | ClassCastException e) {
      return null;
    }
  }
}
