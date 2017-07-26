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

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.LocalFileSystem;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.lookup.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
@ServiceProvider(service = TaskManager.class)
public class TaskManagerImpl implements TaskManager {

  static final private Logger logger = LoggerFactory.getLogger(
    TaskManagerImpl.class);

  private List<Task> topLevelTaskList;
  private PropertyChangeSupport pcs;
  private FileObject root;

  private Map<Task, DataObject> doByTask;

  public TaskManagerImpl() {
    logger.info(this.getClass().getName());
    doByTask = new HashMap<>();
    topLevelTaskList = new ArrayList<>();
    pcs = new PropertyChangeSupport(this);

//    Task t1 = createTask();
//    t1.setName("Todo 1");
//
//    Task t2 = createTask("Todo 1.1", t1.getId());
//    t2 = createTask("Todo 1.2", t1.getId());
//    t2 = createTask("Todo 1.3", t1.getId());
//    createTask("Todo 1.3.1", t2.getId());
//
//    Task t3 = createTask();
//    t3.setName("Todo 2");
//
//    t2 = createTask("Todo 2.1", t3.getId());
//    t2 = createTask("Todo 2.2", t3.getId());
//    t2 = createTask("Todo 2.3", t3.getId());
//
//    Task t4 = createTask("Todo 2.3.1", t3.getId());
//    t2 = createTask("Todo 2.3.1.1", t3.getId());
//    t2 = createTask("Todo 2.3.1.2", t3.getId());

    try {
      File file = new File("/home/ykyang/tmp/jpe/tasks/");
      LocalFileSystem fs = new LocalFileSystem();
      fs.setRootDirectory(file);
      root = fs.getRoot();
    } catch (PropertyVetoException | IOException ex) {
      ex.printStackTrace();
    }
    tasksLoaded = false;
//    this.save(t1);
//    this.save(t3);
//    System.out.println(
//      "Count of known TopLevelTasks before deleting: " + topLevelTaskList.size());
//
//    removeTask(t1.getId());
//    removeTask(t3.getId());
//    System.out.println(
//      "Count of known TopLevelTasks after deleting: " + topLevelTaskList.size());

//    loadTasks();
//    System.out.println(
//      "Count of known TopLevelTasks after loading: " + topLevelTaskList.size());
  }

  @Override
  public synchronized DataObject createTask() {
    DataObject dao = null;
    try {

      Task task = new TaskImpl();
      FileObject fo = save(task);
      dao = DataObject.find(fo);

      if (dao != null) {
        doByTask.put(task, dao);
      }

      topLevelTaskList.add(task);
      pcs.firePropertyChange(PROP_TASKLIST_ADD, null, task);
    } catch (DataObjectNotFoundException ex) {
      ex.printStackTrace();
    }

    return dao;
  }

  @Override
  public synchronized Task createTask(String name, String parentId) {
    Task task = new TaskImpl(name, parentId);
    Task parent = getTask(parentId);
    if (parent != null) {
      parent.addChild(task);
    }
    pcs.firePropertyChange(PROP_TASKLIST_ADD, parent, task);

    return task;
  }

  @Override
  public synchronized void removeTask(String id) {
    Task task = getTask(id);
    if (task == null) {
      return;
    }

    Task parent = getTask(task.getParentId());
    if (parent != null) {
      parent.remove(task);
    } else {
      DataObject dao = doByTask.get(task);
      if (dao != null) {
        try {
          dao.getPrimaryFile().delete();
          doByTask.remove(task);

        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }

    }

    topLevelTaskList.remove(task);
    pcs.firePropertyChange(PROP_TASKLIST_REMOVE, parent, task);
  }

  @Override
  public List<DataObject> getTopLevelTask() {
    return Collections.unmodifiableList(new ArrayList<DataObject>(doByTask.
      values()));
  }

  @Override
  public Task getTask(String id) {
    for (Task task : topLevelTaskList) {
      Task found = findTask(task, id);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  /**
   * Find recursively a task
   *
   * @param task Root task
   * @param id ID of the task to be found
   * @return Task if found, null otherwise
   */
  private Task findTask(Task task, String id) {
    if (id.equals(task.getId())) {
      return task;
    }

    for (Task childTask : task.getChildren()) {
      Task found = findTask(childTask, id);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  @Override
  public synchronized void addPropertyChangeListener(
    PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  @Override
  public synchronized void removePropertyChangeListener(
    PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  public FileObject save(Task task) {
    FileObject fo = null;
    try {
      fo = root.createData(task.getId() + ".tsk");
      save(task, fo);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return fo;
  }

  public void save(Task task, FileObject fo) {
    ObjectOutputStream out = null;
    try {
      out = new ObjectOutputStream(
        new BufferedOutputStream(fo.getOutputStream()));
      out.writeObject(task);
      fo.
        setAttribute("saved", SimpleDateFormat.getInstance().format(new Date()));
    } catch (IOException ex) {
    } finally {
      try {
        out.close();
      } catch (IOException ex) {
      }
    }
  }

  @Override
  public Task load(FileObject fo) {
    Task task = null;
    ObjectInputStream in = null;

    try {
      in = new ObjectInputStream(new BufferedInputStream(fo.getInputStream()));
      task = (Task) in.readObject();
      logger.info("Loaded: {}[{}]", task, fo.getAttribute("saved"));
      if (!topLevelTaskList.contains(task)) {
        topLevelTaskList.add(task);
        pcs.firePropertyChange(PROP_TASKLIST_ADD, null, task);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (IOException ex) {
      }
    }

    return task;
  }

  private boolean tasksLoaded;
  
  private void loadTasks() {
    logger.info("tasksLoaded: {}", tasksLoaded);
    if (tasksLoaded) {
      return;
    }
    tasksLoaded = true;
    logger.info("taskLoaded2: {}", tasksLoaded);
    logger.info("root.getChildren(): {}", root.getChildren().length);
    for (FileObject fo : root.getChildren()) {
      try {
        DataObject dao = DataObject.find(fo);
        if ( dao != null) {
          doByTask.put(dao.getLookup().lookup(Task.class), dao);
        }
      }catch(DataObjectNotFoundException e) {
        e.printStackTrace();
      }
//      if ("tsk".equalsIgnoreCase(fo.getExt())) {
//        load(fo);
//      }
    }
  }

  private void deleteTasks() {
    for (FileObject fo : root.getChildren()) {
      if ("tsk".equalsIgnoreCase(fo.getExt())) {
        try {
          fo.delete();
        } catch (IOException ex) {
        }
      }
    }
  }
}
