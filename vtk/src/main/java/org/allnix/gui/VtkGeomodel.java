package org.allnix.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkRenderer;
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
	/**
	 * Object[0]: VtkUnstructuredGrid
	 * Object[1]: vtkThreshold
	 */
	private Map<String, Object[]> ijkDb;
//	private VtkRenderer renderer;
	private VtkFrame frame;
	
	public void setVtkFrame(VtkFrame frame) {
		this.frame = frame;
	}
	
	public void init() {
		mainGrid = new VtkUnstructuredGrid();
		mainGrid.init();
		
		ijkDb = LazyMap.lazyMap(new HashMap<String, Object[]>(), new Factory<Object[]>() {
			@Override
			public Object[] create() {
				return new Object[3];
			}
		});
	}
	
	/**
	 * The main grid
	 * 
	 * @return
	 */
	public VtkUnstructuredGrid getGrid() {
		return mainGrid;
	}
	
	/**
	 * 
	 * @param name
	 * @param dir Slice direction: "i", "j", "k"
	 */
	public void createIJKSlice(String name, String dir) {
		// TODO: Check existing name
		
		// TODO: Check i,j,k
		vtkThreshold threshold = new vtkThreshold();
		threshold.SetInputData(mainGrid.getUnstructuredGrid());
//		threshold.ThresholdBetween(3, 5);
		threshold.SetInputArrayToProcess(0, 0, 0, 
				1, // vtkDataObject.FIELD_ASSOCIATION_CELLS
				dir + "_index" //field name
				//"i_index" // field name
				);
		
		
		// useless 
//		vtkUnstructuredGrid grid = threshold.GetOutput();
//		
//		VtkUnstructuredGrid wgrid = new VtkUnstructuredGrid();
//		wgrid.setUnstructuredGrid(grid);
////		wgrid.setLookupTable(mainGrid.getLookupTable());
//		wgrid.init();
		
		Object[] objz = this.ijkDb.get(name);
//		objz[0] = wgrid;
		objz[1] = threshold;
	}
	public void setSliceThresholdColorRange(String name, double[] minmax) {
		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0]; // this is stupid
		vugrid.setLookupTableRange(minmax);
	}
	
	public void setSliceThresholdBetween(String name, double min, double max) {
		Object[] objz = this.ijkDb.get(name);
		vtkThreshold threshold = (vtkThreshold) objz[1];
		threshold.ThresholdBetween(min, max);
		threshold.Update();
		// TODO: remove old actor
		
		
		VtkUnstructuredGrid vugrid = new VtkUnstructuredGrid();
		vugrid.setUnstructuredGrid(threshold.GetOutput());
		vugrid.init();
		vugrid.setActiveScalars("Temperature"); // TODO: where do we get this from
		
		
		
		objz[0] = vugrid;
//		objz[1] = threshold;
		
		frame.addActor(vugrid.getActor());
		// TODO: add new actor
	}
	
	static public void main(String[] args) throws InterruptedException {
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
	
		String name = "Temperature";
		
		VtkGeomodel me = new VtkGeomodel();
		
		me.setVtkFrame(vframe);
		me.init();
		
		
		Builder.buildVtkUnstructuredGrid7Cell(me.getGrid());
		
		double[] range = me.getGrid().getRange(name);
		
		String sliceName = "x-slice 1";
		me.createIJKSlice(sliceName, "i");
		
		me.setSliceThresholdBetween(sliceName, 3, 5);
		me.setSliceThresholdColorRange(sliceName, range);
	
		
		
		vframe.pack();
//		vframe.addActor(me.getActor());
//		vframe.render();
		vframe.setVisible(true);
		TimeUnit.MILLISECONDS.sleep(500);
		vframe.render();
		TimeUnit.MILLISECONDS.sleep(1500);
		me.setSliceThresholdBetween(sliceName, 1, 5);
		me.setSliceThresholdColorRange(sliceName, range);
		vframe.render();
		
	}
	
}
