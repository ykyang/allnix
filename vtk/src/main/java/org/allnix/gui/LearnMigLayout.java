package org.allnix.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class LearnMigLayout {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Learn MigLayout");
		frame.setSize(400, 400);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		
		frame.getContentPane().add(panel);
		
		
		JButton but1 = new JButton("Button 1");
		panel.add(but1);
		JButton but2 = new JButton("Button 2");
		panel.add(but2, "span 2 1");
		JButton but3 = new JButton("Button 3");
		panel.add(but3, "wrap");
		//panel.add(but3, "wrap push");
		
		JButton but4 = new JButton("Button 4");
		panel.add(but4, "span 4");
		
		frame.setVisible(true);
	}
}
