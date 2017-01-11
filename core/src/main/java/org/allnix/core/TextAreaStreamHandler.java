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
package org.allnix.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TextAreaStreamHandler implements ExecuteStreamHandler {

  static final private Logger logger = LoggerFactory.getLogger(
    TextAreaStreamHandler.class);
//  private JTextArea textArea;
  private InputStream processOutputStream;
  private InputStream processErrorStream;

  private Thread outThread;

  private Console console;

  private volatile String coutLine;
  private final Runnable coutDisplay;

  public TextAreaStreamHandler() {
    coutDisplay = new Runnable() {
      @Override
      public void run() {
        try {
          console.append(coutLine + "\n");
        } catch (BadLocationException ex) {
        }
      }

    };
  }

  @Override
  public void setProcessErrorStream(InputStream in) throws IOException {
    processErrorStream = in;
  }

  @Override
  public void setProcessInputStream(OutputStream out) throws IOException {
  }

  @Override
  public void setProcessOutputStream(InputStream in) throws IOException {
    processOutputStream = in;
  }

  @Override
  public void start() throws IOException {
    console = new Console();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Dimension d = new Dimension(400, 300);
        console.setPreferredSize(d);
        console.pack();
        console.setVisible(true);
      }
    });

    if (processOutputStream != null) {
      outThread = createLineReader(processOutputStream, coutDisplay);
      outThread.start();
    }
  }

  @Override
  public void stop() throws IOException {
//    try {
//      TimeUnit.SECONDS.sleep(20);
//    } catch (InterruptedException ex) {
//    }
//    System.out.println("Mark 30");
    console.dispose();
//    console.dispatchEvent(new WindowEvent(console, WindowEvent.WINDOW_CLOSING));
//    System.out.println("Mark 40");
  }

  private Thread createLineReader(final InputStream is, final Runnable r) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          BufferedReader in = new BufferedReader(new InputStreamReader(is));
          String line;
          while ((line = in.readLine()) != null) {
//            stdout.add(line);
            coutLine = line;
            try {
              SwingUtilities.invokeAndWait(r);
            } catch (InterruptedException | InvocationTargetException ex) {
            }
          }
        } catch (IOException ex) {
          logger.info(ex.getMessage());
//          throw new UncheckedIOException(ex);
        }
      }
    });

    return thread;
  }

  static class Console extends JFrame {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    final private int SCROLL_SIZE = 1000;

    public Console() {
      super("SMARTIES Console");
      textArea = new JTextArea();
//      textArea.setColumns(10);
//      textArea.setRows(30);
      scrollPane = new JScrollPane(textArea);
      textArea.setEditable(false);

      getContentPane().add(scrollPane, BorderLayout.CENTER);
//      pack();
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void append(String text) throws BadLocationException {
      textArea.append(text);

      int deleteLineCount = textArea.getLineCount() - SCROLL_SIZE;
      if (deleteLineCount > 0) {
        int endOfDeletePos = textArea.getLineEndOffset(deleteLineCount - 1);
        textArea.replaceRange("", 0, endOfDeletePos);
      }
    }
  }
}
