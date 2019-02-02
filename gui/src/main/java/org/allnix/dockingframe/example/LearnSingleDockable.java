package org.allnix.dockingframe.example;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

public class LearnSingleDockable {
	/**
	 * ./gradlew -PmainClass=org.allnix.dockingframe.example.LearnSingleDockable runApp
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Learn Single Dockable");
		frame.setBounds(100, 100, 600, 400);
		CControl cctrl = new CControl(frame);
		frame.add(cctrl.getContentArea());
		
		DefaultSingleCDockable dock = null;
		String id;
		String title;
		JPanel panel;
		Color color;
		
		// Yellow
		id = "yellow";
		title = "Yellow";
		color = Color.ORANGE;
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(color);
		
		dock = new DefaultSingleCDockable(id, title);
		cctrl.addDockable(dock);
		dock.add(panel);
		dock.setLocation(CLocation.base().normalWest(1));
		dock.setVisible(true);
		
		
		frame.setVisible(true);
	}
}
