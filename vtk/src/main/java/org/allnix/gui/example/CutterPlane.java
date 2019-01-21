package org.allnix.gui.example;

import java.util.concurrent.TimeUnit;

import org.allnix.gui.Builder;
import org.allnix.gui.VtkFrame;
import org.allnix.gui.VtkLoader;
import org.allnix.gui.VtkUnstructuredGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkActor;
import vtk.vtkCutter;
import vtk.vtkDataObject;
import vtk.vtkLookupTable;
import vtk.vtkPolyDataMapper;
import vtk.vtkPlane;
import vtk.vtkPolyData;

public class CutterPlane {
	static final private Logger logger = LoggerFactory.getLogger(CutterPlane.class);
	static public void main(String[] args) throws InterruptedException {
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
		
		// > Value to show
		String name = "Temperature";
		
		VtkUnstructuredGrid grid = new VtkUnstructuredGrid();
		grid.init();
		
		Builder.buildVtkUnstructuredGrid7Cell(grid);
		
		// > Value range to color from blue - red
		double[] range = grid.getRange(name);
		// shows white plane without this line
//		grid.setActiveScalars(name);
		grid.setLookupTableRange(range);

		// > lookup table
		vtkLookupTable lut = new vtkLookupTable();
		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
		lut.SetRange(range);
		
		
		
		vtkPlane plane = new vtkPlane();
		plane.SetOrigin(5,7.5,1);
		plane.SetNormal(0,0,1);
		
		vtkCutter cutter = new vtkCutter();
		cutter.SetCutFunction(plane);
		cutter.SetInputData(grid.getUnstructuredGrid());
		// does not work
//		cutter.SetInputArrayToProcess(0, 0, 0, 1, name);
		// modifiy underlying ugrid
//		vtkDataObject vdo = cutter.GetInput();
//		vdo.GetAttributes(1).SetActiveScalars(name); // cell data
//		cutter.GenerateCutScalarsOn();
		cutter.Update();
		vtkPolyData polyData = cutter.GetOutput();
		logger.info(polyData.Print());
		polyData.GetCellData().SetActiveScalars(name);
//		cutter.Update(); // put update here does not work
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
		cutterMapper.SetInputData(polyData); // decouple from underlying grid
//		cutterMapper.SetInputConnection(cutter.GetOutputPort()); // coupled with underlying grid
		cutterMapper.SetLookupTable(lut);
		cutterMapper.UseLookupTableScalarRangeOn();
		cutterMapper.SetScalarModeToUseCellData();
		//cutterMapper.SetArrayName(name);
		
		vtkActor planeActor = new vtkActor();
		planeActor.SetMapper(cutterMapper);
		
		// > render
		vframe.pack();
		vframe.addActor(planeActor);
		vframe.setVisible(true);
		vframe.render();
		
		TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
		vframe.render();
		
		TimeUnit.MILLISECONDS.sleep(1500);
		grid.setActiveScalars("Pressure"); // still interfere with the slice
		vframe.render();
		
		TimeUnit.MILLISECONDS.sleep(1500);
		plane.SetNormal(0,1,0);
		cutter.Update();
		polyData.GetCellData().SetActiveScalars(name);
		vframe.render();
	}
	
}
