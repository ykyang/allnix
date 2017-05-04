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

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
@Configuration
public class SqliteJdbcConfig {
  static private Logger logger = LoggerFactory.getLogger(SqliteJdbcConfig.class);
  static public final String DATABASE_URL = SqliteJdbcConfig.class.getName()+".database";
  
  @Autowired
  private ConfigurableEnvironment env;
  
  @Bean
  public BasicDataSource sqliteDataSource() {
    String url = env.getProperty(DATABASE_URL);
    logger.info("SQLite BasicDataSource URL: {}", url);
    BasicDataSource bean = new BasicDataSource();
    bean.setUrl(url);
    
    // > Maximum number of connection = 1
    // > SQLite cannot have more than 1 connection
    // > in multi-thread mode
    bean.setMaxTotal(1);
    
    return bean;
  }
  
  @Bean
  public JdbcTemplate sqliteJdbcTemplate() {
    JdbcTemplate bean = new JdbcTemplate();
    
    // > Initialize SQLite driver; does not seem to be necessary
    // boolean success = SQLiteJDBCLoader.initialize();
    bean.setDataSource(sqliteDataSource());
    
    // > Not waiting for data actually writing to the disk
    // > The performance is not acceptable for my use case
    // > without this setting.
    bean.execute("pragma synchronous = off;");

    return bean;
  }
  
  @Bean
  public SqliteJsonDao sqliteJsonDao() {
    SqliteJsonDao bean = new SqliteJsonDao();
    bean.setJdbcTemplate(sqliteJdbcTemplate());
    
    return bean;
  }
}
