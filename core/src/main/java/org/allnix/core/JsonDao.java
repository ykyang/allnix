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

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public interface JsonDao {
  /**
   * Creates a new JSON entry
   * 
   * @param tableName Name of the table to insert into
   * @param id ID of the JSON entry.  The maximum length of the ID is 36 (UUID length)
   * @param json The JSON as string
   * @return true on success
   */
  public boolean create(String tableName, String id, String json);
}
