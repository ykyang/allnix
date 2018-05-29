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
package org.allnix.oil.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass // Use flat table (denormalized) to save this info
public abstract class OilObject {
    @org.springframework.data.annotation.Id
    @javax.persistence.Id
    @Column(length=36)
    private String id = UUID.randomUUID().toString();
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
