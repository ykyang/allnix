package org.allnix.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;

@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DriverTest {
    static private Logger logger = LoggerFactory.getLogger(DriverTest.class);
    
    private String host_port = "172.16.0.39:8017";
//    private int port = 8017;
    @Test
    public void insert() {
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
        MongoClient mc = new MongoClient(host_port);
        logger.info("Host: {}", mc.getAddress().getHost());
        
        MongoDatabase db = mc.getDatabase("techlog");
        logger.info("Database: {}", db.getName());
        
        MongoCollection<Document> coll = db.getCollection("log");
        
//        coll.createIndex(Indexes.hashed("name"));

        int n = 300_000;
        int start = 10_001;
        int end = 20_000;
        int docCount = end - start + 1;
        
//        coll.drop();
        
        ExecutorService es = Executors.newFixedThreadPool(10);
        
        List<Document> docs = new ArrayList<Document>();
        for (int i = start; i <= end; i++) {
            String name = "dct_" + Integer.toString(i);
            Double[] value = new Double[n];
            for ( int j = 0; j < n; j++) {
                value[j]= j+1+13.;
            }
            Document doc = new Document("name", name)
                    .append("value", Arrays.asList(value));
            
            docs.add(doc);
            if ( docs.size() >= 10) {
              watch.resume();
//              coll.insertOne(doc);
              coll.insertMany(docs);
              watch.suspend();
              
              docs.clear();
              logger.info("Inserted: {}", i-start+1);
            }
        }
        // - Last batch - //
        if (!docs.isEmpty()) {
            watch.resume();
            coll.insertMany(docs);
            watch.suspend();
            
            docs.clear();
            logger.info("Inserted: {}", docCount);
        }
        
        logger.info("Inser time: {}", watch.getTime());
        logger.info("Insert time per doc: {}", watch.getTime()/docCount);
        logger.info("Document count: {}", coll.count());
        
    
//        MongoCursor<Document> cursor = coll.find().iterator();
//        logger.info("Start all retrivel");
//        while(cursor.hasNext()) {
//            Document doc = cursor.next();
//            System.out.println(doc.getString("name"));
//            List list = doc.get("value", List.class);
//        }
//        logger.info("End all retrivel");
        
        logger.info("Start query");
        {
            Document doc = coll.find(Filters.eq("name", "dct_9999")).first();
            if (doc == null) {
                Assertions.fail("doc is null");
            }
            List list = doc.get("value", List.class);
            Assertions.assertEquals(n, list.size());
            
        }
        logger.info("End query");
    }
    
    
}
