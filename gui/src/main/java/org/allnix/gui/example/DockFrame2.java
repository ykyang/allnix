package org.allnix.gui.example;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.allnix.vtk.VtkLoader;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import vtk.vtkCanvas;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkPanel;
import vtk.vtkRenderWindowPanel;

public class DockFrame2 {
	/**
	 * https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
	 * 
	 * frame.pack();
	 * frame.setLocationRelativeTo(null);
	 * frame.setVisible(true);
	 */
	private JFrame frame;
	private vtkRenderWindowPanel renPanel;
	private vtkCanvas vcanvas;
	private vtkPanel vpanel;
	private CControl cctrl;
	
	public DockFrame2() {
		frame = new JFrame("Docking Frame");
		frame.setBounds(100, 100, 600, 400);
		
		cctrl = new CControl(frame);
		frame.add(cctrl.getContentArea());
		
		
		renPanel = new vtkRenderWindowPanel();
//		renPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		
		vcanvas = new vtkCanvas();
		vpanel = new vtkPanel();
		
		
		CGrid cgrid = new CGrid(cctrl);
		
		DefaultSingleCDockable dock = null;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 10));
		panel.add(new Canvas());
		
//		JInternalFrame iframe = new JInternalFrame(null, true);
		JInternalFrame iframe = new JInternalFrame(null, false, false, false, false);
//		iframe.setLayout(new BorderLayout());
		iframe.add(renPanel);
		iframe.setBorder(BorderFactory.createLineBorder(Color.PINK));
		iframe.setVisible(true);
		
		dock = new DefaultSingleCDockable("vtk", "3D View");
		cctrl.addDockable(dock);
		cgrid.add(0, 0, 1, 1, dock);
		dock.add(iframe);
//		dock.add(panel);
//		dock.add(vpanel);
//		dock.add(renPanel);
//		dock.setLocation(CLocation.base().normalWest(1.0));
		dock.setVisible(true);
		
		dock = new DefaultSingleCDockable("control", "Control");
		cctrl.addDockable(dock);
		cgrid.add(1, 0, 1, 1, dock);
		dock.add(new JPanel());
		dock.setVisible(true);
		
		cctrl.getContentArea().deploy(cgrid);
		
	}
	
	public JFrame getJFrame() {
		return frame;
	}
	
	/**
	 * ./gradlew -PmainClass=org.allnix.gui.example.DockFrame2 runApp
	 * @param args
	 */
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		
		DockFrame2 dframe = new DockFrame2();
		
		
		
		
		JFrame frame = dframe.getJFrame();
		frame.setSize(600,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}
