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
package org.allnix.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestProgressMonitor {

  static private final Logger logger = LoggerFactory.getLogger(
    TestProgressMonitor.class);

  @Test(groups = {"short"})
  public void testSimpleProgressMonitor() {
    final SimpleProgressMonitor monitor = new SimpleProgressMonitor();
    final int threadCount = 10;
    final int taskPerThreadCount = 1000;
    final int taskCount = threadCount * taskPerThreadCount;

    Set<String> uuidSet = Collections.synchronizedSet(new HashSet<>());

    ExecutorService executor = Executors.newCachedThreadPool();

    for (int i = 0; i < threadCount; i++) {
      Runnable thread = () -> {
        for (int j = 0; j < taskPerThreadCount; j++) {
          String uuid = UUID.randomUUID().toString();
          // > Task name == UUID
          monitor.subTask(uuid);
          monitor.worked(1);
          uuidSet.add(uuid);
        }
      };

      executor.submit(thread);
    }

    // > Wait for all threads to finish
    try {
      executor.shutdown();
      executor.awaitTermination(1, TimeUnit.DAYS);
    } catch (InterruptedException ex) {
      logger.error("", ex);
    }

    // > Check work count
    Assert.assertEquals(monitor.getWorked(), taskCount);
    // > Check task name count
    Assert.assertEquals(monitor.getSubTaskList().size(), taskCount);
    Assert.assertEquals(monitor.getSubTaskList().size(), uuidSet.size());
    // > Check task name are not scrambbled
    for (int i = 0; i < taskCount; i++) {
      String uuid = monitor.getSubTaskList().get(i);
      Assert.assertTrue(uuidSet.contains(uuid));
    }
  }

  private long cancelCount;
  
  @Test(groups = {"short"})
  public void testCancelListener() {
    SimpleProgressMonitor monitor = new SimpleProgressMonitor();

    cancelCount = 0;
    
    monitor.addCancelListener(() -> {
      throw new RuntimeException();
    });
    
    monitor.addCancelListener(() -> {
      cancelCount++;
    });
    
    monitor.addCancelListener(() -> {
      cancelCount++;
    });

    // > No exception allowed
    monitor.cancel();
    
    Assert.assertEquals(cancelCount, 2);
  }
}
