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
public class DerbyJdbcConfig {
  static private Logger logger = LoggerFactory.getLogger(DerbyJdbcConfig.class);
  static public final String DATABASE_URL = DerbyJdbcConfig.class.getName() + ".database";
  
  @Autowired
  private ConfigurableEnvironment env;
  
  @Bean
  public BasicDataSource dataSource() {
    String url = env.getProperty(DATABASE_URL);
    
    BasicDataSource bean = new BasicDataSource();
    bean.setUrl(url);
    
    return bean;
  }
  @Bean
  public JdbcTemplate jdbcTemplate() {
    JdbcTemplate bean = new JdbcTemplate();
    bean.setDataSource(dataSource());
    
    return bean;
  }
  
  @Bean
  public SqlJsonDao jsonDao() {
    DerbyJsonDao bean = new DerbyJsonDao();
    bean.setJdbcTemplate(jdbcTemplate());
    
    return bean;
  }
}
