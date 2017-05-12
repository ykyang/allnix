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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class CanaryAirlineDao {

  private JdbcTemplate jdbcTemplate;

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
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
}
