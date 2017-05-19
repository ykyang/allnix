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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
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
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@Configuration
public class H2JdbcConfig {
  static private Logger logger = LoggerFactory.getLogger(H2JdbcConfig.class);
  static public final String DATABASE_URL = H2JdbcConfig.class.getName() + ".database";
  
  @Autowired
  private ConfigurableEnvironment env;
  
  @Bean
  public DataSource h2DataSource() {
    String url = env.getProperty(DATABASE_URL);
    logger.info("H2 BasicDataSource URL: {}", url);
    // > Create H2 data source
//    DataSource bean = new BasicDataSource();
//    bean.setUrl(url);

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setMaximumPoolSize(10);
    logger.debug("AutoCommit: {}", config.isAutoCommit());
//    config.setAutoCommit(false);
//                config.addDataSourceProperty("cachePrepStmts", "true");
//                config.addDataSourceProperty("prepStmtCacheSize", "250");
//                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    DataSource bean = new HikariDataSource(config);
    
    return bean;
  }

  @Bean  
  public JdbcTemplate h2JdbcTemplate() {
    JdbcTemplate bean = new JdbcTemplate();
    bean.setDataSource(h2DataSource());
    
    return bean;
  }
  
  @Bean
  public SqlJsonDao jsonDao() {
    SqlJsonDao bean = new SqlJsonDao();
    bean.setJdbcTemplate(h2JdbcTemplate());
    
    return bean;
  }
  
}