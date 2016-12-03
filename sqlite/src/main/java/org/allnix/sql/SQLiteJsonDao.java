/*
 * Copyright 2016 .
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

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

/**
 * A DAO for storing JSON in SQLite
 * 
 * See TestSQLiteJsonDao for usage example
 * 
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class SQLiteJsonDao {

  private JdbcTemplate jdbcTemplate;

//  public void init() throws Exception {
//    boolean success = SQLiteJDBCLoader.initialize();
//    SQLiteDataSource dataSource = new SQLiteDataSource();
//    dataSource.setUrl("jdbc:sqlite:job.db");
//    
//    BasicDataSource basicDataSource = new BasicDataSource();
//    basicDataSource.setUrl("jdbc:sqlite:job.db");
////    basicDataSource.setUrl("jdbc:sqlite::memory:");
//    basicDataSource.setMaxTotal(1);
//
//    jdbcTemplate = new JdbcTemplate(basicDataSource);
//    
//    pragmaSynchronous(false);
//  }
//
//  public void pragmaSynchronous(boolean onoff) {
//    if (onoff) {
//      jdbcTemplate.execute("pragma synchronous = on;");
//    } else {
//     jdbcTemplate.execute("pragma synchronous = off;");
//    }
//  }
  
  public void setJdbcTemplate(JdbcTemplate value) {
    this.jdbcTemplate = value;
  }
  public void createTable(String tableName) {
    String sql
      = "CREATE TABLE IF NOT EXISTS %s (\n"
      + "  Id    CHAR(36)        PRIMARY KEY,\n"
      + "  Json  TEXT            NOT NULL\n"
      + ");";

    sql = String.format(sql, tableName);

    jdbcTemplate.execute(sql);
  }

  public boolean create(String tableName, String id, String json) {
    final String insert = "INSERT OR IGNORE INTO %s VALUES (?,?);";
    
    String sql = String.format(insert, tableName);
    int rowAffected = jdbcTemplate.update(sql, id, json);

    return rowAffected == 1;
  }

  public boolean delete(String tableName, String id) {
    final String delete = "DELETE FROM %s where Id = ?";
    
    String sql = String.format(delete, tableName);
    int rowAffected = jdbcTemplate.update(sql, id);
    
    return rowAffected == 1;
  }
  
  public String read(String tableName, String id) {
    final String read = "SELECT Json FROM %s WHERE Id = ?";
    
    String sql = String.format(read, tableName);
    
    try {
      String json = jdbcTemplate.queryForObject(sql, String.class, id);
      return json;
    } catch(IncorrectResultSizeDataAccessException ex) {
      return null;
    }
  }
  
  public boolean update(String tableName, String id, String json) {
    final String update = "UPDATE %s SET Json = ? WHERE Id = ?";
    String sql = String.format(update, tableName);
    
    int rowCount = jdbcTemplate.update(sql, json, id);
    
    return rowCount == 1;
  }
}
