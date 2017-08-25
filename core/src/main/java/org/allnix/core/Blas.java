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

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class Blas {

  static void axpy(double a, double[] x, double[] y) {
    for (int i = 0; i < x.length; i++) {
      y[i] = a * x[i] + y[i];
    }
  }

  static void runXyz(int n, double[] x, double[] y, double[] z) {
    for (int i = 0; i < n; i++) {
      y[i] = x[i] / 13. + z[i] - 10.;
    }
  }

  static void runCondition(int n, double[] x, double[] y, double[] z) {
    for (int i = 0; i < n; i++) {
      if (z[i] < 1500.) {
        y[i] = x[i] / 13. + z[i] - 10.;
      } else {
        y[i] = x[i] / 13. + z[i] + 10.;
      }
    }
  }
}
