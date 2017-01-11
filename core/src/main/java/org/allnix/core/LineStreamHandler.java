/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class LineStreamHandler implements ExecuteStreamHandler {

  static private final Logger logger = LoggerFactory.getLogger(
    LineStreamHandler.class);

  private static final long STOP_TIMEOUT_ADDITION = 2000L;

  /**
   * the last exception being caught
   */
  private IOException caught = null;
  private long stopTimeout = 100;
  
  private List<String> stdout;
  private List<String> stderr;
  private Thread outThread, errThread;
  private InputStream processOutputStream, processErrorStream;

  public LineStreamHandler() {
    stdout = Collections.synchronizedList(new LinkedList<String>());
    stderr = Collections.synchronizedList(new LinkedList<String>());
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
    if (processOutputStream != null) {
      outThread = createLineReader(stdout, processOutputStream, "cout");
      outThread.start();

    }
    if (processErrorStream != null) {
      errThread = createLineReader(stderr, processErrorStream, "cerr");
      errThread.start();
    }
  }

  @Override
  public void stop() throws IOException {
    stopThread(outThread, stopTimeout);
    stopThread(errThread, stopTimeout);
  }

//  private Thread createStandardOutLineReader(final InputStream in) {
//    Thread thread = new Thread(new Runnable() {
//      @Override
//      public void run() {
//        try {
//          List<String> lines = IOUtils.readLines(in, StandardCharsets.UTF_8);
//          stdout = lines;
//        } catch (IOException ex) {
//          throw new UncheckedIOException(ex);
//        }
//      }
//    }, "cout");
//
//    return thread;
//  }
  private Thread createLineReader(final List<String> lines,
                                  final InputStream is,
                                  final String name) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          BufferedReader in = new BufferedReader(new InputStreamReader(is));
          String line;
          while ((line = in.readLine()) != null) {
            lines.add(line);
          }
        } catch (IOException ex) {
          logger.info(ExceptionUtils.getStackTrace(ex));
        }
      }
    }, name);

    return thread;
  }

  /**
   * Stopping a pumper thread. The implementation actually waits
   * longer than specified in 'timeout' to detect if the timeout
   * was indeed exceeded. If the timeout was exceeded an IOException
   * is created to be thrown to the caller.
   *
   * @param thread the thread to be stopped
   * @param timeout the time in ms to wait to join
   */
  protected void stopThread(final Thread thread, final long timeout) {

    if (thread != null) {
      try {
        if (timeout == 0) {
          thread.join();
        } else {
          final long timeToWait = timeout + STOP_TIMEOUT_ADDITION;
          final long startTime = System.currentTimeMillis();
          thread.join(timeToWait);
          if (!(System.currentTimeMillis() < startTime + timeToWait)) {
            final String msg = "The stop timeout of " + timeout + " ms was exceeded";
            caught = new ExecuteException(msg, Executor.INVALID_EXITVALUE);
          }
        }
      } catch (final InterruptedException e) {
        thread.interrupt();
      }
    }
  }

  public List<String> getStandardOut() {
    return stdout;
  }

  public List<String> getStandardErr() {
    return stderr;
  }
}
