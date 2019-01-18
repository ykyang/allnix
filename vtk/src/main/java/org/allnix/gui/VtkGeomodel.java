package org.allnix.gui;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkThreshold;
import vtk.vtkUnstructuredGrid;

public class VtkGeomodel {
	static final private Logger logger = LoggerFactory.getLogger(VtkGeomodel.class);
	
	private VtkUnstructuredGrid mainGrid;
	
	private Map<String, VtkUnstructuredGrid> ijkGridDb;
	private Map<String, Object[]> ijkDb;
	
	public void init() {
		mainGrid = new VtkUnstructuredGrid();
		mainGrid.init();
		
		
	}
	
	/**
	 * The main grid
	 * 
	 * @return
	 */
	public VtkUnstructuredGrid getGrid() {
		return mainGrid;
	}
	
	public void createIJKSlice(String name, String dir) {
		// TODO: Check existing name
		
		vtkThreshold threshold = new vtkThreshold();
		threshold.SetInputData(mainGrid.getUnstructuredGrid());
		threshold.ThresholdBetween(3, 5);
		threshold.SetInputArrayToProcess(0,  0, 0, 
				1, // vtkDataObject.FIELD_ASSOCIATION_CELLS
				dir //field name
				//"i_index" // field name
				);
		
		vtkUnstructuredGrid grid = threshold.GetOutput();
		
		VtkUnstructuredGrid wgrid = new VtkUnstructuredGrid();
		wgrid.setUnstructuredGrid(grid);
		wgrid.init();
		wgrid.getMapper().GetUseLookupTableScalarRange();
		
	}
	
	static public void main(String[] args) {
		VtkFrame vframe = new VtkFrame();
		
	}
}
