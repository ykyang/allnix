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
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TopLevelTaskChildren extends Children.Keys<DataObject> implements
        PropertyChangeListener {

  private TaskManager taskManager;

  public TopLevelTaskChildren() {
    taskManager = Lookup.getDefault().lookup(TaskManager.class);

    if (taskManager != null) {
//      taskManager.loadTasks();
      taskManager.addPropertyChangeListener(this);
    }
  }

  @Override
  protected void addNotify() {
    super.addNotify();

    if (taskManager != null) {
      setKeys(taskManager.getTopLevelTask());
    }
  }

  @Override
  protected Node[] createNodes(DataObject t) {
    return new Node[]{t.getNodeDelegate()};
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (taskManager == null) {
      return;
    }

    String name = evt.getPropertyName();
    if (TaskManager.PROP_TASKLIST_ADD.equals(name) || TaskManager.PROP_TASKLIST_REMOVE.
            equals(name)) {
      setKeys(taskManager.getTopLevelTask());
    }
  }

}
