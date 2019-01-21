package org.allnix.gui;

import vtk.vtkProp;
import vtk.vtkRenderWindowPanel;
import vtk.vtkRenderer;

public class VtkRenderWindowPanelRenderer implements VtkRenderer {
	private vtkRenderWindowPanel vtk;
	
	/**
	 * Set the wrapped vtk object
	 * 
	 * @param vtk
	 */
	public void setVtk(vtkRenderWindowPanel vtk) {
		this.vtk = vtk;
	}
	
	@Override
	public void render() {
		Runnable x = () -> {
			vtk.Render();
		};
		invokeAndWait(x);
	}
	
	public vtkRenderer getRenderer() {
		return vtk.GetRenderer();
	}
	
	public void addActor(vtkProp v) {
		Runnable x = () -> {
			getRenderer().AddActor(v);
		};
		invokeAndWait(x);
	}
	
	public void removeActor(vtkProp v) {
		Runnable x = () -> {
			getRenderer().RemoveActor(v);
		};
		invokeAndWait(x);
	}

	
}
