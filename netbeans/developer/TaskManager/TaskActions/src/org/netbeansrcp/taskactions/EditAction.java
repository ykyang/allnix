/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
@ActionReference(path = "Menu/Edit", position = 0)
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
