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
	
//	private Map<String, VtkUnstructuredGrid> ijkGridDb;
	/**
	 * Object[0]: VtkUnstructuredGrid
	 * Object[1]: vtkThreshold
	 */
//	private Map<String, Object[]> ijkDb;
	private EmbeddedVtkService vsvc;
	/**
	 * Map ijk slice name to ID in VtkService
	 */
//	private Map<String, String> ijkMap;
	private VtkRenderer renderer;
	
//	private VtkFrame frame;
	
//	public void setVtkFrame(VtkFrame frame) {
//		this.frame = frame;
//	}
	public void setVtkRenderer(VtkRenderer v) {
		renderer = v;
	}
	
	public void init() {
		mainGrid = new VtkUnstructuredGrid();
		mainGrid.init();
		if (vsvc == null) {
			vsvc = new EmbeddedVtkService();
		}
//		ijkDb = LazyMap.lazyMap(new HashMap<String, Object[]>(), new Factory<Object[]>() {
//			@Override
//			public Object[] create() {
//				return new Object[3];
//			}
//		});
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
		VtkThreshold vtk = new VtkThreshold();
		vtk.init();
		vtk.setInputData(mainGrid.getUnstructuredGrid());
		vtk.setThresholdBetween(1, 1);
		vtk.setCellInputArrayToProcess(dir + "_index");
		vtk.update();
		
//		vtkThreshold threshold = new vtkThreshold();
//		threshold.SetInputData(mainGrid.getUnstructuredGrid());
//		threshold.ThresholdBetween(1, 1);
//		threshold.SetInputArrayToProcess(0, 0, 0, 
//				1, // vtkDataObject.FIELD_ASSOCIATION_CELLS
//				dir + "_index" //field name
//				//"i_index" // field name
//				);
//		threshold.Update();
		
//		VtkUnstructuredGrid augrid = new VtkUnstructuredGrid();
//		augrid.setUnstructuredGrid(threshold.GetOutput());
//		augrid.init();
		
		
		this.vsvc.putVtkThreshold(name, vtk);
//		Object[] objz = this.ijkDb.get(name);
//		objz[0] = augrid;
//		objz[1] = threshold;
	}
	public void setIJKSliceActiveScalar(String sliceName, String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(sliceName);
		
//		Object[] objz = this.ijkDb.get(sliceName);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		vugrid.setActiveScalars(name);
	}
	public void showIJKSlice(String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		
//		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		renderer.addActor(vugrid.getActor());
	}
	public void hideIJKSlice(String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		
//		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		renderer.removeActor(vugrid.getActor());
	}
	public void setIJKSliceThresholdColorRange(String name, double[] minmax) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		
//		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = threshold.getOutput();		
		
		vugrid.setLookupTableRange(minmax);
	}
	
	public void setIJKSliceThresholdBetween(String name, double min, double max) {
		// Object[] objz = this.ijkDb.get(name);
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		threshold.setThresholdBetween(min, max);
		threshold.update();
//		vtkThreshold threshold = (vtkThreshold) objz[1]; // stupid
		
		
//		threshold.ThresholdBetween(min, max);
//		threshold.Update();
		// > after the Update(), threshold returns the same grid object as before
		// vtkUnstructuredGrid newGrid = threshold.GetOutput();
		
//		logger.info("oldGrid: {}", oldGrid);
//		logger.info("newGrid: {}", newGrid);
		
		// remove old actor
		
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		
		// > reset active scalar name
		vugrid.setActiveScalars(vugrid.getActiveScalarName());
	}
	
	static public void main(String[] args) throws InterruptedException {
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
		
		// > expose renderer
		VtkRenderWindowPanelRenderer renderer = new VtkRenderWindowPanelRenderer();
		renderer.setVtk(vframe.getVtkRenderWindowPanel());
		
		String name = "Temperature";
		
		VtkGeomodel me = new VtkGeomodel();
		
//		me.setVtkFrame(vframe);
		me.setVtkRenderer(renderer);
		me.init();
		
		
		Builder.buildVtkUnstructuredGrid7Cell(me.getGrid());
		
		double[] range = me.getGrid().getRange(name);
		
		String sliceName = "x-slice 1";
		me.createIJKSlice(sliceName, "i");
		
		me.setIJKSliceThresholdBetween(sliceName, 1, 5);
		me.setIJKSliceThresholdColorRange(sliceName, range);
		me.setIJKSliceActiveScalar(sliceName, name);
		me.showIJKSlice(sliceName);
		
		
		vframe.pack();
//		vframe.addActor(me.getActor());
//		vframe.render();
		vframe.setVisible(true);
		TimeUnit.MILLISECONDS.sleep(500);
		renderer.render();
		TimeUnit.MILLISECONDS.sleep(1500);
		me.setIJKSliceThresholdBetween(sliceName, 4, 5);
//		me.setIJKSliceThresholdColorRange(sliceName, range);
		me.setIJKSliceActiveScalar(sliceName, name);
		renderer.render();
		TimeUnit.MILLISECONDS.sleep(1500);
		me.hideIJKSlice(sliceName);
		renderer.render();
		TimeUnit.MILLISECONDS.sleep(1500);
		me.setIJKSliceThresholdBetween(sliceName, 1, 7);
//		me.setIJKSliceThresholdColorRange(sliceName, range);
		me.setIJKSliceActiveScalar(sliceName, name);
		me.showIJKSlice(sliceName);
		renderer.render();
		
	}
	
}
