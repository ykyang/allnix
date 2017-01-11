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

import java.io.IOException;
import org.allnix.core.TextAreaStreamHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.testng.annotations.Test;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class GuiTestCommonsExec {

  @Test(groups = {"unix", "win"})
  public void testWindowStreamHandler() throws IOException, InterruptedException {
    // > python debug_stdout.py <length> <text>
    CommandLine cmd = new CommandLine("python");
    cmd.addArgument("script/debug_stdout.py");

    int length = 10_000;
    cmd.addArgument(Integer.toString(length), false);
    String text = "Line Number:";
    cmd.addArgument(text, false);

    TextAreaStreamHandler streamHandler = new TextAreaStreamHandler();

    DefaultExecutor executor = new DefaultExecutor();
    executor.setStreamHandler(streamHandler);

    int exitValue = executor.execute(cmd);

//    List<String> list = streamHandler.getStandardOut();
//    Assert.assertEquals(list.size(), length);
//    Assert.assertEquals(list.get(0), text+Integer.toString(1));
//    Assert.assertEquals(list.get(length-1), text+Integer.toString(length));
  }
}
