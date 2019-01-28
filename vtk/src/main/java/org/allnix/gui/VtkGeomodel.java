package org.allnix.gui;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	// > should I put this in VtkService?
	private VtkUnstructuredGrid mainGrid;
	/**
	 * How to handle global name conflict
	 */
	private EmbeddedVtkService vsvc;
	/**
	 * Shared renderer among actor contributors
	 */
	private VtkRenderer renderer;
	
	public void setVtkRenderer(VtkRenderer v) {
		renderer = v;
	}
	
	public VtkRenderer getVtkRenderer() {
		return renderer;
	}
	
	public void init() {
		mainGrid = new VtkUnstructuredGrid();
		mainGrid.init();
		if (vsvc == null) {
			vsvc = new EmbeddedVtkService();
		}
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
	 * Convenient method
	 * 
	 * @param name
	 */
	public void setGridActiveScalar(String name) {
		mainGrid.setActiveScalars(name);
	}
	
	public void setGridLookupTableRange(double[] range) {
		mainGrid.setLookupTableRange(range);
	}
	
	public void showGrid() {
		renderer.addActor(mainGrid.getActor());
	}
	
	public void hideGrid() {
		renderer.removeActor(mainGrid.getActor());
	}
	
	/**
	 * 
	 * @param name
	 * @param dir Slice direction: "i", "j", "k"
	 */
	public VtkThreshold createIJKSlice(String name, String dir) {
		// TODO: Check existing name
		
		// TODO: Check i,j,k
		VtkThreshold threshold = new VtkThreshold();
		threshold.init();
		threshold.setInputData(mainGrid.getUnstructuredGrid());
		threshold.setThresholdBetween(1, 1);
		// TODO: how do i know the naming convention
		threshold.setCellInputArrayToProcess(dir + "_index");
		threshold.update();
		
		this.vsvc.putVtkThreshold(name, threshold);
		
		return threshold;
	}
	
	public VtkPlaneCutter createSlice(String name) {
		VtkPlaneCutter slice = new VtkPlaneCutter();
		slice.init();
		slice.setInputData(mainGrid.getUnstructuredGrid());
		
		vsvc.putVtkPlaneCutter(name, slice);
		
		return slice;
	}
	
	public VtkPlaneCutter getSlice(String name) {
		return vsvc.getVtkPlaneCutter(name);
	}
	
	public VtkThreshold getIJKSlice(String sliceName) {
		VtkThreshold threshold = vsvc.getVtkThreshold(sliceName);
		return threshold;
	}
	
	public void setIJKSliceActiveScalar(String sliceName, String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(sliceName);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		vugrid.setActiveScalars(name);
	}
	
	public void showSlice(String name) {
		VtkPlaneCutter slice = vsvc.getVtkPlaneCutter(name);
		renderer.addActor(slice.getActor());
	}
	
	public void hideSlice(String name) {
		VtkPlaneCutter slice = vsvc.getVtkPlaneCutter(name);
		renderer.removeActor(slice.getActor());
	}
	
	public void showIJKSlice(String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		renderer.addActor(vugrid.getActor());
	}
	public void hideIJKSlice(String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		renderer.removeActor(vugrid.getActor());
	}
	public void setIJKSliceThresholdColorRange(String name, double[] minmax) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		VtkUnstructuredGrid vugrid = threshold.getOutput();		
		vugrid.setLookupTableRange(minmax);
	}
	
	public void setIJKSliceThresholdBetween(String name, double min, double max) {
		VtkThreshold threshold = vsvc.getVtkThreshold(name);
		threshold.setThresholdBetween(min, max);
		threshold.update();
		
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		
		// > looks like this is necessary
		// > otherwise shows white cells
		// > reset active scalar name
		vugrid.setActiveScalars(vugrid.getActiveScalarName());
	}
	
	static public void main(String[] args) throws InterruptedException {
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
		
		// > Create renderer
		VtkRenderWindowPanelRenderer renderer = new VtkRenderWindowPanelRenderer();
		renderer.setImplementation(vframe.getVtkRenderWindowPanel());
		
		String name = "Temperature";
		
		VtkGeomodel me = new VtkGeomodel();
		
		me.setVtkRenderer(renderer);
		me.init();
		
		
		Builder.buildVtkUnstructuredGrid7Cell(me.getGrid());
		
		double[] range = me.getGrid().getRange(name);
		
		String sliceName = "x-slice 1";
		me.createIJKSlice(sliceName, "i");
		me.createSlice(sliceName);
		
		
		me.setIJKSliceThresholdBetween(sliceName, 1, 5);
		me.setIJKSliceThresholdColorRange(sliceName, range);
		me.setIJKSliceActiveScalar(sliceName, name);
		//me.showIJKSlice(sliceName);
			
		vframe.pack();
		vframe.setVisible(true);
		
		renderer.render();

		VtkPlaneCutter slice;
		
		
		// > show whole grid
		TimeUnit.MILLISECONDS.sleep(500);
		me.setGridActiveScalar(name);
		me.setGridLookupTableRange(range);
		me.showGrid();
		renderer.resetCamera();
		renderer.render();

		TimeUnit.MILLISECONDS.sleep(1500);
		me.hideGrid();
		me.setIJKSliceThresholdBetween(sliceName, 3, 5);
		me.setIJKSliceActiveScalar(sliceName, name);
		me.showIJKSlice(sliceName);
		renderer.render();

		TimeUnit.MILLISECONDS.sleep(1500);
		me.hideIJKSlice(sliceName);
		renderer.render();

		TimeUnit.MILLISECONDS.sleep(1500);
		me.setIJKSliceThresholdBetween(sliceName, 1, 7);
		me.setIJKSliceActiveScalar(sliceName, name);
		me.showIJKSlice(sliceName);
		renderer.render();
		
		TimeUnit.MILLISECONDS.sleep(1500);
		me.hideIJKSlice(sliceName);
		slice = me.getSlice(sliceName);
		slice.setOrigin(35, 7.5, 1);
		slice.setNormal(0, 0, 1);
		slice.setLookupTableRange(range);
		slice.update();
		slice.setActiveScalars(name);
		me.showSlice(sliceName);
	
		renderer.render();
		
		
	}
	
}
