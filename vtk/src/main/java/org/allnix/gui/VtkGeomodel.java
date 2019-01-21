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
		// TODO: how do i know the naming convention
		vtk.setCellInputArrayToProcess(dir + "_index");
		vtk.update();
		
		this.vsvc.putVtkThreshold(name, vtk);
	}
	
	public void setIJKSliceActiveScalar(String sliceName, String name) {
		VtkThreshold threshold = vsvc.getVtkThreshold(sliceName);
		VtkUnstructuredGrid vugrid = threshold.getOutput();
		vugrid.setActiveScalars(name);
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
		
		// > expose renderer
		VtkRenderWindowPanelRenderer renderer = new VtkRenderWindowPanelRenderer();
		renderer.setVtk(vframe.getVtkRenderWindowPanel());
		
		String name = "Temperature";
		
		VtkGeomodel me = new VtkGeomodel();
		
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
		vframe.setVisible(true);

		TimeUnit.MILLISECONDS.sleep(500);
		renderer.render();

		TimeUnit.MILLISECONDS.sleep(1500);
		me.setIJKSliceThresholdBetween(sliceName, 4, 5);
		me.setIJKSliceActiveScalar(sliceName, name);
		renderer.render();

		TimeUnit.MILLISECONDS.sleep(1500);
		me.hideIJKSlice(sliceName);
		renderer.render();

		TimeUnit.MILLISECONDS.sleep(1500);
		me.setIJKSliceThresholdBetween(sliceName, 1, 7);
		me.setIJKSliceActiveScalar(sliceName, name);
		me.showIJKSlice(sliceName);
		renderer.render();
		
	}
	
}
