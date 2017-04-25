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

import java.io.InputStream;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJython {

  static private final Logger logger = LoggerFactory.getLogger(TestJython.class);

  @Test
  public void test() {
    PythonInterpreter interp;
    interp = new PythonInterpreter();

    interp.exec("import sys");
//    interp.exec("print sys");
    interp.set("a", new PyInteger(42));
//    interp.exec("print a");
    PyObject obj;
    obj = interp.get("a");
    
    Assert.assertEquals(obj.asInt(),42);
  }
  
  @Test
  public void testFile() {
    InputStream in = this.getClass().getClassLoader()
        .getResourceAsStream("assumption.py");
    
    PythonInterpreter interp;
    interp = new PythonInterpreter();
    
    interp.execfile(in);
    
    PyObject obj;
    obj = interp.get("a");
    
    Assert.assertEquals(obj.asInt(),43);
  }
}
