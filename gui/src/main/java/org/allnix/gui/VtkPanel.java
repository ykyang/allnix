package org.allnix.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkUnstructuredGrid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkProp;
import vtk.vtkRenderWindowPanel;

/**
 * General purpose
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class VtkPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(VtkPanel.class);
	private vtkRenderWindowPanel winPanel;
	
	public VtkPanel() {
		winPanel = new vtkRenderWindowPanel();  
		winPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		winPanel.setPreferredSize(new Dimension(600,600));
		
		add(winPanel, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(600,600));
	}
	
	public vtkRenderWindowPanel getVtkRenderWindowPanel() {
		return winPanel;
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
	
//	public void pack() {
//		Runnable x = ()->{
//			this.pack();
//		};
//		invokeAndWait(x);
//	}
	
	public void render() {
		Runnable x = ()->{
			winPanel.Render();
		};
		invokeAndWait(x);
	}
//	public void setVisible(boolean value) {
//		Runnable x = ()->{
//			this.setVisible(value);
//		};
//		invokeAndWait(x);
//	}
	
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
	
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		VtkPanel vpanel = new VtkPanel();
		
		String name = "Temperature";
		
//		{
//			Object obj = new Object() {
//				protected void finalize() {
//					logger.info("Finalize");
//				}
//			};
//		}
		
		
		VtkUnstructuredGrid me = new VtkUnstructuredGrid();
		me.init();
		
		Builder.buildVtkUnstructuredGrid7Cell(me);
		
		double[] range = me.getRange(name);
		
		me.setActiveScalars("Temperature");
		me.setLookupTableRange(range);
		
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(vpanel, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(600,600));
		frame.setVisible(true);
		frame.pack();
		
		vpanel.addActor(me.getActor());
		vpanel.render();
		vpanel.setVisible(true);
		vpanel.render();
		
		
	}
	
	
}
