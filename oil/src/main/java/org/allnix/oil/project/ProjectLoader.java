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

import java.util.ArrayList;
import java.util.List;

import org.allnix.oil.lab.DefaultLabService;
import org.allnix.oil.lab.model.Core;
import org.allnix.oil.project.model.Project;
import org.allnix.oil.project.model.Well;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectLoader {
    static final private Logger logger = LoggerFactory.getLogger(ProjectLoader.class);
    static final public String ROSE_CHILDREN = "Rose Children #2v";
    static final public String BILLY_BOB = "Billy Bob Joe V34";
    @Autowired
    private DefaultProjectService ps;
    @Autowired
    private DefaultLabService ls;
    
    public void create() {
        Project proj;
        Well well;
        
        proj = createProject("2018-05-16-Rose");
        well = createWell(ROSE_CHILDREN);
        well.setProjectId(proj.id());
        well.addParentId(proj.id());
        ps.save(well);
        createCore(well, 10);
        
        
        
        well = createWell(BILLY_BOB);
        createCore(well, 4);
    }
    
    public Project createProject(String projName) {
        Project proj = new Project();
        proj.setName(projName);
        return ps.save(proj);
    }
    
    public Well createWell(String wellName) {
        Well well = new Well();
        well.setName(wellName);
        return ps.save(well);
    }
    
    public List<Core> createCore(Well well, int coreCount) {
        List<Core> coreList = new ArrayList<>();
        
        for (int i = 1; i <= coreCount; i++) {
            Core core = new Core();
            core.setName("Core " + Integer.toString(i));
            core.setWellId(well.id());
            
            core = ls.save(core); 
            
            coreList.add(core);
        }
        
        return coreList;
    }
}
