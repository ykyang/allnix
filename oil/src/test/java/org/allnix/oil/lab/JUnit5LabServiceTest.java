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
import java.util.Optional;

import org.allnix.oil.TestSpringApplication;
import org.allnix.oil.lab.model.Core;
import org.allnix.oil.project.DefaultProjectService;
import org.allnix.oil.project.ProjectLoader;
import org.allnix.oil.project.model.Well;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = TestSpringApplication.class,
webEnvironment = WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
@TestInstance(Lifecycle.PER_CLASS)
public class JUnit5LabServiceTest {
    @Autowired
    private DefaultProjectService ps;
    @Autowired
    private DefaultLabService ls;
    @Autowired
    private ProjectLoader pl;
    
    @BeforeAll
    public void beforaAll() {
        pl.create();
    }
    
    @Commit
    @Test
    @Tag("seconds")
    public void test() {
        Well well;
        Optional<Well> wellOpt;
        List<Core> coreList;
        
        wellOpt = //
            ps.findFirstWellByName(ProjectLoader.ROSE_CHILDREN);
        Assertions.assertNotNull(wellOpt.orElse(null));
        well = wellOpt.get();
        
        //< core >//
        coreList = ls.findCoreByWell(well);
        Assertions.assertEquals(10, coreList.size());
    }
}
