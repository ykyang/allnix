/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskeditor;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(
  category = "Edit",
  id = "com.netbeansrcp.taskeditor.NewTaskAction"
)
@ActionRegistration(
  displayName = "#CTL_NewTaskAction"
)
@ActionReference(path = "Menu/Edit", position = 100)
@Messages("CTL_NewTaskAction=New")
public final class NewTaskAction implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    TaskManager taskManager = Lookup.getDefault().lookup(TaskManager.class);
    if (taskManager == null) {
      return;
    }
    Task task = taskManager.createTask();
    TaskEditorTopComponent win = TaskEditorTopComponent.findInstance(task);
    win.open();
    win.requestActive();
  }
}
