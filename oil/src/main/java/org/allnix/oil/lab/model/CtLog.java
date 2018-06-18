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
package org.allnix.oil.lab.model;

import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.allnix.oil.model.OilObject;
import org.springframework.data.annotation.Transient;

/**
 * Computed tomography data
 * 
 * Log data from Core CT
 *
 * @author Yi-Kun Yang ykyang@gmail.com
 */
@Entity
@Table(indexes = { //
    @Index(columnList = "coreId"), @Index(columnList = "wellId") //
    , @Index(columnList = "parentId")
})
public class CtLog extends OilObject {
    private String name;

    @Column(length = 36)
    private String coreId;
    
    @Column(length = 36)
    private String wellId;

    private Double topCoreDepth;
    private Double bottomCoreDepth;
    /**
     * URL of its the log file
     * 
     * The URL is typically relative, the file is typically CSV 
     */
    private String url;
    /**
     * A convenient placeholder for local file system
     * path when the data is downloaded from datastore. 
     */
    @Transient
    @javax.persistence.Transient
    private Path path;
    
    public String getCoreId() {
        return coreId;
    }
    public void setCoreId(String coreId) {
        this.coreId = coreId;
    }
    public String getWellId() {
        return wellId;
    }
    public void setWellId(String wellId) {
        this.wellId = wellId;
    }
}
