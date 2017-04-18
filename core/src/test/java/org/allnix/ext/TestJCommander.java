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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test command line argument parsing with JCommander
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestJCommander {

  static private final Logger logger = LoggerFactory.getLogger(TestJCommander.class);

  @BeforeClass
  public void beforeClass() {
    logger.debug("beforeClass");
  }

  @Test(groups = {"short", "unix", "win"})
  public void test1() {
    String[] args = {
      "-port", "12345"
    };
    Argument argument = new Argument();
    
    JCommander jc = new JCommander();
    jc.addObject(argument);
    jc.parse(args);
    
    Assert.assertEquals(12345, argument.getPort());
  }

  static class Argument {

    @Parameter(names = {"-port", "--port"}, description = "Server port number")
    private int port;

    public int getPort() {
      return port;
    }
  }
}
