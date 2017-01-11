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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class MainDisplay {

  static public void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        System.out.println("Start...");
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

        try {
          System.out.println("Mark 10");
          ExecuteResultHandler resultHandler = new ExecuteResultHandler() {
            @Override
            public void onProcessComplete(int i) {
              System.exit(0);
            }

            @Override
            public void onProcessFailed(ExecuteException ee) {
              System.exit(1);
            }
          };
            
          executor.execute(cmd, resultHandler);
          
          System.out.println("Mark 20");
        } catch (IOException ex) {
        }
//        System.exit(0);
      }

    });
  }
}
