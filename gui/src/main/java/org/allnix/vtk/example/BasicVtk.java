package org.allnix.vtk.example;

import javax.swing.JFrame;

import vtk.vtkNativeLibrary;
import vtk.vtkPoints;

public class BasicVtk extends JFrame {

	static {
		if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
			for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
				if (!lib.IsLoaded()) {
					System.out.println(lib.GetLibraryName() + " not loaded");
				}
			}
		}
		vtkNativeLibrary.DisableOutputWindow(null);
	}
	
	public BasicVtk() {
		vtkPoints points = new vtkPoints();
	}
	
}
