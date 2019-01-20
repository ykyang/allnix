package org.allnix.gui;

import vtk.vtkRenderer;

public class VtkRenderer {
	private vtkRenderer renderer;

	public vtkRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(vtkRenderer renderer) {
		this.renderer = renderer;
	}
	public void render() {
		renderer.Render();
	}
}
