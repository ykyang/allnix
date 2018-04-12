/*
 * Copyright 2017-2018 Yi-Kun Yang.
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
package org.allnix.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TypeAlias("Variable")
public class Variable {
    @Id
    private String id;
    private String name;
    private String familyName;
    private String wellName;
    private byte[] array;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    public String getWellName() {
        return wellName;
    }
    public void setWellName(String wellName) {
        this.wellName = wellName;
    }
    public byte[] getArray() {
        return array;
    }
    public void setArray(byte[] array) {
        this.array = array;
    }
    public String getId() {
        return id;
    }
    
}
