package org.allnix.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import vtk.*;

public class VtkFrame {
	private vtkRenderWindowPanel winPanel;
	private JFrame frame;
	
	public VtkFrame() {
		winPanel = new vtkRenderWindowPanel();  
		winPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		//winPanel.GetRenderer().AddActor(actor);
		//winPanel.GetRenderer().SetBackground(color);
		winPanel.setPreferredSize(new Dimension(600,600));
		
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(winPanel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public vtkRenderWindowPanel getVtkRenderWindowPanel() {
		return winPanel;
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	
}
