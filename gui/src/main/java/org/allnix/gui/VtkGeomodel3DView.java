package org.allnix.gui;

import javax.swing.SwingUtilities;

import org.allnix.vtk.VtkFrame;
import org.allnix.vtk.VtkLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.*;

/**
 * TODO: need to understand memory management
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class VtkGeomodel3DView extends UnstructuredGrid {
	private vtkActor actor;
	private vtkDataSetMapper mapper;
	
	static final private Logger logger = LoggerFactory.getLogger(VtkGeomodel3DView.class); 

	public VtkGeomodel3DView() {
		// purposely keep it blank
		// still not sure about the implication of
		// AWT thread and object creation
	}
	
	public void init() {
		super.init();
		
		// > create objects
		// > purposely separated from constructor
		mapper = new vtkDataSetMapper();
		actor = new vtkActor();
		
				
		// > Not sure if this is OK
		mapper.SetInputData(this.getUnstructuredGrid());
		mapper.SetScalarModeToUseCellData();
		
		actor.SetMapper(mapper);
	}
	
	public vtkActor getActor() {
		return actor;
	}
	
	public vtkDataSetMapper getMapper() {
		return mapper;
	}
	
	public void setScalarRange(double min, double max) {
		mapper.SetScalarRange(min, max);
	}
	
	/**
	 * Got to find a way to move this out
	 * 
	 * ./gradlew -PmainClass=com.wdvglab.gui.VtkGeomodel3DView runApp
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();

		VtkGeomodel3DView view = new VtkGeomodel3DView();

		view.init();

		Builder.buildUnstructuredGrid2Cell(view);

		// > write to XML for debugging
		view.writeXML("geomodel.vtu");

//		view.setActiveScalars("Pressure");
//		view.setScalarRange(1000, 2000);

		view.setActiveScalars("Temperature");
		view.setScalarRange(10, 20);

		VtkFrame vframe = new VtkFrame();
//		vframe.pack();
		

		vframe.addActor(view.getActor());
		vframe.resetCamera();
		
		vframe.setVisible(true);
		vframe.pack();
		vframe.render();
	}
}
