/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class Main {

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
      + "  ID    CHAR(36)        PRIMARY KEY,\n"
      + "  JSON  TEXT            NOT NULL\n"
      + ");";

    jdbcTemplate.execute(sql);

    sql = "CREATE TABLE IF NOT EXISTS Job (\n"
      + "ID    CHAR(36)        PRIMARY KEY,\n"
      + "JSON  TEXT            NOT NULL\n"
      + ");\n";

    jdbcTemplate.execute(sql);

  }

  public int delete() {
    int rowAffected = jdbcTemplate.update("DELETE FROM JobInput;");
    
    return rowAffected;
  }
  
  public int insert(String id, String json) {
    int rowAffected = jdbcTemplate.update(
      "INSERT OR IGNORE INTO JOBINPUT VALUES (?,?);",
//      "INSERT INTO JOBINPUT VALUES (?,?);",
      id, json);
    
    return rowAffected;
  }

  public int update(String id, String json) {
    int rowAffected = jdbcTemplate.update(
      "UPDATE JobInput SET Json = ? WHERE Id = ?", json, id);

    return rowAffected;
  }

  public String queryForObject(String id) {
    String sql = "select json from jobinput where id = ?";

    String json = jdbcTemplate.queryForObject(sql,
      new Object[]{id},
      new RowMapper<String>() {
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        String result = rs.getString("JSON");
        return result;
      }
    });

    return json;
  }

  public boolean hasObject(String id) {
    // Source: http://stackoverflow.com/questions/1676551/best-way-to-test-if-a-row-exists-in-a-mysql-table
    // SELECT EXISTS(SELECT 1 FROM test2 WHERE id ='321321' LIMIT 1)
    String sql = "select exists(select 1 from JobInput where id = ? limit 1)";
    Boolean exist = jdbcTemplate.queryForObject(sql, Boolean.class, id);

    return exist;

//    Integer exist = jdbcTemplate.queryForObject(sql, Integer.class, id);
//    
//    return exist == 1;
  }

  static public void main(String[] args) throws Exception {
    Main driver = new Main();
    String id, json;
    int rowCount;

    driver.init();
    driver.createTable();

    id = "e5a482e5-ceed-4cdb-b82f-4dcdfc7757cb";
    json = "{\"metho\":\"simulate\"}";
    driver.insert(id, json);
    // query
    json = driver.queryForObject(id);

    System.out.println(json);
    // exist
    System.out.println("ID: " + id + " exists: " + driver.hasObject(id));
    id = "1234";
    System.out.println("ID: " + id + " exists: " + driver.hasObject(id));
    // update
    rowCount = driver.update(id, json);
    System.out.println("Row Count: " + rowCount);
    
    id = "e5a482e5-ceed-4cdb-b82f-4dcdfc7757cb";
    json = "json here";
    rowCount = driver.update(id, json);
    System.out.println("Row Count: " + rowCount);
    
    rowCount = driver.delete();
    System.out.println("Row Deleted: " + rowCount);
  }
}
