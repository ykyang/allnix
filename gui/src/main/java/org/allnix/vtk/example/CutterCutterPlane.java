package org.allnix.vtk.example;

import java.util.concurrent.TimeUnit;

import org.allnix.gui.Builder;
import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderer;
import org.allnix.vtk.VtkUnstructuredGrid;

import vtk.vtkActor;
import vtk.vtkClipPolyData;
import vtk.vtkCutter;
import vtk.vtkLookupTable;
import vtk.vtkPlane;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;


/**
 * Cut a slice out of a volume using Cutter then use 2 Clipper to clip
 * the lengh of the slice.
 * 
 * ./gradlew -PmainClass=org.allnix.vtk.example.CutterCutterPlane runApp
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class CutterCutterPlane {
	/**
	 * ./gradlew -PmainClass=org.allnix.vtk.example.CutterCutterPlane runApp
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
		
		
		Builder.buildVtkUnstructuredGrid7Cell(grid, 10, 50, 50);
		
		grid.setOpacity(0.2);
		grid.setActiveScalars("Temperature");
		// > Value range to color from blue - red
		double[] range = grid.getRange(name);
//		grid.setAutoRange(name);
		grid.setLookupTableRange(range);
		// > lookup table
//		vtkLookupTable lut = new vtkLookupTable();
//		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
//		lut.SetRange(range);
		
		vtkPlane plane = new vtkPlane();
		plane.SetOrigin(35,25,1);
		plane.SetNormal(1,1,0);
		
		// Cut a plane out of volume
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
		
		
		// Toe cutter
		vtkPlane toePlane = new vtkPlane();
		toePlane.SetOrigin(55, 7.5, 1);
		toePlane.SetNormal(1, 0, 0);
		
		vtkClipPolyData toeClipper = new vtkClipPolyData();
		toeClipper.SetClipFunction(toePlane);
		toeClipper.SetInputConnection(cutter.GetOutputPort());
		toeClipper.InsideOutOn();
		
		
		// Heel cutter
		vtkPlane heelPlane = new vtkPlane();
		heelPlane.SetOrigin(22, 7.5, 1);
		heelPlane.SetNormal(1, 0, 0);
		
		vtkClipPolyData heelClipper = new vtkClipPolyData();
		heelClipper.SetClipFunction(heelPlane);
		heelClipper.SetInputConnection(toeClipper.GetOutputPort());
		heelClipper.InsideOutOff();
		
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
//		cutterMapper.SetInputData(polyData); // decouple from underlying grid
//		cutterMapper.SetInputConnection(cutter.GetOutputPort());
		//cutterMapper.SetInputConnection(toeClipper.GetOutputPort());
		cutterMapper.SetInputConnection(heelClipper.GetOutputPort());
		cutterMapper.SetLookupTable(grid.getLookupTable());
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
		renderer.addActor(grid.getActor());
        renderer.resetCamera();
		vframe.setVisible(true);
		renderer.render();
		
//		TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
//		renderer.render();
//		
//		TimeUnit.MILLISECONDS.sleep(1500);
//		// this should not interfere with the display color
//		grid.setActiveScalars("Pressure"); 
//		renderer.render();
//		
//		TimeUnit.MILLISECONDS.sleep(1500);
//		plane.SetNormal(0,1,0);
//		cutter.Update();
//		polyData.GetCellData().SetActiveScalars(name); // STAR
        
//		renderer.render();
	}
}
