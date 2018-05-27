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
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

@SpringBootApplication(exclude = { MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class })
@SpringBootTest(classes = TestNGSpringApplication.class,
        webEnvironment = WebEnvironment.NONE)
// @ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
// @TestInstance(Lifecycle.PER_CLASS)
public class ProjectServiceTest extends AbstractTestNGSpringContextTests {
    static final private Logger logger = //
            LoggerFactory.getLogger(ProjectServiceTest.class);
    @Autowired
    private DefaultProjectService ps;
    @Autowired
    private ProjectLoader pl;

    // @BeforeAll
    @BeforeClass(alwaysRun=true)
    public void beforeClass() {
        logger.info("beforeClass");
        pl.create();
    }

    @Commit
    @org.testng.annotations.Test(groups = { "seconds" })
    public void test() {
//        logger.info("test create");
//        pl.create();
        Optional<Well> wellOpt = ps
                .findFirstWellByName(ProjectLoader.ROSE_CHILDREN);
        Assert.assertNotNull(wellOpt.orElse(null));
    }

}
