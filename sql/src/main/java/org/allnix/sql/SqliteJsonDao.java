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
package org.allnix.sql;

/**
 * A DAO for storing JSON in SQLite
 * 
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class SqliteJsonDao extends SqlJsonDao {

  @Override
  public boolean create(String tableName, String id, String json) {
    final String insert = "INSERT OR IGNORE INTO %s VALUES (?,?);";
    
    String sql = String.format(insert, tableName);
    int rowAffected = jdbcTemplate().update(sql, id, json);

    return rowAffected == 1;
  }
}
