package org.allnix.miglayout.example;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.allnix.swing.ColorChooserButton;
import org.allnix.swing.ColorChooserButton.ColorChangedListener;
import org.slf4j.Logger;
/**
 * ./gradlew -PmainClass=org.allnix.miglayout.example.ControlPanel runApp
 * @author ykyang
 *
 */
import org.slf4j.LoggerFactory;
public class ControlPanel {
	static final private Logger logger = LoggerFactory.getLogger(ControlPanel.class);
	
	static public void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel rootPanel = new JPanel();
		rootPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		
		
		MigLayout layout = new MigLayout("wrap");
		rootPanel.setLayout(layout);
		
		JPanel panel;
		JCheckBox checkBox;
		JLabel label;
		JButton button;
		JTextField text;
		
		

		// >>> Well
		panel = new JPanel();
		rootPanel.add(panel, "growx, pushx");
		layout = new MigLayout("wrap 3");
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createTitledBorder("3D View"));

//		checkBox = new JCheckBox("Well:");
		label = new JLabel("Well IDs:");
		button = new JButton("Draw");
		text = new JTextField("1:end");
		text.setToolTipText("Display laterals, fracturs and proppants.");
		panel.add(label);
		panel.add(button);
		panel.add(text, "growx, pushx");
		
		
//		label = new JLabel("Lateral:");
		checkBox = new JCheckBox("Well Path:");
		button = new JButton("Draw");
		text = new JTextField("1:end");
		panel.add(checkBox);
		panel.add(button);
		panel.add(text, "growx, pushx");
		
//		label = new JLabel("Fracture:");
		checkBox = new JCheckBox("Fracture:");
		button = new JButton("Draw");
		text = new JTextField("1:end");
		panel.add(checkBox);
		panel.add(button);
		panel.add(text, "growx, pushx");
		
//		label = new JLabel("Proppant:");
		checkBox = new JCheckBox("Proppant:");
		button = new JButton("Draw");
		text = new JTextField("1:end");
		panel.add(checkBox);
		panel.add(button);
		panel.add(text, "growx, pushx");
		
		
		// >>> Property
		panel = new JPanel();
		rootPanel.add(panel, "growx, pushx");
		layout = new MigLayout("wrap 2");
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		label = new JLabel("Well Path:");
		text = new JTextField("1:end");
		panel.add(label);
		panel.add(text, "growx, pushx");
		
		label = new JLabel("Fracture:");
		text = new JTextField("1:end");
		panel.add(label);
		panel.add(text, "growx, pushx");
		
		label = new JLabel("Proppant:");
		text = new JTextField("1:end");
		panel.add(label);
		panel.add(text, "growx, pushx");
		
		
		
		
		label = new JLabel("Opacity:");
		text = new JTextField("1.0");
		panel.add(label);
		panel.add(text, "growx, pushx");
		
		label = new JLabel("Ambient:");
		text = new JTextField("0.7");
		panel.add(label);
		panel.add(text, "growx, pushx");
		
		
		label = new JLabel("Color:");
		ColorChooserButton colorChooser = new ColorChooserButton(Color.WHITE);
		panel.add(label);
		panel.add(colorChooser);
		colorChooser.addColorChangedListener(new ColorChangedListener() {
		    @Override
		    public void colorChanged(Color newColor) {
		    	logger.info("Color: {}", newColor.toString());
		            // do something with newColor ...
		    }
		});
		
		
		
		
//		panel = new JPanel();
//		panel.setBorder(BorderFactory.createTitledBorder("Stage"));
//		rootPanel.add(panel, "growx, pushx");
		
		
		
		JScrollPane scrollPane = new JScrollPane(rootPanel);
		frame.add(scrollPane);
		frame.setSize(600, 400);
		frame.setVisible(true);
	
		
	}
}
