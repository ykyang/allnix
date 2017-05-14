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

import org.allnix.sql24.model.Aircraft;
import org.allnix.sql24.model.AircraftFleet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class CanaryAirlineDao {
  static private final Logger logger = LoggerFactory.getLogger(CanaryAirlineDao.class);
  private JdbcTemplate jdbcTemplate;
  private final String SCHEMA = "CANARYAIRLINES";
  private final RowMapper<AircraftFleet> aircraftFleetMapper;

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public CanaryAirlineDao() {
    aircraftFleetMapper = new BeanPropertyRowMapper(AircraftFleet.class);
  }

  /**
   *
   * @param aircraftCode Aircraft code
   * @return Aircraft or null if aircraftCode does not exist
   */
  public Aircraft readAircraft(String aircraftCode) {
    final String sql = "SELECT * FROM CANARYAIRLINES.AIRCRAFT WHERE AIRCRAFTCODE = ?";
//    String sql = String.format(template, aircraftCode);

    try {
      Aircraft obj = jdbcTemplate.queryForObject(
              sql,
              BeanPropertyRowMapper.newInstance(Aircraft.class),
              aircraftCode);

      return obj;
    } catch (IncorrectResultSizeDataAccessException e) {
      return null;
    }
  }

  public boolean deleteAircraft(String aircraftCode) {
    final String sql = String.format(
            "DELETE FROM %s.%s WHERE AIRCRAFTCODE = ?",
            SCHEMA, "AIRCRAFT");

    int rowAffected = jdbcTemplate.update(sql, aircraftCode);

    return rowAffected == 1;
  }

  public AircraftFleet readAircraftFleet(int aircraftFleetId) {
    final String sql = String.format(
            "SELECT * FROM %s.%s WHERE AIRCRAFTFLEETID = ?",
            SCHEMA, "AIRCRAFTFLEET");

    try {
      AircraftFleet ans = jdbcTemplate.queryForObject(sql, aircraftFleetMapper,
              aircraftFleetId);

      return ans;
    } catch (IncorrectResultSizeDataAccessException e) {
      return null;
    }
  }
}
