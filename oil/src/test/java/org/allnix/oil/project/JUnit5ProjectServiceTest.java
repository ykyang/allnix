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
package org.allnix.oil.project;

import java.util.Optional;

import org.allnix.oil.TestNGSpringApplication;
import org.allnix.oil.project.model.Well;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootApplication(exclude = { MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class })
@SpringBootTest(classes = TestNGSpringApplication.class,
        webEnvironment = WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
@TestInstance(Lifecycle.PER_CLASS)
public class JUnit5ProjectServiceTest {
    static final private Logger logger = //
        LoggerFactory.getLogger(JUnit5ProjectServiceTest.class);
    
    @Autowired
    private DefaultProjectService ps;
    @Autowired
    private ProjectLoader pl;

    @BeforeAll
    public void beforeClass() {
        pl.create();
    }

    @Commit
    @Test
    @Tag("seconds")
    public void test() {
        Optional<Well> wellOpt = //
            ps.findFirstWellByName(ProjectLoader.BILLY_BOB);
        Assertions.assertNotNull(wellOpt.orElse(null));
    }
}
