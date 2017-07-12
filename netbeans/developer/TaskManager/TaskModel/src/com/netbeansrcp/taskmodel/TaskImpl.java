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
package com.netbeansrcp.taskmodel;

import com.netbeansrcp.taskidgenerator.api.TaskIdGenerator;
import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.Task.Priority;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskImpl implements Task {

  private String id = "";
  private String name = "";
  private String parentId = "";
  private Date due = new Date();
  private Priority prio = Priority.MEDIUM;
  private int progr = 0;
  private String descr = "";
  private List<Task> children = new ArrayList<>();
  private PropertyChangeSupport pss;

  public TaskImpl() {
    this("", "");
  }

  public TaskImpl(String name, String parentId) {
    TaskIdGenerator idGen = Lookup.getDefault().lookup(TaskIdGenerator.class);
    
    this.id = idGen.generateId();
    this.name = name;
    this.parentId = parentId;
    this.due = new Date();
    this.prio = Priority.MEDIUM;
    this.progr = 0;
    this.descr = "";
    this.children = new ArrayList<Task>();
    this.pss = new PropertyChangeSupport(this);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.pss.firePropertyChange(PROP_NAME, old, name);
  }

  public String getParentId() {
    return parentId;
  }

  public Date getDue() {
    return due;
  }

  public void setDue(Date due) {
    Date old = this.due;
    this.due = due;
    this.pss.firePropertyChange(PROP_DUE, old, due);
  }

  public Priority getPrio() {
    return prio;
  }

  public void setPrio(Priority prio) {
    Priority old = this.prio;
    this.prio = prio;
    this.pss.firePropertyChange(PROP_PRIO, old, prio);
  }

  public int getProgr() {
    return progr;
  }

  public void setProgr(int progr) {
    int old = this.progr;
    this.progr = progr;
    this.pss.firePropertyChange(PROP_PROGR, old, progr);
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    String old = this.descr;
    this.descr = descr;
    this.pss.firePropertyChange(PROP_DESCR, old, descr);
  }

  public List<Task> getChildren() {

    return Collections.unmodifiableList(this.children);
  }

  public void addChild(Task subTask) {
    this.children.add(subTask);
    this.pss.firePropertyChange(PROP_CHILDREN_ADD, null, this.children);
  }

  public boolean remove(Task subTask) {
    boolean res = this.children.remove(subTask);
    this.pss.firePropertyChange(PROP_CHILDREN_REMOVE, null, this.children);
    return res;
  }

  public synchronized void addPropertyChangeListener(
    PropertyChangeListener listener) {
    this.pss.addPropertyChangeListener(listener);
  }

  public synchronized void removePropertyChangeListener(
    PropertyChangeListener listener) {
    this.pss.removePropertyChangeListener(listener);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TaskImpl other = (TaskImpl) obj;
    return this.id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return this.getId() + " - " + this.parentId + " - " + this.getName() + " - " + DateFormat.getInstance().format(
      this.due) + " - " + this.prio + " - " + this.progr + " - " + this.descr;
  }
}
