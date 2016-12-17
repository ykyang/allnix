/*
 * Copyright 2016 Yi-Kun Yang.
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
package org.allnix.restjson.web;

import org.allnix.core.JsonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@RestController
@RequestMapping(value="/json")
public class JsonController {
  static private Logger logger = LoggerFactory.getLogger(JsonController.class);
  
  private JsonDao dao;

  public JsonController() {
    
  }
  
  @Autowired
  public void setJsonDao(JsonDao dao) {
    logger.info("Set JsonDao");
    this.dao = dao;
  }

  @PostMapping(value="/{table}/{id}")
  public boolean create(@PathVariable("table") String tableName, @PathVariable String id,
    @RequestBody String json) {
    return dao.create(tableName, id, json);
  }

  
  @GetMapping(value="/{table}/{id}")
  public String read(@PathVariable("table") String tableName, @PathVariable String id) {
//    return table + ": " + id;
     String value = dao.read(tableName, id);
     if ( value == null ) {
       throw new JsonNotFoundException(tableName, id);
     } else {
       return value;
     }
  }

  @PutMapping(value="/{table}/{id}")  
  public boolean update(@PathVariable("table") String tableName, 
    @PathVariable String id, @RequestBody String json) {
    
    boolean value = dao.update(tableName, id, json);
    
    if ( !value ) {
      throw new JsonNotFoundException(tableName, id); 
    } else {
      return true;
    }
  }
}
