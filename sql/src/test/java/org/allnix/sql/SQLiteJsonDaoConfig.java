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

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteJDBCLoader;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 * @deprecated 
 */
@Configuration
public class SQLiteJsonDaoConfig {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  @Qualifier("jobDatabaseName")
  private String databaseName;

  @Bean
  public SqliteJsonDao newSQLiteJsonDao() throws Exception {
    // > Initialize SQLite driver
    boolean success = SQLiteJDBCLoader.initialize();

    SqliteJsonDao bean = new SqliteJsonDao();

    // > Create SQLite data source
    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setUrl("jdbc:sqlite:" + databaseName);
    // > Maximum number of connection = 1
    // > SQLite cannot have more than 1 connection
    // > in multi-thread mode
    basicDataSource.setMaxTotal(1);

    jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(basicDataSource);
    // > Not waiting for data actually writing to the disk
    // > The performance is not acceptable for my use case
    // > without this setting.
    jdbcTemplate.execute("pragma synchronous = off;");

    bean.setJdbcTemplate(jdbcTemplate);

    return bean;
  }

//  @Bean
//  public DataSource defaultDataSource() {
//    BasicDataSource bean = new BasicDataSource();
//    bean.setUrl("jdbc:sqlite:" + "job.db");
//    
//    return bean;
//  }
}
