package org.allnix.gui.example;

import java.util.concurrent.TimeUnit;

import org.allnix.gui.Builder;
import org.allnix.gui.VtkFrame;
import org.allnix.gui.VtkLoader;
import org.allnix.gui.VtkUnstructuredGrid;

import vtk.vtkActor;
import vtk.vtkCutter;
import vtk.vtkLookupTable;
import vtk.vtkPolyDataMapper;
import vtk.vtkPlane;

public class CutterPlane {

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
		grid.setActiveScalars("Temperature");
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
		cutter.Update();
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
		cutterMapper.SetInputConnection(cutter.GetOutputPort());
		cutterMapper.SetLookupTable(lut);
		cutterMapper.UseLookupTableScalarRangeOn();
		
		vtkActor planeActor = new vtkActor();
		planeActor.SetMapper(cutterMapper);
		
		// > render
		vframe.pack();
		vframe.addActor(planeActor);
		vframe.setVisible(true);
		vframe.render();
		TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
		vframe.render();
	}
}
