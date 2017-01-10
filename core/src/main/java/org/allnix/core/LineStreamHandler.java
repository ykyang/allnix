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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class LineStreamHandler implements ExecuteStreamHandler {
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
    if ( processOutputStream != null ) {
      outThread = createLineReader(stdout, processOutputStream, "cout");
      outThread.start();
      
    }
    if ( processErrorStream != null ) {
      errThread = createLineReader(stderr, processErrorStream, "cerr");
      errThread.start();
    }
  }

  @Override
  public void stop() throws IOException {
  }
 
  private Thread createStandardOutLineReader(final InputStream in) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          List<String> lines = IOUtils.readLines(in, StandardCharsets.UTF_8);
          stdout = lines;
        } catch (IOException ex) {
          throw new UncheckedIOException(ex);
        }
      }
    }, "cout");
  
    
    return thread;
  }
  
  private Thread createLineReader(final List<String> lines, 
                                  final InputStream is,
                                  final String name) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = in.readLine()) != null ) {
          lines.add(line);
        }
        
        
//          List<String> lines = IOUtils.readLines(in, StandardCharsets.UTF_8);
//          stderr = lines;
        } catch (IOException ex) {
          throw new UncheckedIOException(ex);
        }
      }
    }, name);
    
    return thread;
  }
  
  public List<String> getStandardOut() {
    return stdout;
  }
  
  public List<String> getStandardErr() {
    return stderr;
  }
}
