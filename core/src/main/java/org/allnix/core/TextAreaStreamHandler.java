/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
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
  
  static final private Logger logger = LoggerFactory.getLogger(TextAreaStreamHandler.class);
//  private JTextArea textArea;
  private InputStream processOutputStream;
  private InputStream processErrorStream;

  private Thread outThread;

  private Console console;

  private LinkedList<String> stdout;

  public TextAreaStreamHandler() {
    stdout = new LinkedList<String>();
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
      outThread = createLineReader(processOutputStream);
      outThread.start();
    }
  }

  @Override
  public void stop() throws IOException {
//    try {
//      TimeUnit.SECONDS.sleep(20);
//    } catch (InterruptedException ex) {
//    }
    console.dispose();
  }

  private Thread createLineReader(final InputStream is) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          BufferedReader in = new BufferedReader(new InputStreamReader(is));
          String line;
          while ((line = in.readLine()) != null) {
            stdout.add(line);
            try {
              SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                  try {
                    console.append(stdout.poll() + "\n");
                  } catch (BadLocationException ex) {

                  }
                }
              });

            } catch (InterruptedException ex) {
            } catch (InvocationTargetException ex) {
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
