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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskNode extends AbstractNode implements PropertyChangeListener {

  public TaskNode(Task task) {
    this(task, Lookups.singleton(task));
//    super(new TaskChildren(task), Lookups.singleton(task));
//
//    setName(task.getId());
//    setDisplayName(task.getName());
//    // > TODO: Add icon
//    task.addPropertyChangeListener(this);
  }
  
  public TaskNode(Task task, Lookup lookup) {
    super(Children.create(new TaskChildFactory(task), true), lookup);
    
    setName(task.getId());
    setDisplayName(task.getName());
    // > TODO p. 137 Add icons
    
    task.addPropertyChangeListener(this);
  }
  
  @Override
  public String getHtmlDisplayName() {
    // > TODO: Use a template
    String html = "<font color='";
    Task task = getLookup().lookup(Task.class);
    switch(task.getPrio()) {
      case LOW:
        html += "0000FF"; // blue
        break;
      case MEDIUM:
        html += "000000"; // black
        break;
      case HIGH:
        html += "FF0000"; // red
        break;
    }
    
    html += "'>" + task.getName() + "</font>";
    return html;
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent event) {
    String propertyName = event.getPropertyName();
    if ( Task.PROP_NAME.equals(propertyName) || Task.PROP_PRIO.equals(propertyName)) {
      setDisplayName(event.getNewValue().toString());
    }
  }
  
  @Override
  public Action getPreferredAction() {
   Action action = Utilities.actionsForPath("Tasks/Nodes/Task/PreferredAction").get(0);
   return action;
  }
  
  @Override
  public Action[] getActions(boolean context) {
    List<Action> actions = new ArrayList<>();
    actions.addAll(getRegisteredActions());
    actions.addAll(Arrays.asList(super.getActions(context)));
//    System.out.println("Action count: "+ actions.size());
    return actions.toArray(new Action[actions.size()]);
  }
  
  static List<? extends Action> registeredActions;
  protected static List<? extends Action> getRegisteredActions() {
    if ( registeredActions == null) {
      registeredActions = Utilities.actionsForPath("Tasks/Nodes/Task/Actions");
    }
    
    return registeredActions;
  }
}
