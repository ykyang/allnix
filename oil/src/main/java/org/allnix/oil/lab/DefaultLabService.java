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

import javax.transaction.Transactional;

import org.allnix.oil.lab.model.Core;
import org.allnix.oil.lab.model.CtLog;
import org.allnix.oil.lab.repository.CoreRepository;
import org.allnix.oil.lab.repository.CtRepository;
import org.allnix.oil.project.model.Well;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultLabService {
    @Autowired
    private CoreRepository coreDao;
    @Autowired
    private CtRepository ctDao;
    
    @Transactional
    public Core save(Core core) {
        return coreDao.save(core);
    }
    public CtLog save(CtLog ct) {
        return ctDao.save(ct);
    }
    
    @Transactional
    public List<Core> findCoreByWell(Well well) {
        return coreDao.findByWellId(well.id());
    }
    
    public Optional<CtLog> findCtByCore(Core core) {
        return ctDao.findByCoreId(core.id());
    }
//    @Transactional
//    public List<Core> findByName(String name) {
//        return coreDao.findByName(name);
//    }
}
