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
package org.allnix.mongodb;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

import org.allnix.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SpringTest {
    static private Logger logger = LoggerFactory.getLogger(SpringTest.class);
    
//    @Qualifier("person")
//    @Autowired
    private MongoTemplate personT = null;
    
    private AnnotationConfigApplicationContext ctx;
    
    @BeforeAll
    public void beforeAll() {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        
        personT = ctx.getBean("person", MongoTemplate.class);
    }
    
    @Test
    public void crud() {
        MongoOperations ops = personT;
        
        ops.dropCollection(Person.class);
        
        Person p = new Person("Joe", 37);
        
        ops.insert(p);
        
        String id = p.getId();
        // Find
        Person p2 = ops.findById(id, Person.class);
        
        Assertions.assertFalse(p.equals(p2));
        Assertions.assertEquals(p.getId(), p2.getId());
        
        // Update
        ops.updateFirst(query(where("name").is("Joe")), update("age", 39), Person.class);
        p = ops.findOne(query(where("name").is("Joe")), Person.class);
        Assertions.assertEquals(39, p.getAge());
        
//        // Delete
//        ops.remove(p);
//        // Check that deletion worked
//        List<Person> people =  ops.findAll(Person.class);
//        Assertions.assertTrue(people.isEmpty());
    }
}
