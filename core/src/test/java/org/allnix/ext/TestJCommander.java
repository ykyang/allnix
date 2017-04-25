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
import com.beust.jcommander.Parameters;
import java.util.ArrayList;
import java.util.List;
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

  /**
   * Test
   * <pre>
   * -port 12345
   * </pre>
   */
  @Test(groups = {"short"})
  public void testDefaultOverride() {
    String[] args = {
      "-port", "12345",
      "-Dorg.allnix=false"
    };
    Argument argument = new Argument();
    JavaProperty prop = new JavaProperty();
    
    JCommander jc = new JCommander();
    jc.addObject(argument);
    jc.addObject(prop);
    jc.parse(args);
    
    Assert.assertEquals(12345, argument.getPort());
    Assert.assertFalse(prop.isAllnix());
  }
  
  @Test
  public void testDefault() {
    String[] args = {
    };
    Argument argument = new Argument();
    JavaProperty prop = new JavaProperty();
    
    JCommander jc = new JCommander();
    jc.addObject(argument);
    jc.addObject(prop);
    jc.parse(args);
    
    Assert.assertEquals(0, argument.getPort());
    Assert.assertTrue(prop.isAllnix());
  }
  
  @Test
  public void testHelp() {
    String[] args = {
      "--help"
    };
    Argument argument = new Argument();
    JavaProperty prop = new JavaProperty();
    
    JCommander jc = new JCommander();
    jc.addObject(argument);
    jc.addObject(prop);
    jc.parse(args);
    
    StringBuilder sb = new StringBuilder();
    jc.usage(sb);
    
    logger.info(sb.toString());
  }

  /**
   * Define main parameters
   */
  static class Argument {

    @Parameter(names = {"-port", "--port"}, description = "Server port number")
    private int port = 0;
    @Parameter(names = "--help", help = true)
    private boolean help;
    
    @Parameter(description = "Main Parameters")
    private List<String> params = new ArrayList<>();
    
    public int getPort() {
      return port;
    }
  }
  
  /**
   * Main parameters not allowed
   */
  @Parameters(separators = "=")
  static class JavaProperty {
    @Parameter(names= {"-Dorg.allnix"}, description = "is org.allnix", arity=1)
    private boolean allnix = true;
    
    public boolean isAllnix() {
      return allnix;
    }
  }
}
