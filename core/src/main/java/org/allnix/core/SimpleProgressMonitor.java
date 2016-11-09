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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class SimpleProgressMonitor implements ProgressMonitor {

  static private final Logger logger = LoggerFactory.getLogger(
    SimpleProgressMonitor.class);

  AtomicBoolean canceled = new AtomicBoolean(false);
  AtomicBoolean done = new AtomicBoolean(false);
  AtomicInteger totalWork = new AtomicInteger(-1);
  AtomicInteger worked = new AtomicInteger(0);

  final Object taskLock = new Object();
  String taskName = "Task Name";

  final Object subTaskLock = new Object();
  String subTaskName = "Subtask Name";

  List<String> subTaskList = Collections.synchronizedList(
    new ArrayList<String>());
  List<ProgressMonitorListener> cancelListener = Collections.synchronizedList(
    new ArrayList<ProgressMonitorListener>());

  @Override
  public void beginTask(String name, int totalWork) {
    synchronized (taskLock) {
      taskName = name;
    }
    this.totalWork.set(totalWork);
    worked.set(0);
  }

  @Override
  public boolean isCanceled() {
    return canceled.get();
  }

  public void addCancelListener(ProgressMonitorListener listener) {
    cancelListener.add(listener);
  }

  @Override
  public void cancel() {
    boolean ans = canceled.compareAndSet(false, true);
    if (ans) {
      cancelListener.forEach((listener) -> {
        try {
          listener.actionPerformed();
        } catch (Exception e) {
          // > Show must go on so we catch all exceptions
          logger.error("Exception: ", e);
        }
      });
    }
  }

  @Override
  public void subTask(String name) {
    synchronized (subTaskLock) {
      subTaskName = name;
    }
    subTaskList.add(name);
  }

  @Override
  public void worked(int value) {
    worked.getAndAdd(value);
  }

  public String getSubTask() {
    synchronized (subTaskLock) {
      return subTaskName;
    }
  }

  public String getTask() {
    synchronized (taskLock) {
      return taskName;
    }
  }

  @Override
  public void done() {
    done.set(true);
  }

  public int getWorked() {
    return worked.get();
  }

  public boolean isDone() {
    return done.get();
  }

  public List<String> getSubTaskList() {
    return subTaskList;
  }
}
