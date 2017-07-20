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
package com.netbeansrcp.overview;

import com.netbeansrcp.taskmodel.api.Task;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskChildFactory extends ChildFactory<Task> implements
  PropertyChangeListener {

  private Task task;

  public TaskChildFactory(Task task) {
    this.task = task;
    task.addPropertyChangeListener(this);
  }

  @Override
  protected boolean createKeys(List<Task> list) {
    final long delay = 500;

    ProgressHandle handle = ProgressHandle.createHandle("Creating subtasks...");
    // > Deprecated: 
    // > ProgressHandle handle = ProgressHandleFactory.createHandle("Creating subtasks...");

    handle.start(100);
    SwingUtilities.invokeLater(() -> {
      JFrame mainWindow = (JFrame) WindowManager.getDefault().getMainWindow();
//      Cursor cursor = mainWindow.getGlassPane().getCursor();
//      System.out.println(cursor.toString());
      // > For some reason the WAIT_CURSOR on Linux shows up as a dot
      mainWindow.getGlassPane().setCursor(
        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    });

    try {
      handle.progress(1);
      TimeUnit.MILLISECONDS.sleep(delay);
      handle.progress(25);
      TimeUnit.MILLISECONDS.sleep(delay);
      handle.progress(50);
      TimeUnit.MILLISECONDS.sleep(delay);
      handle.progress(75);
    } catch (InterruptedException ex) {
      Exceptions.printStackTrace(ex);
    }

    handle.finish();
    list.addAll(task.getChildren());

    return true;
  }

  protected Node[] createNodesForKey(Task task) {
    return new TaskNode[]{new TaskNode(task)};
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String name = evt.getPropertyName();

    if (Task.PROP_CHILDREN_ADD.equals(name) || Task.PROP_CHILDREN_REMOVE.equals(
      name)) {
      refresh(true);
    }
  }

}
