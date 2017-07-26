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
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public interface TaskManager {

//  Task createTask();
  Task createTask(String name, String parentId);
  void removeTask(String id);
//  List<Task> getTopLevelTask();
  List<DataObject> getTopLevelTask();
  Task getTask(String id);
  
  static final String PROP_TASKLIST_ADD = "TASK_LIST_ADD";
  static final String PROP_TASKLIST_REMOVE = "TASK_LIST_REMOVE";
  
  void addPropertyChangeListener(PropertyChangeListener listener);
  void removePropertyChangeListener(PropertyChangeListener listener);
  
  FileObject save(Task task);
  void save(Task task, FileObject fo);
  Task load(FileObject fo);
  
  DataObject createTask();
//   public void loadTasks();
}
