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
package org.allnix.sql24;

import java.util.List;
import org.allnix.sql24.model.Aircraft;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class AircraftDao {

  private JdbcTemplate jdbcTemplate;
  private final String SCHEMA = "CANARYAIRLINES";
  private final String TABLE = "AIRCRAFT";
  private BeanPropertyRowMapper<Aircraft> rowMapper;

  public AircraftDao() {
    rowMapper = BeanPropertyRowMapper.newInstance(Aircraft.class);
  }
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public long count() {
    final String sql = String.format("SELECT * FROM %s.%s", SCHEMA, TABLE);

    RowCountCallbackHandler countCallback = new RowCountCallbackHandler();  // not reusable
    jdbcTemplate.query(sql, countCallback);
    int rowCount = countCallback.getRowCount();
 
    return rowCount;
  }
  
  public Iterable<Aircraft> findAll() {
    final String sql = String.format("SELECT * FROM %s.%s", SCHEMA, TABLE);
    List<Aircraft> ans = jdbcTemplate.query(sql, rowMapper);
    
    return ans;
  }
  
  public Aircraft findOne(String aircraftCode) {
    if (aircraftCode == null) {
      throw new IllegalArgumentException("Aircraft code is null");
    }

    final String sql = String.format(
            "SELECT * FROM %s.%s WHERE AIRCRAFTCODE = ?", SCHEMA, TABLE);

    try {
      Aircraft obj = jdbcTemplate.queryForObject(
              sql,
              rowMapper,
              aircraftCode);

      return obj;
    } catch (IncorrectResultSizeDataAccessException e) {
      return null;
    }
  }

  public void deleteAircraft(String aircraftCode) {
    if (aircraftCode == null) {
      throw new IllegalArgumentException("Aircraft code is null");
    }
    final String sql = String.format(
            "DELETE FROM %s.%s WHERE AIRCRAFTCODE = ?",
            SCHEMA, TABLE);

    // Not used: int rowAffected = 
    jdbcTemplate.update(sql, aircraftCode);
  }
}
