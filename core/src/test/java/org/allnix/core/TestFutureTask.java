/*
 * Copyright 2016 Yi-Kun Yang
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestFutureTask {

  static private final Logger logger = LoggerFactory.getLogger(
    TestFutureTask.class);
  private ExecutorService executor;
  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
    executor = Executors.newCachedThreadPool();
  }

  @Test(groups = {"short"})
  public void testExecute() {
    FutureTask<String> task = new FutureTask<String>(
      () -> {
        return "This is a test";
      }
    );

    executor.execute(task);

    try {
      String result = task.get();
      Assert.assertEquals(result, "This is a test");
    } catch (InterruptedException | ExecutionException ex) {
      throw new RuntimeException();
    }
  }

  @Test(groups = {"short"})
  public void testCancel() {
    FutureTask<String> task = new FutureTask<String>(
      () -> {
        try {
          TimeUnit.DAYS.sleep(1);
        } catch (InterruptedException ex) {
          // Does not reach here
          Assert.fail("Should not be here");
        }
        return "done";
      }
    );
    
    executor.execute(task);
    
    boolean success = task.cancel(true);

    Assert.assertTrue(success);
    Assert.assertTrue(task.isCancelled());
    Assert.assertTrue(task.isDone());
  }
}
