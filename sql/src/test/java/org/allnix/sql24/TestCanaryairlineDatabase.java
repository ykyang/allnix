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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.allnix.sql.H2JdbcConfig;
import org.allnix.sql24.model.Aircraft;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestCanaryairlineDatabase {

  static private final Logger logger = LoggerFactory.getLogger(TestCanaryairlineDatabase.class);

  private Path dbFolder;
  private String dbName;
  private Path dbFile;
  private String url;
  private String schema;

  private AnnotationConfigApplicationContext ctx;
  private JdbcTemplate jdbcTemplate;

  private CanaryAirlineDao dao;

  @BeforeClass
  void beforeClass() throws IOException {
    dbName = "canaryairline-3e254c70";
    dbFolder = Paths.get("h2").toAbsolutePath();
    FileUtils.forceMkdir(dbFolder.toFile());
    dbFile = dbFolder.resolve(dbName);
    url = String.format("jdbc:h2:%s", dbFile.toString());

    // > Set H2 database path
    ConfigurableEnvironment environment = new StandardEnvironment();
    MutablePropertySources propertySources = environment.getPropertySources();
    Map myMap = new HashMap();
    myMap.put(H2JdbcConfig.DATABASE_URL, url);
    propertySources.addFirst(new MapPropertySource("MY_MAP", myMap));

    ctx = new AnnotationConfigApplicationContext();
    ctx.setEnvironment(environment);
    ctx.register(
            H2JdbcConfig.class
    );
    ctx.refresh();
    ctx.registerShutdownHook();

    // > Populate database
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScripts(
            new FileSystemResource("/home/ykyang/work/h2/h2.schema.sql"),
            new FileSystemResource("/home/ykyang/work/h2/h2.data.sql")
    );
    populator.execute(ctx.getBean("h2DataSource", BasicDataSource.class));

    jdbcTemplate = ctx.getBean(JdbcTemplate.class);

    schema = "CANARYAIRLINES";

    dao = new CanaryAirlineDao();
    dao.setJdbcTemplate(jdbcTemplate);
  }

  @AfterClass
  void afterClass() {
    ctx.close();
    FileUtils.deleteQuietly(dbFolder.toFile());
  }

  /**
   * Insertion failed due to duplicated primary key
   */
  @Test
  public void testAircraftPrimaryKey() {
    final String sql = "Insert into CANARYAIRLINES.AIRCRAFT \n"
            + "(AIRCRAFTCODE,AIRCRAFTTYPE,FREIGHTONLY,SEATING) \n"
            + "values ('146','British Aerospace BAe146-100',0,82)";

    try {
      int rowAffected = jdbcTemplate.update(sql);
      Assert.fail();
    } catch (org.springframework.dao.DuplicateKeyException e) {
      // > Success
    } catch (DataAccessException e) {
      Assert.fail();
    }
  }

  @Test
  public void testAircraftDataType() {
    final String template = "SELECT * FROM %s.%s";
    String sql = String.format(template, schema, "AIRCRAFT");

    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
    SqlRowSetMetaData metaData = rowSet.getMetaData();

    Assert.assertEquals(metaData.getColumnCount(), 4);
    rowSet.last();
    Assert.assertEquals(rowSet.getRow(), 40);

    Assert.assertEquals(metaData.getColumnClassName(1), String.class.getName());
    Assert.assertEquals(metaData.getColumnClassName(2), String.class.getName());
    Assert.assertEquals(metaData.getColumnClassName(3), BigDecimal.class.getName());
    Assert.assertEquals(metaData.getColumnClassName(4), Integer.class.getName());

//    int col;
//    Object obj;
//    rowSet.beforeFirst();
//    while(rowSet.next()) {
//      StringBuilder sb = new StringBuilder();
//      // > Column starts at 1
//      for ( col = 1; col <= metaData.getColumnCount(); col++) {
//        obj = rowSet.getObject(col);
//        if (obj == null) {
//          obj = "{}";
//        }
//        sb.append(obj.toString());
//        sb.append('\t');
//      }
//      logger.debug(sb.toString());
//    }
  }

  @Test
  public void testAircraftDao() {
    String aircraftCode;
    Aircraft aircraft;

    aircraftCode = "146";
    aircraft = dao.readAircraft(aircraftCode);
    Assert.assertNotNull(aircraft);
    Assert.assertEquals(aircraft.getSeating(), Integer.valueOf(82));

    aircraftCode = "310";
    aircraft = dao.readAircraft(aircraftCode);
    Assert.assertNotNull(aircraft);
    Assert.assertEquals(aircraft.getSeating(), Integer.valueOf(198));
    
    // > No such aircraft
    aircraftCode = "ZZZ";
    aircraft = dao.readAircraft(aircraftCode);
    Assert.assertNull(aircraft);
  }
}
