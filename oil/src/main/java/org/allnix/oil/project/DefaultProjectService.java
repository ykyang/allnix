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

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.allnix.oil.lab.model.Core;
import org.allnix.oil.lab.model.Ct;
import org.allnix.oil.project.model.Project;
import org.allnix.oil.project.model.Well;
import org.allnix.oil.project.repository.ProjectRepository;
import org.allnix.oil.project.repository.WellRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultProjectService {
    static final private Logger logger //
            = LoggerFactory.getLogger(DefaultProjectService.class);
    @Autowired
    private WellRepository wellDao;
    @Autowired
    private ProjectRepository projectDao;
    @Transactional
    public Optional<Project> findProjectById(String id) {
        return projectDao.findById(id);
    }
    @Transactional
    public Optional<Project> findProjectByName(String name) {
        return projectDao.findByName(name);
    }
    /**
     * 
     * @param name
     * @return
     */
    @Transactional
    public Optional<Well> findFirstWellByName(String name) {
        return wellDao.findFirstByName(name);
    }

    @Transactional
    public List<Well> findWellByName(String name) {
        return wellDao.findByName(name);
    }

    @Transactional
    public Well save(Well well) {
        logger.info("WellDao: {}", wellDao.getClass().getName());
        return wellDao.save(well);
    }
    @Transactional
    public Project save(Project project) {
        return projectDao.save(project); 
    }
    
    public List<Core> findAllCore(Well well) {
        throw new UnsupportedOperationException();
    }
    
//    public Optional<Well> findFirstWellByProjectId(String projectId) {
//        wellDao.find
//    }
    @Transactional
    public List<Well> findWellByProjectId(String projectId) {
        return wellDao.findByProjectId(projectId);
    }
    @Transactional
    public List<Well> findWellByParentId(String parentId) {
        return wellDao.findByParentId(parentId);
    }

    /**
     * Find combined DCT CSV from all cores
     * 
     * @param well
     * @return
     */
    public Ct findDctCsv(Well well) {
        throw new UnsupportedOperationException();
    }

    /**
     * Find the DCT CSV for the given Core
     * 
     * @param core
     * @return
     */
    public Ct findDctCsv(Core core) {
        throw new UnsupportedOperationException();
    }
}
