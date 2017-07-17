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
package com.netbeansrcp.duelist;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskChildren extends Children.Array implements
  PropertyChangeListener, ChangeListener {

  private long startTime;
  private long endTime;
  private JSpinner spinner;
  private TaskManager taskManager;

  public TaskChildren(JSpinner spinner) {
    this.spinner = spinner;
    spinner.addChangeListener(this);

    taskManager = Lookup.getDefault().lookup(TaskManager.class);
    if (taskManager != null) {
      taskManager.addPropertyChangeListener(this);
    }
  }

  @Override
  protected Collection<Node> initCollection() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.WEEK_OF_YEAR, (Integer) spinner.getValue());
    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
    startTime = cal.getTimeInMillis();

    cal.add(Calendar.WEEK_OF_YEAR, 1);
    endTime = cal.getTimeInMillis();

    List<Task> dueTasks = new ArrayList<Task>();
    if (taskManager != null) {
      List<Task> topLevelTasks = taskManager.getTopLevelTask();
      for (Task topLevelTask : topLevelTasks) {
        findDueTasks(topLevelTask, dueTasks);
      }
    }

    Collection<Node> dueNodes = new ArrayList<Node>(dueTasks.size());
    for (Task task : dueTasks) {
      dueNodes.add(new TaskNode(task));
      task.addPropertyChangeListener(this);
    }

    return dueNodes;
  }

  private void findDueTasks(Task task, List<Task> dueTasks) {
    long dueTime = task.getDue().getTime();
    if (dueTime >= this.startTime && dueTime <= this.endTime) {
      dueTasks.add(task);
    }
    for (Task subTask : task.getChildren()) {
      this.findDueTasks(subTask, dueTasks);
    }
  }

  private void updateNodes() {
    remove(getNodes());
    add(initCollection().toArray(new Node[]{}));
  }

  public void propertyChange(PropertyChangeEvent arg0) {
    if ((arg0.getSource() instanceof Task) && TaskManager.PROP_TASKLIST_ADD.equals(
      arg0.getPropertyName()) || TaskManager.PROP_TASKLIST_REMOVE.equals(
        arg0.getPropertyName())) {
      this.updateNodes();
    }
    if ((arg0.getSource() instanceof TaskManager) && TaskManager.PROP_TASKLIST_ADD.equals(
      arg0.getPropertyName()) || TaskManager.PROP_TASKLIST_REMOVE.equals(
        arg0.getPropertyName())) {
      this.updateNodes();
    }
  }

  public void stateChanged(ChangeEvent arg0) {
    this.updateNodes();
  }
}
