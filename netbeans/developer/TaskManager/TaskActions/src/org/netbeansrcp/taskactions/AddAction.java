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
package org.netbeansrcp.taskactions;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(
  category = "Task",
  id = "org.netbeansrcp.taskactions.AddAction"
)
@ActionRegistration(
  displayName = "#CTL_AddAction",
  lazy = false
)
@ActionReferences({
  @ActionReference(path = "Menu/Edit", position = 1300),
  @ActionReference(path="Toolbars/Task")
})
@Messages("CTL_AddAction=Add Action")
public final class AddAction extends AbstractAction implements
  Presenter.Toolbar, Presenter.Popup, ContextAwareAction, LookupListener {

  private Lookup.Result<Task> result;
  private JButton toolbarButton;

  public AddAction() {
    super("Create New Task...");
  }

  private AddAction(Lookup lookup) {
    super("Create New Task...");

    result = lookup.lookupResult(Task.class);
    result.addLookupListener(this);
    // > Initialize???
    resultChanged(new LookupEvent(result));
  }

//  private AddAction(String label) {
//    super(label);
//  }
  @Override
  public void actionPerformed(ActionEvent e) {
    TaskManager taskManager = Lookup.getDefault().lookup(TaskManager.class);
    
    Task task = null;
    if ( result != null && !result.allClasses().isEmpty()) {
      task = result.allInstances().iterator().next();
      task = taskManager.createTask("New Sub Task", task.getId());
    } else {
      task = taskManager.createTask().getLookup().lookup(Task.class);
      task.setName("New Task");
    }
    
    EditAction.openInTaskEditor(task);
  }

  @Override
  public Action createContextAwareInstance(Lookup lkp) {
    return new AddAction(lkp);
  }

  @Override
  public JMenuItem getPopupPresenter() {
    return new JMenuItem(this);
  }

  @Override
  public Component getToolbarPresenter() {
    if ( toolbarButton == null) {
      toolbarButton = new JButton(this);
    }
    
    return toolbarButton;
  }

  @Override
  public void resultChanged(LookupEvent le) {
    if ( result.allInstances().isEmpty()) {
      setEnabled(false);
    } else {
      setEnabled(true);
    }
  }
}
