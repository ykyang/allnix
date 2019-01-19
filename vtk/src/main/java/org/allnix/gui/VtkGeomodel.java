package org.allnix.gui;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkThreshold;
import vtk.vtkUnstructuredGrid;

/**
 * Geomodel
 * 
 * Include the base grid and the capabilities to slice
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
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
//		threshold.ThresholdBetween(3, 5);
		threshold.SetInputArrayToProcess(0, 0, 0, 
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
	public void setSliceDirection(String dir) {
		
	}
	
	static public void main(String[] args) throws InterruptedException {
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
	
		String name = "Temperature";
		
		VtkUnstructuredGrid me = new VtkUnstructuredGrid();
		me.init();
		
		Builder.buildVtkUnstructuredGrid7Cell(me);
		
		double[] range = me.getRange(name);
		
		me.setActiveScalars("Temperature");
		me.setLookupTableRange(range);
	
		vframe.pack();
		vframe.addActor(me.getActor());
//		vframe.render();
		vframe.setVisible(true);
		TimeUnit.MILLISECONDS.sleep(500);
		vframe.render();
	}
}
