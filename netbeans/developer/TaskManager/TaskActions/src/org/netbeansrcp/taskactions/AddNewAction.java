/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansrcp.taskactions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(
  category = "Task",
  id = "org.netbeansrcp.taskactions.AddNewAction"
)
@ActionRegistration(
  displayName = "#CTL_AddNewAction",
  lazy=false
)
@ActionReference(path = "Menu/File", position = 1300)
@Messages("CTL_AddNewAction=New Action")
public final class AddNewAction extends AbstractAction implements ContextAwareAction {

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO implement action body
  }

  @Override
  public Action createContextAwareInstance(Lookup lkp) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
