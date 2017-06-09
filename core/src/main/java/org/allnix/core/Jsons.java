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

import java.util.HashMap;
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
   * Set a value into hierarchy of maps
   * 
   * Maps along the path specified by the keys does not need to exist
   * before this call.  Missing maps will be created with HashMap<String,Object>
   * @param db
   * @param value
   * @param keys 
   */
  static public void set(Map<String,Object> db, Object value, String... keys) {
    if (keys.length == 0) {
      return;
    }
    
    Map<String,Object> obj = db;
    for ( int i = 0; i < keys.length-1; i++) {
      Map<String,Object> o = (Map<String,Object>) obj.get(keys[i]);
      if ( o == null ) {
        o = new HashMap<String,Object>();
        obj.put(keys[i], o);
      }
      obj = o;
    }
    
    obj.put(keys[keys.length-1], value);
    
  }
}
