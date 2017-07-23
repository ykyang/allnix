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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
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
  protected Sheet createSheet() {
    final Task task = getLookup().lookup(Task.class);
    
    Sheet sheet = Sheet.createDefault();
    
    Sheet.Set set;

    // > Set: identification 
    set = Sheet.createPropertiesSet();
    set.setDisplayName("identification");
    sheet.put(set);
    
    Property<String> idProp
      = new PropertySupport.ReadOnly<String>("id",
                                             String.class,
                                             "ID",
                                             "identification number") {
      @Override
      public String getValue() throws IllegalAccessException,
        InvocationTargetException {
        return (null != task) ? task.getId() : "";
      }
    };
    
    Property<String> parentIdProp
      = new PropertySupport.ReadOnly<String>("parent-id", String.class,
                                             "parent-ID",
                                             "identification number of parent task") {
      @Override
      public String getValue() throws IllegalAccessException,
        InvocationTargetException {
        return (null != task) ? task.getParentId() : "";
      }
    };
    
    set.put(idProp);
    set.put(parentIdProp);

    // > Set: properties 
    set = Sheet.createExpertSet();
    set.setDisplayName("properties");
    sheet.put(set);
    
    try {
      // > name
      Property prop = new PropertySupport.Reflection<>(task,
                                                       String.class,
                                                       "name");
      prop.setName("name");
      prop.setDisplayName("name");
      prop.setShortDescription("name");
      
      set.put(prop);

      // > due
      prop = new PropertySupport.Reflection<>(task, Date.class, "due");
      prop.setName("due");
      prop.setDisplayName("due date");
      prop.setShortDescription("due date");
      
      set.put(prop);
      // > priority
      prop = new PropertySupport.Reflection<>(task, Task.Priority.class, "prio");
      prop.setName("priority");
      prop.setDisplayName("priority");
      prop.setShortDescription("priority");
      
      set.put(prop);

      // > Progress
      prop = new PropertySupport.ReadWrite<Integer>("progress", Integer.class,
                                                    "Progress", "Progress") {
        @Override
        public Integer getValue() throws IllegalAccessException,
          InvocationTargetException {
          return (null != task) ? task.getProgr() : 0;
        }
        
        @Override
        public void setValue(Integer t) throws IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
          if (task != null && t != null) {
            task.setProgr(t);
          }
        }
        
      };
      
      set.put(prop);
    } catch (NoSuchMethodException ex) {
      Exceptions.printStackTrace(ex);
    }

    // > Set: description
    set = new Sheet.Set();
    set.setName("description");
    set.setValue("tabName", " description ");
    sheet.put(set);
    
    try {
      Property prop = new PropertySupport.Reflection<>(task, String.class,
                                                       "descr");
      prop.setName("description");
      prop.setDisplayName("description ");
      prop.setShortDescription("description ");
      
      set.put(prop);
    } catch (NoSuchMethodException ex) {
      Exceptions.printStackTrace(ex);
    }
    
    return sheet;
  }
  
  @Override
  public String getHtmlDisplayName() {
    String color = "000000";
    
    Task task = getLookup().lookup(Task.class);
    switch (task.getPrio()) {
      case LOW:
        color = "74a5c9"; // blue
        break;
      case MEDIUM:
        color = "000000"; // black
        break;
      case HIGH:
        color = "98002e"; // red
        break;
    }
    
    String html = "<font color='%s'>%s</font>";
    
    return String.format(html, color, task.getName());
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent event) {
    String propertyName = event.getPropertyName();
    if (Task.PROP_NAME.equals(propertyName) || Task.PROP_PRIO.equals(
      propertyName)) {
      setDisplayName(event.getNewValue().toString());
    }
  }
  
  @Override
  public Action getPreferredAction() {
//   Action action = Utilities.actionsForPath("Tasks/Nodes/Task/PreferredAction").get(0);

    Action action
      = FileUtil.getConfigObject(
        "Actions/Task/org-netbeansrcp-taskactions-EditAction.instance",
        Action.class);
    
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
    if (registeredActions == null) {
//      registeredActions = Utilities.actionsForPath("Tasks/Nodes/Task/Actions");
      registeredActions = Utilities.actionsForPath("Actions/Task");
    }
    
    return registeredActions;
  }
}
