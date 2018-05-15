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
package org.allnix.oil;

import java.util.List;

import org.allnix.oil.lab.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectService {
    static final private Logger logger //
            = LoggerFactory.getLogger(ProjectService.class);

    /**
     * 
     * @param name
     * @return
     */
    public Well findFirstWellByName(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Well> findAllWellByName(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Core> findAllCore(Well well) {
        throw new UnsupportedOperationException();
    }

    /**
     * Find combined DCT CSV from all cores
     * 
     * @param well
     * @return
     */
    public DctCsv findDctCsv(Well well) {
        throw new UnsupportedOperationException();
    }

    /**
     * Find the DCT CSV for the given Core
     * 
     * @param core
     * @return
     */
    public DctCsv findDctCsv(Core core) {
        throw new UnsupportedOperationException();
    }
}
