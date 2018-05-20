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
package org.allnix.oil.lab;


import org.allnix.oil.lab.model.Core;
import org.allnix.oil.lab.repository.CoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
@EnableJpaRepositories("org.allnix.oil.lab.repository")
@EntityScan(basePackages= {"org.allnix.oil.lab.model"})
public class RepositoryTest {

    @Autowired
    private CoreRepository coreDao;
    
    @Transactional // I want to see the data 
    @Test
    @Tag("milliseconds")
    public void testCoreRepository() {
        Core obj = new Core();
        String id = obj.id();
        
        Assertions.assertNotNull(id);
        
        Core per = coreDao.save(obj);
        Assertions.assertNull(per.name());
        Assertions.assertNull(per.topCoreDepth());
        
        obj = per;
        
        obj.topCoreDepth(1000.);
        obj.bottomCoreDepth(1100.);
        per = coreDao.save(obj);
        Assertions.assertEquals(1000., per.topCoreDepth().doubleValue());
        Assertions.assertEquals(1100., per.bottomCoreDepth().doubleValue());
      
        obj = per;
        
        String name = "What's up!";
        obj.name(name);
        per = coreDao.save(obj);
        Assertions.assertEquals(name, per.name());
        
    }
}
