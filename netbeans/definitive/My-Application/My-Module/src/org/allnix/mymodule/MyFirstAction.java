/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.mymodule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
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
@ActionReference(path = "Menu/Edit", position = 1200)
@Messages("CTL_MyFirstAction=My First Action")
public final class MyFirstAction implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO implement action body
  }
}
