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
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@Configuration
public class HsqlJdbcConfig {
  static private Logger logger = LoggerFactory.getLogger(HsqlJdbcConfig.class);
  static public final String DATABASE = HsqlJdbcConfig.class.getName() + ".database";
  
  @Autowired
  private ConfigurableEnvironment env;
  
  @Bean
  public BasicDataSource hsqlDataSource() {
    String database = env.getProperty(DATABASE);
    String url = "jdbc:hsqldb:file:"+database;

    // > Create H2 data source
    BasicDataSource bean = new BasicDataSource();
    bean.setUrl(url);
    
    return bean;
  } 
  
  @Bean  
  public JdbcTemplate hsqlJdbcTemplate() {
    JdbcTemplate bean = new JdbcTemplate();
    bean.setDataSource(hsqlDataSource());
    
    return bean;
  }
  
  @Bean
  public HsqlJsonDao jsonDao() {
    HsqlJsonDao bean = new HsqlJsonDao();
    bean.setJdbcTemplate(hsqlJdbcTemplate());
    
    return bean;
  }
}
