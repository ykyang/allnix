package org.allnix.gui;

import java.util.concurrent.TimeUnit;

import vtk.vtkDataObject;
import vtk.vtkThreshold;

public class VtkThreshold {
	// > Main vtk object
	private vtkThreshold vtk;
	
	// > TODO: Should its output bundled here?
	// > so far makes sense
	private VtkUnstructuredGrid output;
	
	public void init() {
		vtk = new vtkThreshold();
	}
	
	public VtkUnstructuredGrid getOutput() {
		if (output == null) { // lazy initialization
			output = new VtkUnstructuredGrid();
			output.setUnstructuredGrid(vtk.GetOutput());
			output.init();
		}
		
		return output;
	}
	
	/**
	 * Get the wrapped vtkThreshold
	 * 
	 * @return
	 */
	public vtkThreshold getVtkThreshold() {
		return vtk;
	}
	
	public void setThresholdBetween(double min, double max) {
		vtk.ThresholdBetween(min, max);
	}
	
	/**
	 * Set which cell data to use for thresholding
	 * 
	 * @param name
	 */
	public void setCellInputArrayToProcess(String name) {
		vtk.SetInputArrayToProcess(0, 0, 0, 
				1, // vtkDataObject.FIELD_ASSOCIATION_CELLS
				name);
	}
	
	public void setInputData(vtkDataObject inputData) {
		vtk.SetInputData(inputData);
	}
	
	public void update() {
		vtk.Update();
	}
	
	static public void main(String[] args) throws InterruptedException {
		// > Basic setup
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
				
		String name = "Temperature";
		
		VtkUnstructuredGrid ugrid = new VtkUnstructuredGrid();
		ugrid.init();
		
		Builder.buildVtkUnstructuredGrid7Cell(ugrid);

		ugrid.setActiveScalars("Temperature");
		
		double[] range = ugrid.getRange(name);
		ugrid.setLookupTableRange(range);
		
		// > Threshold
		VtkThreshold me = new VtkThreshold();
		me.init();
		me.setInputData(ugrid.getUnstructuredGrid());
		me.setCellInputArrayToProcess("i_index");
		me.setThresholdBetween(3, 5);
		me.update();
		
		VtkUnstructuredGrid out = me.getOutput();
		out.setActiveScalars(name);
		out.setLookupTableRange(range);
		
		vframe.pack();
		vframe.addActor(out.getActor());
		vframe.setVisible(true);
		vframe.render();
		TimeUnit.MILLISECONDS.sleep(500);
		vframe.render();
	}
}
