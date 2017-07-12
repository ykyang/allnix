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
package com.netbeansrcp.taskmodel.api;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public interface Task extends Serializable {

  public java.lang.String getId();

  public java.lang.String getParentId();

  public java.lang.String getName();

  public void setName(java.lang.String name);

  public java.util.Date getDue();

  public void setDue(java.util.Date due);

  public enum Priority {
    LOW, MEDIUM, HIGH
  }

  public Priority getPrio();

  public void setPrio(Priority prio);

  public int getProgr();

  public void setProgr(int progr);

  public java.lang.String getDescr();

  public void setDescr(java.lang.String descr);

  public void addChild(Task subTask);

  public java.util.List<Task> getChildren();

  public boolean remove(Task subTask);

  public void addPropertyChangeListener(PropertyChangeListener listener);

  public void removePropertyChangeListener(PropertyChangeListener listener);
  
  public static final String PROP_NAME = "name";
  public static final String PROP_DUE = "due";
  public static final String PROP_PRIO = "prio";
  public static final String PROP_PROGR = "progr";
  public static final String PROP_DESCR = "descr";
  public static final String PROP_CHILDREN_ADD = "children_add";
  public static final String PROP_CHILDREN_REMOVE = "children_remove";
}
