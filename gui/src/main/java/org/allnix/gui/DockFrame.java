package org.allnix.gui;

import javax.swing.JFrame;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkRenderWindowPanel;

public class DockFrame {
	/**
	 * https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
	 * 
	 * frame.pack();
	 * frame.setLocationRelativeTo(null);
	 * frame.setVisible(true);
	 */
	private JFrame frame;
	private vtkRenderWindowPanel renPanel;
	private CControl cctrl;
	
	public DockFrame() {
		frame = new JFrame("Docking Frame");
		frame.setBounds(100, 100, 600, 400);
		
		cctrl = new CControl(frame);
		frame.add(cctrl.getContentArea());
		
		
		renPanel = new vtkRenderWindowPanel();
		renPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		
		DefaultSingleCDockable dock = null;
		
		dock = new DefaultSingleCDockable("vtk", "3D");
		cctrl.addDockable(dock);
		dock.add(renPanel);
		dock.setLocation(CLocation.base().normalWest(1.0));
		dock.setVisible(true);
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	/**
	 * ./gradlew -PmainClass=org.allnix.gui.DockFrame runApp
	 * @param args
	 */
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		
		DockFrame dframe = new DockFrame();
		
		
		
		
		JFrame frame = dframe.getJFrame();
		frame.setSize(600,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}
