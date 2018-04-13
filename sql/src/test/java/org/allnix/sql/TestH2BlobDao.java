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

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(singleThreaded = true)
public class TestH2BlobDao {
    static private final Logger logger = LoggerFactory.getLogger(TestH2BlobDao.class);
    
    private SqlBlobDao dao;
    static private final String BLOB_TABLE = "blob_table";
    
    private Path databaseFolder;
    private Path databaseFile;
    private String databaseFileName;
    
    private String url;
    private AnnotationConfigApplicationContext ctx;
    
    @BeforeClass
    void beforeClass() throws Exception {
      logger.debug("beforeTest()");
      
      databaseFileName = "job";
      databaseFolder = Paths.get("/f/ykyang/data/h2").toAbsolutePath();
      FileUtils.forceMkdir(databaseFolder.toFile());
      databaseFile = databaseFolder.resolve(databaseFileName);
      url = String.format("jdbc:h2:%s", databaseFile.toString());
      
//      Path database = Paths.get("h2-job").toAbsolutePath();
//      databaseFileName = 
      
      // > Set database name
      logger.info("H2 database name property key: {}", H2JdbcConfig.DATABASE_URL);
      logger.info("H2 database URL: {}", url);
      
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
      
      dao = ctx.getBean(SqlBlobDao.class);
      dao.createTable(BLOB_TABLE);
    }
    
    @AfterClass
    void afterClass() {
      ctx.close();
//      FileUtils.deleteQuietly(databaseFolder.toFile());
    }
    
    @Test//(threadPoolSize = 2, invocationCount = 2)
    public void testBlob() {
        // 14:25:23.875 [pool-1-thread-1] INFO org.allnix.sql.TestH2BlobDao -
        // Insert time: 139976
        // 14:25:23.875 [pool-1-thread-1] INFO org.allnix.sql.TestH2BlobDao -
        // Insert time per doc: 13.9976
        // 14:25:24.431 [pool-1-thread-1] INFO org.allnix.sql.TestH2BlobDao -
        // Query time: 555
        String tableName = BLOB_TABLE;
        String id = UUID.randomUUID().toString();
        
        int n = 300_000;
        int start = 1;
        int end = 10_000;
        int docCount = end - start + 1;
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES*n);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        
        for ( int j = 0; j < n; j++) {
            double v = j+1+13.;
            // - Do not delete
            // - Pay attention to the index
            // byteBuffer.putDouble(j*Double.BYTES, v);
            doubleBuffer.put(j, v);
        }
        
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        String varid = null;
        for (int i = start; i <= end; i++) {
            id = UUID.randomUUID().toString();
            
            if ( i == (start+end)/2) {
                varid = id;
            }
            
            watch.resume();
            dao.create(tableName, id, byteBuffer.array());
            watch.suspend();
            
            logger.info("Document Ind: {}", i);
        }
        
        logger.info("Insert time: {}", watch.getTime());
        logger.info("Insert time per doc: {}", watch.getTime()/(double)docCount);
        
        watch.reset();
        watch.start();
        byte[] array = dao.read(tableName, varid);
        watch.suspend();
        logger.info("Query time: {}", watch.getTime());
        {
            ByteBuffer bf = ByteBuffer.wrap(array);
            DoubleBuffer dB = bf.asDoubleBuffer();
            for (int j = 0; j < n; j++) {
                double v = j+1+13;
                Assert.assertEquals(dB.get(j), v);
            }
        }
    }
}
