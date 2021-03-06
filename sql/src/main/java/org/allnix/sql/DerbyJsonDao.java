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
package org.allnix.sql;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class DerbyJsonDao extends SqlJsonDao {
  public void createTable(String tableName) {
    String sql
            = "CREATE TABLE %s (\n"
            + "  Id    CHAR(36)        PRIMARY KEY,\n"
            + "  Json  CLOB            NOT NULL\n"
            + ")";

    sql = String.format(sql, tableName);

    jdbcTemplate().execute(sql);
  }
}
