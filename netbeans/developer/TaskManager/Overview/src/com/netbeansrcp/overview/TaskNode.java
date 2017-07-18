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
import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskNode extends AbstractNode implements PropertyChangeListener {

  public TaskNode(Task task) {
    super(new TaskChildren(task), Lookups.singleton(task));

    setName(task.getId());
    setDisplayName(task.getName());
    // > TODO: Add icon
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
}
