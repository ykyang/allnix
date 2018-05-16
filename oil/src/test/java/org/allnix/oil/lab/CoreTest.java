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

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * http://www.baeldung.com/spring-boot-testing
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
//@SpringBootTest
@ExtendWith(SpringExtension.class)
//@SpringJUnitConfig(classes = { org.allnix.oil.lab.TestConfig.class })
@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
public class CoreTest {
    static final private Logger logger = LoggerFactory.getLogger(CoreTest.class);
    @Autowired
    private CoreRepository coreDao;
    
    @Test
    @Tag("seconds")
    public void test() {
        coreDao.save(new Core("Core 1", 1000., 1100.));
        coreDao.save(new Core("Core 2", 1100., 1200.));
        
//        List<Core> coreList = coreDao.findByName("Core 1");
        Iterable<Core> itab = coreDao.findAll();
        itab.forEach((v)->{
            logger.info(v.getName());
        });
    }
}
