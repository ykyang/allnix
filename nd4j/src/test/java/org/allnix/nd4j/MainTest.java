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

import java.util.Random;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MainTest {
    @Test
    @Tag("seconds")
    public void test() {
        Random rand = new Random();
        int length = 1_000_000;
        double[] d1 = new double[length];
        double[] d2 = new double[length];

        for (int i = 0; i < length; i++) {
            d1[i] = rand.nextDouble();
            d2[i] = rand.nextDouble();
        }

        INDArray nd1 = Nd4j.create(d1, new int[] { 1, length });
        INDArray nd2 = Nd4j.create(d2, new int[] { length, 1 });
        long total = 0;
        for (int i = 0; i < 1000; i++) {
            long t1 = timeNow();

            INDArray nd3 = nd1.mmul(nd2);

            long t2 = timeNow();
            total += t2-t1;
        }
//        print(nd3);
        long t3 = timeNow();
        double d = 0;
        for (int i = 0; i < length; i++) {
            d += d1[i] * d2[i];
        }

        long t4 = timeNow();
        print(d);
        print("ND4J cost time: " + total/1000.);
        print("For Loop cost time: " + Long.toString(t4 - t3));

    }

    private static void print(Object o) {
        System.out.println(o);
    }

    private static long timeNow() {
        return System.currentTimeMillis();
    }
}
