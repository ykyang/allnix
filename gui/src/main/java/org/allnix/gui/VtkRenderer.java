package org.allnix.gui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.exception.ExceptionUtils;

import vtk.vtkProp;
import vtk.vtkRenderer;

public interface VtkRenderer {
	void render();
	vtkRenderer getRenderer();
	void addActor(vtkProp v);
	void removeActor(vtkProp v);
	void resetCamera();
	
	default void invokeAndWait(Runnable v) {
		try {
			SwingUtilities.invokeAndWait(v);
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	default void invokeLater(Runnable v) {
		SwingUtilities.invokeLater(v);
	}
}
