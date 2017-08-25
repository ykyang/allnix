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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@Test(singleThreaded = true)
public class TestBlas {
  static private Logger logger = LoggerFactory.getLogger(TestBlas.class);
  @Test(invocationCount = 1)
  public void testAxpy() {
    int n = 1000000;
    int count = 1000;
    double a = 0.5;
    double[] x = new double[n];
    double[] y = new double[n];
    
    for ( int i = 0; i < n; i++) {
      x[i] = i;
      y[i] = 1.0;
    }
    
    long startTime = System.nanoTime();
    for ( int i = 0; i < count; i++) {
      Blas.axpy(a, x, y);
    }
    long endTime = System.nanoTime();

    double duration = (endTime - startTime) / 1.e9; // seconds
    logger.info("testAxpy time: {}, y = {}", duration, y[n-1]);
  }
  
  @Test(invocationCount = 1)
  public void testXyz() {
    int n = 10000000;
    int count = 100;
    double a = 0.5;
    double[] x = new double[n];
    double[] y = new double[n];
    double[] z = new double[n];
    for (int i = 0; i < n; i++) {
      x[i] = i;
      y[i] = 0.0;
      z[i] = 1.5 * (i+2);
    }
    long startTime = System.nanoTime();
    for ( int i = 0; i < count; i++) {
      Blas.runXyz(n, x, y, z);
    }
    long endTime = System.nanoTime();

    double duration = (endTime - startTime) / 1.e9; // seconds
    logger.info("testXyz time: {}, y = {}", duration, y[n-1]);
  }
  
  @Test(invocationCount = 1)
  public void testCondition() {
    int n = 10000000;
    int count = 100;
    double a = 0.5;
    double[] x = new double[n];
    double[] y = new double[n];
    double[] z = new double[n];
    for (int i = 0; i < n; i++) {
      x[i] = i;
      y[i] = 0.0;
      z[i] = 1.5 * (i+2);
    }
    long startTime = System.nanoTime();
    for ( int i = 0; i < count; i++) {
      Blas.runCondition(n, x, y, z);
    }
    long endTime = System.nanoTime();

    double duration = (endTime - startTime) / 1.e9; // seconds
    logger.info("testCondition time: {}, y = {}", duration, y[n-1]);
  }
}
