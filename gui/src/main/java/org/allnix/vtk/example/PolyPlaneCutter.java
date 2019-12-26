/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.vtk.example;

import org.allnix.gui.Builder;
import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderer;
import org.allnix.vtk.VtkUnstructuredGrid;
import vtk.vtkActor;
import vtk.vtkCellArray;
import vtk.vtkCutter;
import vtk.vtkLookupTable;
import vtk.vtkPlane;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkPolyLine;
import vtk.vtkPolyPlane;

/**
 * ./gradlew -PmainClass=org.allnix.vtk.example.PolyPlaneCutter runApp
 * 
 * @author ykyang
 */
public class PolyPlaneCutter {
    static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
		VtkExternalFrame vframe = new VtkExternalFrame();
        
        // > Value to show
		String name = "Temperature";
		
		VtkUnstructuredGrid grid = new VtkUnstructuredGrid();
		grid.init();
		
        // (0,20) --- (70,20)
        // (0,0)  --- (70,0)
		Builder.buildVtkUnstructuredGrid7Cell(grid, 20, 20, 50);
		
		// > Value range to color from blue - red
		double[] range = grid.getRange(name);

		// > lookup table
		vtkLookupTable lut = new vtkLookupTable();
		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
		lut.SetRange(range);
		
        vtkPolyLine polyLine = new vtkPolyLine();
        vtkPoints points = polyLine.GetPoints();//new vtkPoints();
//        points.SetNumberOfPoints(2);
//        points.SetPoint(0, 140, 0, 25);
//        points.SetPoint(1, 120, 5, 25);
//        points.InsertNextPoint(20,0,0);
        points.InsertNextPoint(140,0,25);
//        points.InsertNextPoint(12.5,7.5,0);
//        points.InsertNextPoint(120, 5, 25);
        //points.InsertNextPoint(10, 10, 0);
//        points.InsertNextPoint(55, 0, 0);
        points.InsertNextPoint(70, 10, 0);
        
        System.out.println(polyLine.toString());
//        polyLine.GetPointIds().SetNumberOfIds(3);
//        polyLine.GetPointIds().SetId(0, 0);
//        polyLine.GetPointIds().SetId(1, 1);
//        polyLine.GetPointIds().SetId(2, 2);
//        polyLine.
//        vtkCellArray cells = new vtkCellArray();
//		cells.InsertNextCell(polyLine);
//		
//		vtkPolyData polyData = new vtkPolyData();
//		polyData.SetPoints(points);
		
//		polyData.SetLines(cells);
        
//		vtkPlane plane = new vtkPlane();
//		plane.SetOrigin(5,7.5,1);
//		plane.SetNormal(0,1,0);

        vtkPolyPlane polyPlane = new vtkPolyPlane();
        polyPlane.SetPolyLine(polyLine);
		
		vtkCutter cutter = new vtkCutter();
		cutter.SetCutFunction(polyPlane);
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
//        polyData.GetPointData().SetActiveScalars(name);		

        vframe.getSceneProp().showAxesActor();
        
        VtkRenderer renderer = vframe.getVtkRenderer();
		// > render
//		vframe.pack();
         // > Value range to color from blue - red
        
        grid.setActiveScalars("Temperature");
        grid.setLookupTableRange(range);
        grid.setOpacity(0.3);
        renderer.addActor(grid.getActor());
		renderer.addActor(planeActor);
        renderer.resetCamera();
		vframe.setVisible(true);
		renderer.render();
    }
            
}
