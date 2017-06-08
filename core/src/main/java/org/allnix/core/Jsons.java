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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retrieve db.key[0].key[1]...key[n-1]
 * 
 * The type of key[n-1] is T and all others are Map<String,Object>
 * 
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class Jsons {
  static private final Logger logger = LoggerFactory.getLogger(Jsons.class);
  
  static public <T> T get(Map<String, Object> db, String... keys) {
    int length = keys.length;
    
    if (length == 0) {
      return null;
    }
    
    try {
      Map<String, Object> obj = db;
      
      for (int i = 0; i < length - 1; i++) {
        obj = (Map<String, Object>) obj.get(keys[i]);
      }
      
      // > Note: a ClassCastException won't be thrown here
      // > I think it is a Java thing that the casting is done
      // > outside of this method
      return (T) obj.get(keys[length - 1]); 
    } catch (NullPointerException | ClassCastException e) {
      return null;
    }
  }
  /**
   * Merge data into template then return the merged map
   * <p>
   * The template will be modified and the data will be used by the returning
   * map.  If this is unacceptable then the caller should make a deep copy
   * before make this call.
   * <b>User data:</b>
   * <pre>
   * {
   *   "a":"AA",
   *   "c":{
   *     "e":"EE"
   *   },
   *   "i":["l", "m"],
   *   "w":"This will be in the merged result" 
   * }
   * </pre>
   * <p>
   * <b>Template:</b>
   * <pre>
   * {
   *   "a":"A", 
   *   "b":"B",
   *   "c":{
   *     "e":"E",
   *     "f":{"g":"G"},
   *   },
   *   "d":{"h":"H"},
   *   "i":["j", "k"]
   * }
   * </pre>
   * <p>
   * <b>Merged result:</b>
   * <pre>
   * {
   *   "a":"AA", 
   *   "b":"B",
   *   "c":{
   *     "e":"EE",
   *     "f":{"g":"G"},
   *   },
   *   "d":{"h":"H"},
   *   "i":["l", "m"]
   * }
   * </pre> Notice the list "i" is in template is completely replaced by the one
   * from user input.
   *
   *
   * @param data User input JSON
   * @param template Template JSON, merged with user input after the call
   */
  static public void merge(Map<String, Object> data,
                           Map<String, Object> template) {

    Map<String, Object> result = template;

    for (Map.Entry<String, Object> entry : data.entrySet()) {
      String dataKey = entry.getKey();
      Object dataValue = entry.getValue();

      if (dataValue instanceof Map) {
        Map dataMap = (Map) dataValue;
        Object templateObj = template.get(dataKey);
        if (templateObj instanceof Map) {
          Map templateMap = (Map) templateObj;

          // >Recursive
          merge(dataMap, templateMap);
        } else {
          throw new RuntimeException("Expect Map in template with key: " + dataKey);
        }
      } else {
        result.put(dataKey, dataValue);
      }
    }
  }
}
