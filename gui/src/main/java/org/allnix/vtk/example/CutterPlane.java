package org.allnix.vtk.example;

import java.util.concurrent.TimeUnit;

import org.allnix.gui.Builder;
import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderer;
import org.allnix.vtk.VtkUnstructuredGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkActor;
import vtk.vtkCutter;
import vtk.vtkLookupTable;
import vtk.vtkPolyDataMapper;
import vtk.vtkPlane;
import vtk.vtkPolyData;

public class CutterPlane {
	static final private Logger logger = LoggerFactory.getLogger(CutterPlane.class);
	/**
	 * ./gradlew -PmainClass=org.allnix.vtk.example.CutterPlane runApp
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	static public void main(String[] args) throws InterruptedException {
		VtkLoader.loadAllNativeLibraries();
		VtkExternalFrame vframe = new VtkExternalFrame();
		
		// > Value to show
		String name = "Temperature";
		
		VtkUnstructuredGrid grid = new VtkUnstructuredGrid();
		grid.init();
		
		Builder.buildVtkUnstructuredGrid7Cell(grid);
		
		// > Value range to color from blue - red
		double[] range = grid.getRange(name);

		// > lookup table
		vtkLookupTable lut = new vtkLookupTable();
		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
		lut.SetRange(range);
		
		vtkPlane plane = new vtkPlane();
		plane.SetOrigin(5,7.5,1);
		plane.SetNormal(0,0,1);
		
		vtkCutter cutter = new vtkCutter();
		cutter.SetCutFunction(plane);
		vtkPolyData polyData;
		polyData = cutter.GetOutput();
		//logger.info(polyData.Print());
		
		// > purposely put setinput after getPolyData
		cutter.SetInputData(grid.getUnstructuredGrid());
		// > the polydata before and after Update() is the same
		// > object
		// > polyData = cutter.GetOutput(); 
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
		cutterMapper.SetInputData(polyData); // decouple from underlying grid
		cutterMapper.SetLookupTable(lut);
		cutterMapper.UseLookupTableScalarRangeOn();
		cutterMapper.SetScalarModeToUseCellData();
		
		vtkActor planeActor = new vtkActor();
		planeActor.SetMapper(cutterMapper);
		
		cutter.Update();
		polyData.GetCellData().SetActiveScalars(name);
		
        vframe.getSceneProp().showAxesActor();
        
        VtkRenderer renderer = vframe.getVtkRenderer();
		// > render
//		vframe.pack();
		renderer.addActor(planeActor);
        renderer.resetCamera();
		vframe.setVisible(true);
		renderer.render();
		
		TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
		renderer.render();
		
		TimeUnit.MILLISECONDS.sleep(1500);
		// this should not interfere with the display color
		grid.setActiveScalars("Pressure"); 
		renderer.render();
		
		TimeUnit.MILLISECONDS.sleep(1500);
		plane.SetNormal(0,1,0);
		cutter.Update();
		polyData.GetCellData().SetActiveScalars(name); // STAR
        
		renderer.render();
	}
	
}
