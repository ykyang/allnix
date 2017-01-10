/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.ext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.allnix.core.LineStreamHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
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
  
  @Test(groups = {"linux"})
  public void testOneLine() throws IOException {
    String line = "ls -l";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    int exitValue = executor.execute(cmdLine);
    
    Assert.assertEquals(exitValue, 0);
  }
  
  @Test(groups = {"linux"})
  public void testQuoting() {
    CommandLine cmdLine = new CommandLine("ls");
    cmdLine.addArgument("File Name with quote", true);
    cmdLine.addArgument("File Name without quote", false);
    
    System.out.println(cmdLine);
  }
  
  @Test(groups = {"linux"})
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
  
  @Test(groups = {"linux"})
  public void testLineStreamHandler() throws IOException {
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
    Assert.assertEquals(list.get(0), text+Integer.toString(1));
    Assert.assertEquals(list.get(length-1), text+Integer.toString(length));
  }
}
