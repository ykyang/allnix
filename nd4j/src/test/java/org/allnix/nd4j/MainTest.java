/*
 * Copyright 2018 Yi-Kun Yang.
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
package org.allnix.nd4j;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class MainTest {
    static final private Logger logger = LoggerFactory.getLogger(MainTest.class);
    
    private int length = 1_000_000;
    private int iteration = 10000;
    
    private double[] d1;
    private double[] d2;
    
    private DecimalFormat df;
    
    @BeforeAll
    public void beforeAll() {
        Random rand = new Random();
        
        d1 = new double[length];
        d2 = new double[length];

        for (int i = 0; i < length; i++) {
            d1[i] = rand.nextDouble();
            d2[i] = rand.nextDouble();
            
//            d1[i] = i % 9 * 100./length;
//            d2[i] = i % 13 * 200./length;
        }
        
        df = new DecimalFormat();
        df.setMinimumFractionDigits(16);
    }
    /**
     * Add 
     * 
     * Add Python's MKL so files to LD_LIBRARY_PATH then this is as
     * fast as Numpy.
     */
    @Test
    @Tag("seconds")
    public void testNd4j() {
        INDArray nd1 = Nd4j.create(d1, new int[] { 1, length });
        INDArray nd2 = Nd4j.create(d2, new int[] { length, 1 });
        INDArray nd3 = null;
        
        StopWatch nd4jWatch = StopWatch.createStarted();
        for (int i = 0; i < iteration; i++) {
            nd3 = nd1.mmul(nd2);
        }
        nd4jWatch.stop();
        logger.info("ND4J total   time: {} sec", nd4jWatch.getTime(TimeUnit.SECONDS));
        logger.info("ND4J average time: {} msec", (double)nd4jWatch.getTime(TimeUnit.MILLISECONDS)/iteration);
        logger.info("ND4J value: {}", df.format(nd3.getDouble(0)));
    }
    
    @Test
    @Tag("seconds")
    public void testForLoop() {
        StopWatch javaWatch = StopWatch.createStarted();
        double d = 0.;
        for (int j = 0; j < iteration; j++) {
            d = 0;
            for (int i = 0; i < length; i++) {
                d += d1[i] * d2[i];
            }
        }
        javaWatch.stop();
        logger.info("for-loop total   time: {} sec", javaWatch.getTime(TimeUnit.SECONDS));
        logger.info("for-loop average time: {} msec", (double)javaWatch.getTime(TimeUnit.MILLISECONDS)/iteration);
        logger.info("for-loop value: {}", df.format(d));
    }
}
