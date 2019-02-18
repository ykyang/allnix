package org.allnix.vtk;

import java.util.concurrent.TimeUnit;

import org.allnix.gui.Builder;

import vtk.vtkActor;
import vtk.vtkCutter;
import vtk.vtkDataObject;
import vtk.vtkLookupTable;
import vtk.vtkPlane;
import vtk.vtkPolyDataMapper;

public class VtkPlaneCutter {
	private vtkPlane plane;
	private vtkCutter cutter;
	private vtkActor actor;
	private vtkPolyDataMapper mapper;
	private vtkLookupTable lut;
	
	private String activeScalarName;
	
	public void init() {
		plane = new vtkPlane();
		plane.SetOrigin(0,0,0);
		plane.SetNormal(0,0,1);
		
		cutter = new vtkCutter();
		cutter.SetCutFunction(plane);
		
		if (lut == null) {
			lut = new vtkLookupTable();
			// > blue, low -> red, high
			// > flip the number to inverse the color
			lut.SetHueRange(0.6667, 0.);
			lut.Build();
		}
		
		mapper = new vtkPolyDataMapper();
		mapper.SetLookupTable(lut);
		mapper.UseLookupTableScalarRangeOn();
		mapper.SetScalarModeToUseCellData();
		
		// > This one will link mapper color to
		// > underlying cutter.SetInput(vtkDataObject)
		// mapper.SetInputConnection(cutter.GetOutputPort());
		
		mapper.SetInputData(cutter.GetOutput());
		
		actor = new vtkActor();
		actor.SetMapper(mapper);
	}
	
	public vtkActor getActor() {
		return actor;
	}
	
	public void setActiveScalars(String name) {
		cutter.GetOutput().GetCellData().SetActiveScalars(name);
		this.activeScalarName = name;
	}
	
	public void setLookupTableRange(double[] minmax) {
		lut.SetRange(minmax);
		lut.Build();
	}
	
	public void setOrigin(double x, double y, double z) {
		plane.SetOrigin(x,y,z);
	}
	
	public void setNormal(double x, double y, double z) {
		plane.SetNormal(x, y, z);
	}
	
	public void update() {
		cutter.Update();
	}
	
	public void setInputData(vtkDataObject input) {
		cutter.SetInputData(input);
	}
	
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
		
		// > Slice
		VtkPlaneCutter slice = new VtkPlaneCutter();
		slice.init();
		slice.setInputData(grid.getUnstructuredGrid());
		
		slice.setLookupTableRange(range);
//		slice.setOrigin(0, 0, 1); // right on the boundary does not work
		slice.setOrigin(0.01, 0.01, 0.01);
		slice.setNormal(0, 0, 1);
		
		slice.update();
		slice.setActiveScalars(name);
		
		// > render
		vframe.pack();
		vframe.addActor(slice.getActor());
		vframe.setVisible(true);
		vframe.render();
		
		TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
		vframe.render();
		
		TimeUnit.MILLISECONDS.sleep(1500);
		slice.setNormal(0,1,0);
		slice.update();
		slice.setActiveScalars(name); // STAR
		vframe.render();
	}
}
