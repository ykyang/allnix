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
  id = "org.netbeansrcp.taskactions.CopyAction"
)
@ActionRegistration(
  displayName = "#CTL_CopyAction",
  lazy = false
)
@ActionReferences({
  @ActionReference(path = "Toolbars/Task")
})
@Messages("CTL_CopyAction=Copy Task")
public final class CopyAction extends AbstractAction implements LookupListener,
  ContextAwareAction, Presenter.Toolbar, Presenter.Popup {

  private Lookup.Result<Task> result;

  public CopyAction() {
    this(Utilities.actionsGlobalContext());
  }

  public CopyAction(Lookup lookup) {
    super("Copy Task");

    result = lookup.lookupResult(Task.class);
    result.addLookupListener(this);

    // > ?????
    resultChanged(new LookupEvent(result));
    resultChanged(null);
  }

//  public CopyAction(String name) {
//    super(name);
//  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if ( result == null ) {
      return;
    }
    
    Collection<? extends Task> tasks = result.allInstances();
    if ( tasks.isEmpty()) {
      return;
    }
    
    Task source = tasks.iterator().next();
    TaskManager taskManager = Lookup.getDefault().lookup(TaskManager.class);

    Task target = null;
    String parentId = source.getParentId();
    if ( parentId != null && !parentId.isEmpty()) {
      target = taskManager.createTask(source.getName(), parentId);
    } else {
      target = taskManager.createTask().getLookup().lookup(Task.class);
      target.setName(source.getName());
    }
    
    target.setDescr(source.getDescr());
    target.setDue(source.getDue());
    target.setPrio(source.getPrio());
    target.setProgr(source.getProgr());
    EditAction.openInTaskEditor(target);
  }

  @Override
  public Action createContextAwareInstance(Lookup actionContext) {
    return new CopyAction(actionContext);
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
