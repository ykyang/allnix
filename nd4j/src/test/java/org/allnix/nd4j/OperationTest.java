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

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.BlasWrapper;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.nd4j.linalg.ops.transforms.Transforms;
//import static org.nd4j.linalg.ops.transforms.Transforms.*;
//import static org.nd4j.linalg.ops.transforms.Transforms.pow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class OperationTest {
    static final Logger logger = LoggerFactory.getLogger(OperationTest.class);
    
    private int length = 10;
    private double[] data;
    private double[] data2;
    @BeforeAll
    public void beforeAll() {
        data = new double[length];
        data2 = new double[length];
        
        Random rand = new Random(12344321);
        for (int i = 0; i < length; i++) {
            data[i] = rand.nextDouble();
        }
        for (int i = 0; i < length; i++) {
            data2[i] = rand.nextDouble();
        }
        logger.info("Data: {}", Arrays.toString(data));
        logger.info("Data 2: {}", Arrays.toString(data2));
    }
    
    @Test
    @Tag("milliseconds")
    public void testPow() {
        StopWatch watch = null;
        INDArray nd = Nd4j.create(data);
        watch = StopWatch.createStarted();
        INDArray ans = Transforms.pow(nd, 2.);
        watch.stop();
        Assertions.assertNotSame(nd, ans);
        logger.info("POW copied: {} msec", watch.getTime(TimeUnit.MILLISECONDS));
        
        watch = StopWatch.createStarted();
        ans = Transforms.pow(nd, 2, false);
        watch.stop();
        Assertions.assertSame(nd, ans);
        logger.info("POW in-place: {} msec", watch.getTime(TimeUnit.MILLISECONDS));
    }
    @Test
    @Tag("milliseconds")
    public void testMul() {
        INDArray nd = Nd4j.create(data);
        INDArray ans = nd.mul(nd);
        for ( int i = 0; i < length; i++) {
            double value = nd.getDouble(i);
            value *= value;
            Assertions.assertEquals(value, ans.getDouble(i), value/1_000_000.);
        }
    }
    @Test
    @Tag("milliseconds")
    public void testBlasAxpy() {
        BlasWrapper blas = Nd4j.getBlasWrapper();
        INDArray x = Nd4j.create(data);
        INDArray y = Nd4j.create(data2);
        blas.axpy(new Double(7.0), x, y);
        for ( int i = 0; i < length; i++) {
            double value = 7*data[i] + data2[i];
            Assertions.assertEquals(value, y.getDouble(i), value/1_000_000.);
        }
        
    }
}
