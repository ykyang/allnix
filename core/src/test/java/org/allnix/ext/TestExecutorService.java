/*
 * Copyright 2017 Yi-Kun Yang.
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
package org.allnix.ext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TestExecutorService {
  static final Logger logger = LoggerFactory.getLogger(TestExecutorService.class);
  
//  private ExecutorService es;
  
  @BeforeClass
  public void beforeClass() {
//    es = Executors.newCachedThreadPool();
  }
  
  @Test
  public void testCancel() {
    ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    
    AtomicBoolean value = new AtomicBoolean(false);
    
    Runnable task = ()->{
      while(true){
        value.set(false);
        if ( Thread.interrupted() ) {
          return;
        }
      }
    };
    
    Future<?> future = es.submit(task);

    Assert.assertFalse(value.get());
    value.set(true);
    try {
      TimeUnit.MILLISECONDS.sleep(1);
    } catch (InterruptedException ex) {
    }
    // set to false by the thread
    Assert.assertFalse(value.get());
    
    // > Stop the thread
    future.cancel(true);
    Assert.assertTrue(future.isCancelled());
    
    try {
      TimeUnit.MILLISECONDS.sleep(1);
    } catch (InterruptedException ex) {
    }

    // > Now the value is not chaged by the thread
    value.set(true);
    Assert.assertTrue(value.get());
    
    
//    future.cancel(true);
//    Assert.assertEquals(es.getActiveCount(), 0); 
    
//    
//    // > Test failed submission
//    try {
//      es.submit(task);
//      Assert.fail();
//    } catch(RejectedExecutionException e) {
//    }
//    
//    // > Cancel then try again; it should success this time
//    future.cancel(true);
//    try {
//      es.submit(task);
//    } catch(RejectedExecutionException e) {
//      Assert.fail();
//    }
//    
//    // > Clean up
//    future.cancel(true);
  }
}
