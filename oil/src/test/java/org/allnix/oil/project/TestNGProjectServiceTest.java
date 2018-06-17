/*
 * Copyright 2018 Yi-Kun Yang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.allnix.oil.project;

import java.util.Optional;

import org.allnix.oil.TestSpringApplication;
import org.allnix.oil.project.model.Well;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ActiveProfiles("int-test")
//@SpringBootApplication(exclude = { MongoAutoConfiguration.class,
//    MongoDataAutoConfiguration.class })
@SpringBootTest(classes = TestSpringApplication.class,
    webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(classes = { TestConfig.class })
public class TestNGProjectServiceTest extends AbstractTestNGSpringContextTests {
    static final private Logger logger = //
        LoggerFactory.getLogger(TestNGProjectServiceTest.class);
    @Autowired
    private DefaultProjectService ps;
    @Autowired
    private ProjectLoader pl;

    @BeforeClass(groups = { "all" })
    public void beforeClass() {
        pl.create();
    }

    @Commit
    @Test(groups = { "seconds" })
    public void test() {
        Well well;
        Optional<Well> wellOpt;
        
        wellOpt = ps.findFirstWellByName("Well not exists!");
        
        wellOpt = //
            ps.findFirstWellByName(ProjectLoader.ROSE_CHILDREN);
        Assert.assertNotNull(wellOpt.orElse(null));
        
        well = wellOpt.get();
        
    }

}
