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
package org.allnix.oil.lab.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.allnix.oil.model.OilObject;

@Entity
@Table(indexes = { //
    @Index(columnList = "wellId") //
    , @Index(columnList = "parentId")
})
public class Core extends OilObject {
    
    @Column(length=36)
    private String wellId;
    
	private String name;
	private Double topCoreDepth;
	private Double bottomCoreDepth;
	private Double topMd;
    private Double bottomMd;
    
	public String getWellId() {
        return wellId;
    }
    public void setWellId(String wellId) {
        this.wellId = wellId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getTopCoreDepth() {
        return topCoreDepth;
    }
    public void setTopCoreDepth(Double topCoreDepth) {
        this.topCoreDepth = topCoreDepth;
    }
    public Double getBottomCoreDepth() {
        return bottomCoreDepth;
    }
    public void setBottomCoreDepth(Double bottomCoreDepth) {
        this.bottomCoreDepth = bottomCoreDepth;
    }
    public Double getTopMd() {
        return topMd;
    }
    public void setTopMd(Double topMd) {
        this.topMd = topMd;
    }
    public Double getBottomMd() {
        return bottomMd;
    }
    public void setBottomMd(Double bottomMd) {
        this.bottomMd = bottomMd;
    }
}
