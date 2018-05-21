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

import org.springframework.stereotype.Component;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@Component
public class Runner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
//    @Autowired
//    private CoreRepository coreDao;
//    @Autowired
//    private LabService labSrv;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Hello");

//        labSrv.save(new Core("Core 1", 1000., 1100.));
//        labSrv.save(new Core("Core 2", 1100., 1200.));
//        List<Core> ans = labSrv.findByName("Core 1");
//        ans.forEach((v) -> {
//            logger.info("Core Name: {}", v.getName());
//        });
        // Iterable<Core> itab = coreDao.findAll();
        // itab.forEach((v)->{
        // logger.info(v.getName());
        // });
    }

}
