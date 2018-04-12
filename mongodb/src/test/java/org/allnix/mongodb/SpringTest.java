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

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

import org.allnix.model.Person;
import org.allnix.model.Variable;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SpringTest {
    static private Logger logger = LoggerFactory.getLogger(SpringTest.class);
    
//    @Qualifier("person")
//    @Autowired
    private MongoTemplate personT = null;
    private MongoTemplate techlogTemplate = null;
    private MongoClient mongoClient = null;
    private AnnotationConfigApplicationContext ctx;
    
    @BeforeAll
    public void beforeAll() {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        
        personT = ctx.getBean("person", MongoTemplate.class);
        techlogTemplate = ctx.getBean("TechlogDb", MongoTemplate.class);
        mongoClient = ctx.getBean(MongoClient.class);
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
    @Test
    public void index() {
        
    }
    @Test
    public void techlog() {
        // 16:11:07.174 [Test worker] INFO org.allnix.mongodb.SpringTest -
        // Insert time: 137640
        // 16:11:07.174 [Test worker] INFO org.allnix.mongodb.SpringTest -
        // Insert time per doc: 13.764
        // 16:11:07.228 [Test worker] INFO  org.allnix.mongodb.SpringTest - 
        // Query time: 54
        MongoOperations ops = techlogTemplate;
       
        ops.dropCollection(Variable.class);
        
        MongoDatabase db = mongoClient.getDatabase("TechlogDb");
        MongoCollection<Document> coll = db.getCollection("variable");
        coll.createIndex(Indexes.hashed("name"));
//        ops.indexOps(Variable.class).ensureIndex(new Index().named("name"));
        
        int n = 300_000;
        int start = 1;
        int end = 10000;
//        int  = (end+start)/2;
        String var_name = "dct_" + Integer.toString((end+start)/2);
        int docCount = end - start + 1;
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES*n);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        
        for ( int j = 0; j < n; j++) {
            double v = j+1+13.;
            // - Do not delete
            // - Pay attention to the index
            // byteBuffer.putDouble(j*Double.BYTES, v);
            doubleBuffer.put(j, v);
        }
        
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
        for (int i = start; i <= end; i++) {
            String name = "dct_" + Integer.toString(i);
            Variable v = new Variable();
            v.setArray(byteBuffer.array());
            v.setName(name);
            v.setWellName("Woods");
            
            watch.resume();
            ops.insert(v);
            watch.suspend();
            
            logger.info("Document Ind: {}", i);
        }
        
        logger.info("Insert time: {}", watch.getTime());
        logger.info("Insert time per doc: {}", watch.getTime()/(double)docCount);
        
        watch.reset();
        watch.start();
        Variable var = ops.findOne(query(where("name").is(var_name)), Variable.class);
        watch.suspend();
        logger.info("Query time: {}", watch.getTime());
        {
            ByteBuffer bf = ByteBuffer.wrap(var.getArray());
            DoubleBuffer dB = bf.asDoubleBuffer();
            for (int j = 0; j < n; j++) {
                double v = j+1+13;
                Assertions.assertEquals(v, dB.get(j),"Ind = " + Integer.toString(j));
            }
        }
        
    }
}
