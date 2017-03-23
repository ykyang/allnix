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
package org.allnix.core;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TestThread {

  private ExecutorService es;

  @BeforeClass
  public void beforeClass() {
    es = Executors.newCachedThreadPool();
  }

  /**
   * Test the wrapping of exception in thread
   */
  @Test
  public void testException() {
    Runnable task = () -> {
      try {
        throw new IOException();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    };

    CompletableFuture<Void> future = CompletableFuture.runAsync(task, es);
    try {
      future.join();
      Assert.fail();
    } catch (CompletionException e) {
      Throwable throwable = e.getCause();
      Assert.assertTrue(throwable instanceof UncheckedIOException);
      
      throwable = throwable.getCause();
      Assert.assertTrue(throwable instanceof IOException);
    }
  }

}
