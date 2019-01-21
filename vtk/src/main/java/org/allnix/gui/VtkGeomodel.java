package org.allnix.gui;

import java.rmi.UnexpectedException;
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
		threshold.ThresholdBetween(1, 1);
		threshold.SetInputArrayToProcess(0, 0, 0, 
				1, // vtkDataObject.FIELD_ASSOCIATION_CELLS
				dir + "_index" //field name
				//"i_index" // field name
				);
		threshold.Update();
		
//		vtkUnstructuredGrid grid = threshold.GetOutput();
//		
		VtkUnstructuredGrid augrid = new VtkUnstructuredGrid();
		augrid.setUnstructuredGrid(threshold.GetOutput());
////		wgrid.setLookupTable(mainGrid.getLookupTable());
		augrid.init();
		
		Object[] objz = this.ijkDb.get(name);
		objz[0] = augrid;
		objz[1] = threshold;
	}
	public void setIJKSliceActiveScalar(String sliceName, String name) {
		Object[] objz = this.ijkDb.get(sliceName);
		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0];
		vugrid.setActiveScalars(name);
	}
	public void showIJKSlice(String name) {
		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0];
		renderer.addActor(vugrid.getActor());
	}
	public void hideIJKSlice(String name) {
		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0];
		renderer.removeActor(vugrid.getActor());
	}
	public void setIJKSliceThresholdColorRange(String name, double[] minmax) {
		Object[] objz = this.ijkDb.get(name);
		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0]; // this is stupid
		vugrid.setLookupTableRange(minmax);
	}
	
	public void setIJKSliceThresholdBetween(String name, double min, double max) {
		Object[] objz = this.ijkDb.get(name);
		
//		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0];
//		if (vugrid != null) {
//		  vtkUnstructuredGrid oldGrid = vugrid.getUnstructuredGrid();
//		}
		vtkThreshold threshold = (vtkThreshold) objz[1]; // stupid
		
		
		threshold.ThresholdBetween(min, max);
		threshold.Update();
		// > after the Update(), threshold returns the same grid object as before
		// vtkUnstructuredGrid newGrid = threshold.GetOutput();
		
//		logger.info("oldGrid: {}", oldGrid);
//		logger.info("newGrid: {}", newGrid);
		
		// remove old actor
		
		VtkUnstructuredGrid vugrid = (VtkUnstructuredGrid) objz[0];
		
//		if (vugrid == null) {
//			vugrid = new VtkUnstructuredGrid();
//			vugrid.setUnstructuredGrid(threshold.GetOutput());
//			vugrid.init();
//			vugrid.setActiveScalars("Temperature"); // TODO: where do we get this from
//			
//			objz[0] = vugrid;
//			
//			frame.addActor(vugrid.getActor());
//		} else {
//			if (vugrid.getUnstructuredGrid() != threshold.GetOutput()) {
//				throw new RuntimeException("The two vtkUnstructuredGrid are expected to be the same");
//			}
//		}
		// > reset active scalar name
		vugrid.setActiveScalars(vugrid.getActiveScalarName());
//		vugrid.getMapper().SetInputData(vugrid.getUnstructuredGrid());
//		vugrid.getMapper().Update();
		
//		vugrid.getUnstructuredGrid().GetCellData().Modified();
//		vugrid.getUnstructuredGrid().Modified();
//		vugrid.getActor().Modified();
		
//		vugrid.getMapper().StaticOff();
//		vugrid.getMapper().SetInputData(vugrid.getUnstructuredGrid());
		
//		if (vugrid != null) {
//			frame.removeActor(vugrid.getActor());
//			vtkUnstructuredGrid oldGrid = vugrid.getUnstructuredGrid();
//			vtkUnstructuredGrid newGrid = threshold.GetOutput();
//			logger.info("oldGrid: {}", oldGrid);
//			logger.info("newGrid: {}", newGrid);
//		}
				
//		vugrid = new VtkUnstructuredGrid();
//		vugrid.setUnstructuredGrid(threshold.GetOutput());
//		vugrid.init();
//		vugrid.setActiveScalars("Temperature"); // TODO: where do we get this from
//		
//		
//		
//		objz[0] = vugrid;
////		objz[1] = threshold;
//		
//		frame.addActor(vugrid.getActor());
//		// TODO: add new actor
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
