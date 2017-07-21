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

import com.netbeansrcp.taskeditor.TaskEditorTopComponent;
import com.netbeansrcp.taskmodel.api.Task;
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
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
  category = "Task",
  id = "org.netbeansrcp.taskactions.EditAction"
)
@ActionRegistration(
  displayName = "#CTL_EditAction",
  lazy=false
)
@ActionReferences({
  @ActionReference(path = "Menu/Edit", position = 0),
  @ActionReference(path = "Toolbars/Task")
})
@Messages("CTL_EditAction=Edit")
public final class EditAction extends AbstractAction implements LookupListener,
  Presenter.Toolbar, ContextAwareAction, Presenter.Popup {

  private Lookup.Result<Task> result;
  private JButton toolbarButton;

  public EditAction() {
    this(Utilities.actionsGlobalContext());
  }

  public EditAction(Lookup lookup) {
    super("Edit Task ...");  // TODO: Add icon

    result = lookup.lookupResult(Task.class);
    result.addLookupListener(this);
    resultChanged(new LookupEvent(result));
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    if (result != null && result.allInstances().size() > 0) {
      Task task = result.allInstances().iterator().next();
      EditAction.openInTaskEditor(task);
    }
  }

  public static void openInTaskEditor(Task task) {
    TaskEditorTopComponent win = TaskEditorTopComponent.findInstance(task);
    win.open();
    win.requestActive();
  }

  @Override
  public Action createContextAwareInstance(Lookup lkp) {
    return new EditAction(lkp);
  }

  @Override
  public JMenuItem getPopupPresenter() {
    return new JMenuItem(this);
  }

  @Override
  public Component getToolbarPresenter() {
    if (toolbarButton == null) {
      toolbarButton = new JButton(this);
    }
    return toolbarButton;
  }

  @Override
  public void resultChanged(LookupEvent le) {
    if (result.allInstances().size() > 0) {
      setEnabled(true);
      if (toolbarButton != null) {
        toolbarButton.setEnabled(true);
      }
    } else {
      setEnabled(false);
      if (toolbarButton != null) {
        toolbarButton.setEnabled(false);
      }
    }
  }
}
