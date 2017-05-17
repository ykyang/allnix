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
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import static org.allnix.sql24.TestCanaryAirlineConfig.DATABASE_URL;
import org.allnix.sql24.model.Aircraft;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestCanaryAirlineService {

  static private final Logger logger = LoggerFactory.getLogger(
          TestCanaryAirlineService.class);

  private Path dbFolder;
  private String dbName;
  private Path dbFile;
  private String url;

  private AnnotationConfigApplicationContext ctx;
  private CanaryAirlineService service;

  @BeforeClass
  void beforeClass() throws IOException {
    dbName = "canaryairline";
    dbFolder = Paths.get("h2-3e254c70").toAbsolutePath();
    FileUtils.forceMkdir(dbFolder.toFile());
    dbFile = dbFolder.resolve(dbName);
    // > Use H2 database
    url = String.format("jdbc:h2:%s", dbFile.toString());

    // > Populate database
    HikariDataSource dataSource = dataSource();
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScripts(
            new FileSystemResource("/home/ykyang/work/h2/h2.schema.sql"),
            new FileSystemResource("/home/ykyang/work/h2/h2.data.sql")
    );
    populator.execute(dataSource);
    dataSource.close();
    
    // > Set H2 database path
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map<String,Object> myMap = new HashMap<>();
    myMap.put(TestCanaryAirlineConfig.DATABASE_URL, url);
    propertySources.addFirst(new MapPropertySource("MY_MAP", myMap));

    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(environment);
    ctx.register(TestCanaryAirlineConfig.class);
    ctx.refresh();
    ctx.registerShutdownHook();

    service = ctx.getBean(CanaryAirlineService.class);
  }
  
  public HikariDataSource dataSource() {
    logger.info("Canary Airline URL: {}", url);

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setMaximumPoolSize(1);
    config.setAutoCommit(true);
    HikariDataSource bean = new HikariDataSource(config);
    
    return bean;
  }
  
  @AfterClass
  void afterClass() {
    ctx.close();
    FileUtils.deleteQuietly(dbFolder.toFile());
  }

  @Test
  public void testAircraft() {
      String aircraftCode;
    Aircraft aircraft;

    aircraftCode = "146";
    aircraft = service.findOneAircraft(aircraftCode);
    Assert.assertNotNull(aircraft);
    Assert.assertEquals(aircraft.getAircraftCode(), aircraftCode);
    Assert.assertEquals(aircraft.getAircraftType(), "British Aerospace BAe146-100");
    Assert.assertFalse(aircraft.getFreightOnly());
    Assert.assertEquals(aircraft.getSeating(), Integer.valueOf(82));
  }
}
