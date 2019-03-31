package org.allnix.miglayout.example;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LearnMigLayout {

	static public void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
//		frame.setLayout(new MigLayout("fill"));
		frame.add(panel);
		
		
		MigLayout layout = new MigLayout("wrap 2", "[][grow]");
//		MigLayout layout = new MigLayout();
		panel.setLayout(layout);
		
		panel.add(newLabel("Button 1"));
		panel.add(new JTextField("Well A"), "growx");
//		panel.add(newLabel("A Text Field"), "growx");
//		panel.add(new JButton("Button 3"), "growx");
//		panel.add(new JButton("Button 4"));
//		panel.add(new JButton("Button 4"));
//		panel.add(new JButton("Button 4"));
//		panel.add(new JButton("Button 4"));
		
		frame.setSize(300,300);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	static private JLabel newLabel(String text) {
		JLabel label = new JLabel(text, JLabel.CENTER);
        label.setBorder(BorderFactory.createEtchedBorder());

        return label;
	}
}
