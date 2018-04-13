/*
 * Copyright 2018 Yi-Kun Yang.
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

import org.allnix.core.BlobDao;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlBlobDao implements BlobDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate value) {
        this.jdbcTemplate = value;
    }

    public void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS %s (\n"
                + "  Id    CHAR(36)        PRIMARY KEY,\n"
                + "  Array BLOB            NOT NULL\n" + ")";

        sql = String.format(sql, tableName);

        jdbcTemplate.execute(sql);
    }

    @Override
    public boolean create(String tableName, String id, byte[] array) {
        final String insert = "INSERT INTO %s VALUES (?,?)";
        
        String sql = String.format(insert, tableName);
        try {
          int rowAffected = jdbcTemplate.update(sql, id, array);

          return rowAffected == 1;
        } catch (org.springframework.dao.DuplicateKeyException e) {
          return false;
        }
    }

    @Override
    public byte[] read(String tableName, String id) {
        final String read = "SELECT Array FROM %s WHERE Id = ?";

        String sql = String.format(read, tableName);

        try {
          byte[] blob= jdbcTemplate.queryForObject(sql, byte[].class, id);
          return blob;
        } catch (IncorrectResultSizeDataAccessException ex) {
          return null;
        }
    }
}
