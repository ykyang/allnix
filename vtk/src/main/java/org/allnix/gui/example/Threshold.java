package org.allnix.gui.example;

import org.allnix.gui.Builder;
import org.allnix.gui.VtkFrame;
import org.allnix.gui.VtkGeomodel3DView;
import org.allnix.gui.VtkLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkLookupTable;
import vtk.vtkThreshold;
import vtk.vtkUnstructuredGrid;
/**
 * https://lorensen.github.io/VTKExamples/site/Cxx/PolyData/ThresholdCells/
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class Threshold {
	static final private Logger logger = LoggerFactory.getLogger(Threshold.class); 
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		
		VtkGeomodel3DView view = new VtkGeomodel3DView();
		view.init();
		
		Builder.buildUnstructuredGrid7Cell(view);
		
		view.setActiveScalars("Temperature");
		view.getMapper().UseLookupTableScalarRangeOn();
		vtkLookupTable lut = new vtkLookupTable();
		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
		view.getMapper().SetLookupTable(lut);
		lut.SetRange(view.getRange("Temperature"));
		
		
		vtkThreshold threshold = new vtkThreshold();
		// > Use SetInputConnection() ???
		threshold.SetInputData(view.getUnstructuredGrid());
		threshold.ThresholdBetween(3, 5);
		threshold.SetInputArrayToProcess(0,  0, 0, 
				1, // vtkDataObject.FIELD_ASSOCIATION_CELLS
				"i_index" // field name
				);
		threshold.Update();
		
		vtkUnstructuredGrid thresholdUGrid = threshold.GetOutput();
		logger.info(thresholdUGrid.Print());
		VtkGeomodel3DView thresholdView = new VtkGeomodel3DView();
		thresholdView.setUnstructuredGrid(thresholdUGrid);
		thresholdView.init();
		thresholdView.setActiveScalars("Temperature");
		thresholdView.getMapper().UseLookupTableScalarRangeOn();
		thresholdView.getMapper().SetLookupTable(lut);
		
		
		VtkFrame vframe = new VtkFrame();
		
		vframe.pack();
		
//		vframe.addActor(view.getActor());
		logger.info(view.getActor().Print());
		logger.info(thresholdView.getActor().Print());
		vframe.addActor(thresholdView.getActor());
		
		vframe.resetCamera();
		vframe.render();
		vframe.setVisible(true);
//		vframe.pack();
		vframe.render();
	}

}
