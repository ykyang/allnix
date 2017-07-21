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
import java.util.Collection;
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
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
  category = "Task",
  id = "org.netbeansrcp.taskactions.DeleteAction"
)
@ActionRegistration(
  displayName = "#CTL_DeleteAction",
  lazy=false
)
@ActionReferences({
  @ActionReference(path = "Toolbars/Task")
})
@Messages("CTL_DeleteAction=Delete Task")
public final class DeleteAction extends AbstractAction implements
  ContextAwareAction, LookupListener, Presenter.Popup, Presenter.Toolbar {

  private Lookup.Result<Task> result;
  
  public DeleteAction() {
    this(Utilities.actionsGlobalContext());
  }
  
  public DeleteAction(Lookup lookup) {
    super("Delete Task");
    
    result = lookup.lookupResult(Task.class);
    result.addLookupListener(this);
    resultChanged(null);
  }
  
  @Override
  public void actionPerformed(ActionEvent event) {
    if ( result == null) {
      return;
    }
    
    Collection<? extends Task> tasks = result.allInstances();
    Task task = tasks.iterator().next();
    TaskManager taskManager = Lookup.getDefault().lookup(TaskManager.class);
    try {
      taskManager.removeTask(task.getId());
    } catch (NullPointerException e) {
      // log
    }
  }

  @Override
  public Action createContextAwareInstance(Lookup actionContext) {
    return new DeleteAction(actionContext);
  }

  @Override
  public JMenuItem getPopupPresenter() {
    return new JMenuItem(this);
  }

  @Override
  public Component getToolbarPresenter() {
    return new JButton(this);
  }

  @Override
  public void resultChanged(LookupEvent ev) {
    setEnabled(!result.allInstances().isEmpty());
  }
}
