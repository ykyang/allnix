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

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class SQLiteJobDao {

  private JdbcTemplate jdbcTemplate;

  public void init() throws Exception {
    boolean success = SQLiteJDBCLoader.initialize();
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl("jdbc:sqlite:job.db");

    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void createTable() {
    String sql;

    sql = "CREATE TABLE IF NOT EXISTS JobInput (\n"
      + "  Id    CHAR(36)        PRIMARY KEY,\n"
      + "  Json  TEXT            NOT NULL\n"
      + ");";

    jdbcTemplate.execute(sql);

    sql = "CREATE TABLE IF NOT EXISTS Job (\n"
      + "Id    CHAR(36)        PRIMARY KEY,\n"
      + "Json  TEXT            NOT NULL\n"
      + ");\n";

    jdbcTemplate.execute(sql);

    sql = "CREATE TABLE IF NOT EXISTS JobOutput (\n"
      + "  Id    CHAR(36)        PRIMARY KEY,\n"
      + "  Json  TEXT            NOT NULL\n"
      + ");";

    jdbcTemplate.execute(sql);
  }

  public boolean createJobInput(String id, String json) {
    int rowAffected = jdbcTemplate.update(
      "INSERT OR IGNORE INTO JOBINPUT VALUES (?,?);",
      id, json);

    return rowAffected == 1;
  }

  public String readJobInput(String id) {
    try {
      String json = jdbcTemplate.queryForObject(
        "SELECT Json FROM  JobInput WHERE Id = ?", String.class, id);
      return json;
    } catch (IncorrectResultSizeDataAccessException ex) {
      return null;
    }
  }

  public boolean deleteJobInput(String id) {
    int rowCount = jdbcTemplate.update("DELETE FROM JobInput where Id = ?", id);
    return rowCount == 1;
  }

  public boolean updateJobInput(String id, String json) {
    int rowCount = jdbcTemplate.update(
      "UPDATE JobInput SET Json = ? WHERE Id = ?", json, id);

    return rowCount == 1;
  }
}
