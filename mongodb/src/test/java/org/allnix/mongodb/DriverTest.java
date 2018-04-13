package org.allnix.mongodb;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.types.Binary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DriverTest {
    static private Logger logger = LoggerFactory.getLogger(DriverTest.class);
    @Test
    public void dropAll() {
        MongoClient mc = new MongoClient(host_port);
        MongoDatabase db = mc.getDatabase("techlog_double");
        db.drop();
        db = mc.getDatabase("techlog");
        db.drop();
        db = mc.getDatabase("techlog_byte");
        db.drop();
        db = mc.getDatabase("techlog_manybyte");
        db.drop();
        db = mc.getDatabase("techlog_gridfs");
        db.drop();
    }
    private String host_port = "127.0.0.1:8017";
//    private int port = 8017;
    @Test
    public void insertDouble() {
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
    	MongoClient mc = new MongoClient(host_port);
        logger.info("Host: {}", mc.getAddress().getHost());
        
        MongoDatabase db = mc.getDatabase("techlog_double");
        logger.info("Database: {}", db.getName());
        
        MongoCollection<Document> coll = db.getCollection("log");
        
//        int n = 300_000;
        int n = 300_000;
        int start = 1;
        int end = 1_000;
//      int start = 10_001;
//      int end = 20_000;
        int docCount = end - start + 1;

        int query = (end+start)/2;
        
        coll.drop();
        coll.createIndex(Indexes.hashed("name"));

        // ExecutorService es = Executors.newFixedThreadPool(10);


        Double[] value = new Double[n];
        for (int j = 0; j < n; j++) {
            value[j] = j + 1 + 13.;
        }

        List<Document> docs = new ArrayList<Document>();
        for (int i = start; i <= end; i++) {
            String name = "dct_" + Integer.toString(i);
            // byte[] value = new byte[n];
            // Double[] value = new Double[n];
            // for ( int j = 0; j < n; j++) {
            // value[j]= j+1+13.;
            // }

            Document doc = new Document("name", name).append("value",
                    Arrays.asList(value));

            docs.add(doc);
            if (docs.size() >= 10) {
                watch.resume();
                // coll.insertOne(doc);
                coll.insertMany(docs);
                watch.suspend();

                docs.clear();
                logger.info("Inserted: {}", i - start + 1);
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
        logger.info("Insert time per doc: {}", watch.getTime() / docCount);
        logger.info("Document count: {}", coll.count());

        // MongoCursor<Document> cursor = coll.find().iterator();
        // logger.info("Start all retrivel");
        // while(cursor.hasNext()) {
        // Document doc = cursor.next();
        // System.out.println(doc.getString("name"));
        // List list = doc.get("value", List.class);
        // }
        // logger.info("End all retrivel");

        logger.info("Start query");
        {
            Document doc = coll.find(Filters.eq("name", "dct_" + Integer.toString(query))).first();
            if (doc == null) {
                Assertions.fail("doc is null");
            }
            List<Double> list = (List<Double>) doc.get("value", List.class);
            logger.info("length: {}", list.size());
            Assertions.assertEquals((Double)(n + 13.), list.get(n - 1));
        }
        logger.info("End query");
    }
    
    @Test
    public void insertManyByte() {
        // - Separate journal/ from db/ - //
        // 15:13:07.056 [Test worker] INFO org.allnix.mongodb.DriverTest - Inser
        // time: 85889
        // 15:13:07.056 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Insert time per doc: 8.5889
        // 15:13:07.150 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Document count: 10000
        // 15:13:07.150 [Test worker] INFO org.allnix.mongodb.DriverTest - Start
        // query
        // 15:13:07.457 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // length: 2400000
        // 15:13:07.680 [Test worker] INFO org.allnix.mongodb.DriverTest - End
        // query
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
        MongoClient mc = new MongoClient(host_port);
        logger.info("Host: {}", mc.getAddress().getHost());
        
        MongoDatabase db = mc.getDatabase("techlog_manybyte");
        logger.info("Database: {}", db.getName());
        
        MongoCollection<Document> coll = db.getCollection("log");
        
        

        int n = 300_000;
//        int n = 300;
        int start = 1;
        int end = 10000;  // 1.2 GB instead of 2.2 GB as calculated
//        int start = 10_001;
//        int end = 20_000;
        int docCount = end - start + 1;

        int query = (end+start)/2;
        
        coll.drop();
        coll.createIndex(Indexes.hashed("name"));
        
//        ExecutorService es = Executors.newFixedThreadPool(10);
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES*n);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        
        Double[] value = new Double[n];
        for ( int j = 0; j < n; j++) {
            double v = j+1+13.;
            value[j]= j+1+13.;
//            bb.putDouble(j, value[j]);
            doubleBuffer.put(j, v);
        }
//        byte[] b = SerializationUtils.serialize(value);
//        logger.info("length: {}", b.length);
//        logger.info("bb length: {}", bb.capacity());
        
        List<Document> docs = new ArrayList<Document>();
        for (int i = start; i <= end; i++) {
            String name = "dct_" + Integer.toString(i);
//            byte[] value = new byte[n];
//            Double[] value = new Double[n];
//            for ( int j = 0; j < n; j++) {
//                value[j]= j+1+13.;
//            }
            
            Document doc = new Document("name", name)
                    .append("value", byteBuffer.array());
            
            docs.add(doc);
            if ( docs.size() >= 10) {
              watch.resume();
//              coll.insertOne(doc);
              coll.insertMany(docs);
              watch.suspend();
              
//              for (Document d : docs) {
//                  logger.info("ID: {}", d.getObjectId("_id"));
//              }
              docs.clear();
              logger.info("Inserted: {}", i-start+1);
            }
        }
        // - Last batch - //
        if (!docs.isEmpty()) {
            watch.resume();
            coll.insertMany(docs);
            watch.suspend();
//            for (Document d : docs) {
//                logger.info("ID: {}", d.getObjectId("_id"));
//            }
            docs.clear();
            logger.info("Inserted: {}", docCount);
        }
        
        logger.info("Inser time: {}", watch.getTime());
        logger.info("Insert time per doc: {}", watch.getTime()/(double)docCount);
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
            Document doc = coll.find(Filters.eq("name", 
                    "dct_" + Integer.toString(query))).first();
            if (doc == null) {
                Assertions.fail("doc is null");
            }
            byte[] c = new byte[1];
            Binary list = doc.get("value", Binary.class);
            logger.info("length: {}", list.getData().length);
            ByteBuffer bf = ByteBuffer.wrap(list.getData());
            DoubleBuffer dB = bf.asDoubleBuffer();
            for (int j = 0; j < n; j++) {
                double v = j+1+13;
                Assertions.assertEquals(v, dB.get(j),"Ind = " + Integer.toString(j));
            }
//            Assertions.assertEquals((double)(n+13.), bf.getDouble(n-1));
//            Assertions.assertEquals(n, list.length()/8);
//            Double[] v = SerializationUtils.deserialize(list.getData());
//            logger.info("v length: {}", v.length);
//            logger.info("v[n-1]: {}", v[n-1]);
//            Assertions.assertEquals((Double)(n+13.), v[n-1]);
        }
        logger.info("End query");
    }
    
    @Test
    public void insertByte() {
        // 15:31:00.433 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Insert time: 162547
        // 15:31:00.433 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Insert time per doc: 16.2547
        // 15:31:00.435 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Document count: 10000
        // 15:31:00.436 [Test worker] INFO org.allnix.mongodb.DriverTest - Start
        // query
        // 15:31:00.521 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // length: 2400000
        // 15:31:00.584 [Test worker] INFO org.allnix.mongodb.DriverTest - End
        // query
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
        MongoClient mc = new MongoClient(host_port);
        logger.info("Host: {}", mc.getAddress().getHost());
        
        MongoDatabase db = mc.getDatabase("techlog_byte");
        logger.info("Database: {}", db.getName());
        
        MongoCollection<Document> coll = db.getCollection("log");

        int n = 300_000;
//        int n = 300;
        
        int start = 1;
        int end = 10000; // 1.2 GB instead of 2.2 GB as calculated
        
        int query = (end+start)/2;

        //        int start = 10_001;
//        int end = 20_000;
        int docCount = end - start + 1;
        
        coll.drop();
        coll.createIndex(Indexes.hashed("name"));
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES*n);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        
        for ( int j = 0; j < n; j++) {
            double v = j+1+13.;
            
            // - Do not delete
            // - Pay attention to the index
            // byteBuffer.putDouble(j*Double.BYTES, v);
            
            doubleBuffer.put(j, v);
        }
        
        for (int i = start; i <= end; i++) {
            String name = "dct_" + Integer.toString(i);
            
            Document doc = new Document("name", name)
                    .append("value", byteBuffer.array());
            
            watch.resume();
            coll.insertOne(doc); // 7 ms per insert
            watch.suspend();
            
            logger.info("Document Ind: {}", i);
            // - Do NOT delete.  This is how you get ObjectId. - //
            // logger.info("ID: {}", doc.getObjectId("_id"));
        }
        logger.info("Insert time: {}", watch.getTime());
        logger.info("Insert time per doc: {}", watch.getTime()/(double)docCount);
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
            Document doc = coll.find(Filters.eq("name", 
                    "dct_" + Integer.toString(query))).first();
            if (doc == null) {
                Assertions.fail("doc is null");
            }
            Binary bytes = doc.get("value", Binary.class);
            
            logger.info("length: {}", bytes.getData().length);
            Assertions.assertEquals(n*Double.BYTES, bytes.getData().length);
            
            ByteBuffer bf = ByteBuffer.wrap(bytes.getData());
            DoubleBuffer dB = bf.asDoubleBuffer();
            for (int j = 0; j < n; j++) {
                double v = j+1+13;
                Assertions.assertEquals(v, dB.get(j),"Ind = " + Integer.toString(j));
            }
            
        }
        logger.info("End query");
    }
    
    @Test
    public void insertGridFS() throws IOException {
        // 15:25:40.392 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Insert time: 195960
        // 15:25:40.392 [Test worker] INFO org.allnix.mongodb.DriverTest -
        // Insert time per doc: 19.596
        // 15:25:40.392 [Test worker] INFO org.allnix.mongodb.DriverTest - Start
        // query
        // 15:25:40.442 [Test worker] INFO org.allnix.mongodb.DriverTest - End
        // query

        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
        MongoClient mc = new MongoClient(host_port);
        logger.info("Host: {}", mc.getAddress().getHost());
        
        DB db = mc.getDB("techlog_gridfs");
        MongoDatabase mongodb = mc.getDatabase("techlog_gridfs");
        GridFSBucket bucket = GridFSBuckets.create(mongodb, "log");
        bucket.drop();
        
        MongoCollection<Document> coll = mongodb.getCollection("log.files");
        coll.createIndex(Indexes.hashed("name"));
//        logger.info("Database: {}", db.getName());
        
        GridFS gridfs = new GridFS(db, "log");
       
       
        
        int n = 300_000;
//      int n = 300;
      
      int start = 1;
      int end = 10000; //  GB instead of 2.2 GB as calculated
      
      int query = (end+start)/2;

      //        int start = 10_001;
//      int end = 20_000;
      int docCount = end - start + 1;
      
//      db.drop();
//      coll.createIndex(Indexes.hashed("name"));
      
      
      
      ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES*n);
      DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
      
      for ( int j = 0; j < n; j++) {
          double v = j+1+13.;
          
          // - Do not delete
          // - Pay attention to the index
          // byteBuffer.putDouble(j*Double.BYTES, v);
          
          doubleBuffer.put(j, v);
      }
        
        
      for (int i = start; i <= end; i++) {
          String name = "dct_" + Integer.toString(i);
          
          Document doc = new Document("name", name);
          
          watch.resume();
          GridFSInputFile in = gridfs.createFile(byteBuffer.array());
//          in.setChunkSize(255);
          in.put("name", name);
          in.save(); // 16-20 ms per insert
          watch.suspend();
          
          logger.info("Document Ind: {}", i);
          // - Do NOT delete.  This is how you get ObjectId. - //
          // logger.info("ID: {}", doc.getObjectId("_id"));
      }
//      GridFSInputFile in = gridfs.createFile(byteBuffer.array());
//      in.save();
      logger.info("Insert time: {}", watch.getTime());
      logger.info("Insert time per doc: {}", watch.getTime()/(double)docCount);
      
      logger.info("Start query");
      {
          BasicDBObject dbo = new BasicDBObject("name", "dct_" + Integer.toString(query));
          GridFSDBFile out = gridfs.findOne(dbo);
          if (out == null) {
              Assertions.fail("doc is null");
          }
          
          ByteArrayOutputStream ob = new ByteArrayOutputStream();
          out.writeTo(ob);
//          Binary bytes = doc.get("value", Binary.class);
          
//          logger.info("length: {}", bytes.getData().length);
          Assertions.assertEquals(n*Double.BYTES, ob.size());
          
          ByteBuffer bf = ByteBuffer.wrap(ob.toByteArray());
          DoubleBuffer dB = bf.asDoubleBuffer();
          for (int j = 0; j < n; j++) {
              double v = j+1+13;
              Assertions.assertEquals(v, dB.get(j),"Ind = " + Integer.toString(j));
          }
          
      }
      logger.info("End query");
 
    }
    
    @Test
    public void insertBigGridFS() throws IOException {
        StopWatch watch = StopWatch.createStarted(); 
        watch.suspend();
        
        MongoClient mc = new MongoClient(host_port);
        logger.info("Host: {}", mc.getAddress().getHost());
        
        DB db = mc.getDB("techlog_big_gridfs");
        MongoDatabase mongodb = mc.getDatabase("techlog_big_gridfs");
        GridFSBucket bucket = GridFSBuckets.create(mongodb, "log");
        bucket.drop();
        
        MongoCollection<Document> coll = mongodb.getCollection("log.files");
        coll.createIndex(Indexes.hashed("name"));
//        logger.info("Database: {}", db.getName());
        
        GridFS gridfs = new GridFS(db, "log");
       
       
        
        int n = 100_000_000;
//      int n = 300;
      
      int start = 1;
      int end = 30; //  GB instead of 2.2 GB as calculated
      
      int query = (end+start)/2;

      //        int start = 10_001;
//      int end = 20_000;
      int docCount = end - start + 1;
      
//      db.drop();
//      coll.createIndex(Indexes.hashed("name"));
      
      
      
      ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES*n);
      DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
      
      for ( int j = 0; j < n; j++) {
          double v = j+1+13.;
          
          // - Do not delete
          // - Pay attention to the index
          // byteBuffer.putDouble(j*Double.BYTES, v);
          
          doubleBuffer.put(j, v);
      }
        
        
      for (int i = start; i <= end; i++) {
          String name = "dct_" + Integer.toString(i);
          
          Document doc = new Document("name", name);
          
          watch.resume();
          GridFSInputFile in = gridfs.createFile(byteBuffer.array());
          in.setChunkSize(1024*1024*1 - 1);
          in.put("name", name);
          in.save(); // 16-20 ms per insert
          watch.suspend();
          
          logger.info("Document Ind: {}", i);
          // - Do NOT delete.  This is how you get ObjectId. - //
          // logger.info("ID: {}", doc.getObjectId("_id"));
      }
//      GridFSInputFile in = gridfs.createFile(byteBuffer.array());
//      in.save();
      logger.info("Insert time: {}", watch.getTime());
      logger.info("Insert time per doc: {}", watch.getTime()/(double)docCount);
      
      logger.info("Start query");
      {
          BasicDBObject dbo = new BasicDBObject("name", "dct_" + Integer.toString(query));
          GridFSDBFile out = gridfs.findOne(dbo);
          if (out == null) {
              Assertions.fail("doc is null");
          }
          
          ByteArrayOutputStream ob = new ByteArrayOutputStream();
          out.writeTo(ob);
//          Binary bytes = doc.get("value", Binary.class);
          
//          logger.info("length: {}", bytes.getData().length);
          Assertions.assertEquals(n*Double.BYTES, ob.size());
          
          ByteBuffer bf = ByteBuffer.wrap(ob.toByteArray());
          DoubleBuffer dB = bf.asDoubleBuffer();
          for (int j = 0; j < n; j++) {
              double v = j+1+13;
              Assertions.assertEquals(v, dB.get(j),"Ind = " + Integer.toString(j));
          }
          
      }
      logger.info("End query");
 
    }
    @Test
    public void writeFile() throws IOException {
        StopWatch watch = StopWatch.createStarted(); 
        
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("big.out"));
        for ( long i = 0; i < 3_000_000_000l*8; i++) {
            out.write(13);
        }
        out.close();
        watch.suspend();
        
        logger.info("Time: {}", watch.getTime());
    }
}
