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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@Configuration
@EnableTransactionManagement
public class CanaryAirlineConfig {
  static private Logger logger = LoggerFactory.getLogger(CanaryAirlineConfig.class);
  
  @Bean
  public DataSource dataSource() throws IOException {
    Path path = Paths.get("canary-airline-a694a2cf").toAbsolutePath();
    FileUtils.forceMkdir(path.toFile());
    path = path.resolve("h2");
    String url = String.format("jdbc:h2:%s", path.toString());

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setMaximumPoolSize(10);
    logger.debug("AutoCommit: {}", config.isAutoCommit());
    config.setAutoCommit(false);
//                config.addDataSourceProperty("cachePrepStmts", "true");
//                config.addDataSourceProperty("prepStmtCacheSize", "250");
//                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    DataSource bean = new HikariDataSource(config);

    return bean;
  }
  
  @Bean
  public PlatformTransactionManager txManager() throws IOException {
    return new DataSourceTransactionManager(dataSource());
  }
}
