/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.mymodule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.io.IOProvider;
import org.netbeans.api.io.InputOutput;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
  category = "Edit",
  id = "org.allnix.mymodule.MyFirstAction"
)
@ActionRegistration(
  iconBase = "org/allnix/mymodule/ic_directions_bike_black.png",
  displayName = "#CTL_MyFirstAction"
)
@ActionReferences({
  @ActionReference(path = "Menu/Edit", position = 1200),
  @ActionReference(path = "Toolbars/MyToolbars", position = 100)
})
@Messages("CTL_MyFirstAction=My First Action")
public final class MyFirstAction implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO implement action body
    InputOutput io = IOProvider.getDefault().getIO("Task", true);
    
    // > Open the Output Window
    io.show();
    
    io.getOut().println("Standand output");
    io.getErr().println("Error output");
    
    io.getOut().close();
    io.getErr().close();
  }
}
