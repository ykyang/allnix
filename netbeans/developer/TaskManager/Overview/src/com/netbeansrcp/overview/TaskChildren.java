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
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskChildren extends Children.Keys<Task> implements PropertyChangeListener {

  private Task task;
  
  public TaskChildren(Task task) {
    this.task = task;
    task.addPropertyChangeListener(this);
  }
  
  @Override
  protected void addNotify() {
    super.addNotify();
    setKeys(task.getChildren());
  }
  
  @Override
  protected Node[] createNodes(Task key) {
    // > Creates a new TaskNode for each child Task
    return new TaskNode[] {new TaskNode(key)};
  }

  /**
   * Update child list
   * @param event 
   */
  @Override
  public void propertyChange(PropertyChangeEvent event) {
    String name = event.getPropertyName();
    if (TaskManager.PROP_TASKLIST_ADD.equals(name) || TaskManager.PROP_TASKLIST_REMOVE.equals(name)) {
      setKeys(task.getChildren());
    }
  }
  
}
