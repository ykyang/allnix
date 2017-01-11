/*
 * Copyright 2016 Yi-Kun Yang.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.allnix.core.LineStreamHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TestCommonsExec {

  static final Logger logger = LoggerFactory.getLogger(TestCommonsExec.class);

  volatile boolean complete = false;
  
  @Test(groups = {"unix"})
  public void testOneLine() throws IOException {
    String line = "ls -l";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    int exitValue = executor.execute(cmdLine);

    Assert.assertEquals(exitValue, 0);
  }

  @Test(groups = {"unix"})
  public void testQuoting() {
    CommandLine cmdLine = new CommandLine("ls");
    cmdLine.addArgument("File Name with quote", true);
    cmdLine.addArgument("File Name without quote", false);

    System.out.println(cmdLine);
  }

  @Test(groups = {"unix"})
  public void testEcho() throws IOException {
    CommandLine cmd = new CommandLine("script/echo.sh");
    String param = "Qutoe is necessary";
    // > exec handles the quote correctly
    // > when the second param set to true
    // > additional quotes are added
    cmd.addArgument(param, false);

    System.out.println(cmd);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PumpStreamHandler streamHandler = new PumpStreamHandler(out);

    DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);

    int exitValue = executor.execute(cmd);

    System.out.println(out.toString());
  }

  @Test(groups = {"unix"})
  public void testLineStreamHandler() throws IOException {
    // > script/loop.sh <length> <text>
    CommandLine cmd = new CommandLine("script/loop.sh");
    int length = 100_000;
    cmd.addArgument(Integer.toString(length), false);
    String text = "Line Number:";
    cmd.addArgument(text, false);

    LineStreamHandler streamHandler = new LineStreamHandler();

    DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);

    int exitValue = executor.execute(cmd);
    List<String> list = streamHandler.getStandardOut();
    Assert.assertEquals(list.size(), length);
    Assert.assertEquals(list.get(0), text + Integer.toString(1));
    Assert.assertEquals(list.get(length - 1), text + Integer.toString(length));
  }

  @Test(groups = {"unix", "win"})
  public void testLineStreamHandler2() throws IOException {
    // > python debug_stdout.py <length> <text>
    CommandLine cmd = new CommandLine("python");
    cmd.addArgument("script/debug_stdout.py");

    int length = 10_000;

    cmd.addArgument(Integer.toString(length), false);

    String text = "Line Number:";
    cmd.addArgument(text, false);

    LineStreamHandler streamHandler = new LineStreamHandler();

    DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);

    int exitValue = executor.execute(cmd);
    List<String> list = streamHandler.getStandardOut();
    Assert.assertEquals(list.size(), length);
    Assert.assertEquals(list.get(0), text + Integer.toString(1));
    Assert.assertEquals(list.get(length - 1), text + Integer.toString(length));
  }

  @Test(groups = {"unix", "win"})
  public void testLineStreamhandler3() throws IOException, InterruptedException {
    // > python debug_stdout.py <length> <text>
    CommandLine cmd = new CommandLine("python");
    cmd.addArgument("script/debug_stdout.py");

    int length = 10_000;

    cmd.addArgument(Integer.toString(length), false);

    String text = "Line Number:";
    cmd.addArgument(text, false);

    LineStreamHandler streamHandler = new LineStreamHandler();
    ExecuteWatchdog watchdog = new ExecuteWatchdog(
      ExecuteWatchdog.INFINITE_TIMEOUT);

    DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
      @Override
      public void onProcessComplete(int i) {
        super.onProcessComplete(i);
        complete = true;
        logger.info("Process Complete");
      }

      @Override
      public void onProcessFailed(ExecuteException ee) {
        super.onProcessFailed(ee);
        logger.info("Process Failed");
        Assert.fail();
      }
    };

    DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);
    executor.setWatchdog(watchdog);

//    int exitValue = executor.execute(cmd);

    executor.execute(cmd, resultHandler);

    resultHandler.waitFor();
    
    Assert.assertEquals(resultHandler.getExitValue(), 0);
    Assert.assertTrue(complete);
    
    List<String> list = streamHandler.getStandardOut();
    Assert.assertEquals(list.size(), length);
    Assert.assertEquals(list.get(0), text + Integer.toString(1));
    Assert.assertEquals(list.get(length - 1), text + Integer.toString(length));
    
    
  }

}
