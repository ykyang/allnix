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
package org.allnix.hf;

import java.util.concurrent.TimeUnit;

public class Datastore {
    
    /**
     * Asynchronously load data
     * 
     * Load data from remote WS or database to local cache.
     * Use readData() to read from local cache.
     *
     * Most likely an I/O bound function
     * @param id
     * @return handle to the data, for example file name of MAT file
     * ready to be read from MATLAB
     */
    public String loadData(String id) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Done";
//        throw new UnsupportedOperationException();
    }

}
