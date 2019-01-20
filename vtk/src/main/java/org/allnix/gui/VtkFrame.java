package org.allnix.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.*;

public class VtkFrame {
	static private Logger logger = LoggerFactory.getLogger(VtkFrame.class);
	
	private vtkRenderWindowPanel winPanel;
	private JFrame frame;
	
	public VtkFrame() {
		winPanel = new vtkRenderWindowPanel();  
		winPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		//winPanel.GetRenderer().AddActor(actor);
		//winPanel.GetRenderer().SetBackground(color);
		winPanel.setPreferredSize(new Dimension(600,600));
	
		frame = new JFrame();
		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(winPanel, BorderLayout.CENTER);
		
//		frame.pack();
//		frame.setVisible(true);
	}
	
	public vtkRenderWindowPanel getVtkRenderWindowPanel() {
		return winPanel;
	}
	 
	public JFrame getJFrame() {
		return frame;
	}

	public void addActor(vtkProp v) {
		invokeAndWait(() -> {
			winPanel.GetRenderer().AddActor(v);
		});
	}
	
	public void removeActor(vtkProp v) {
		invokeAndWait(() -> {
			winPanel.GetRenderer().RemoveActor(v);
		});
	}
	
	public void resetCamera() {
		this.invokeAndWait(()->{
			winPanel.GetRenderer().ResetCamera();
		});
	}
	
	public void pack() {
		Runnable x = ()->{
			frame.pack();
		};
		invokeAndWait(x);
	}
	
	public void render() {
		Runnable x = ()->{
			winPanel.Render();
		};
		invokeAndWait(x);
	}
	public void setVisible(boolean value) {
		Runnable x = ()->{
			frame.setVisible(value);
		};
		invokeAndWait(x);
	}
	
	private void invokeAndWait(Runnable v) {
//		invokeLater(v);
		try {
			SwingUtilities.invokeAndWait(v);
		} catch (InvocationTargetException | InterruptedException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}
	
	private void invokeLater(Runnable v) {
		SwingUtilities.invokeLater(v);
	}
}

