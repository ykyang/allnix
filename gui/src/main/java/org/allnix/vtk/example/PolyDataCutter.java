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
import vtk.vtkDoubleArray;
import vtk.vtkElevationFilter;
import vtk.vtkImplicitDataSet;
import vtk.vtkLookupTable;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkPolyLine;
import vtk.vtkPolyPlane;
import vtk.vtkQuad;
import vtk.vtkSphere;
import vtk.vtkTriangle;

/**
 * ./gradlew -PmainClass=org.allnix.vtk.example.PolyDataCutter runApp
 * @author ykyang
 */
public class PolyDataCutter {
    static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
		VtkExternalFrame vframe = new VtkExternalFrame();
        
        // > Value to show
		String name = "Temperature";
		
		VtkUnstructuredGrid grid = new VtkUnstructuredGrid();
		grid.init();
		
        // (0,20) --- (70,20)
        // (0,0)  --- (70,0)
		Builder.buildVtkUnstructuredGrid7Cell(grid, 10, 20, 50);
		
		// > Value range to color from blue - red
		double[] range = grid.getRange(name);

		// > lookup table
		vtkLookupTable lut = new vtkLookupTable();
		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
		lut.SetRange(range);
		
        vtkPolyData polyDataFilter = new vtkPolyData();
        
//        vtkPolyLine polyLine = new vtkPolyLine();
        vtkPoints points = new vtkPoints();
//        points.InsertNextPoint(0,0,-1);
//        points.InsertNextPoint(25, 15, -1);
//        points.InsertNextPoint(25, 15, 70);
//        points.InsertNextPoint(0, 0, 70);
        points.InsertNextPoint(-10,-10,25);
        points.InsertNextPoint(100, 100, 25);
        points.InsertNextPoint(100, 100, 25);
        points.InsertNextPoint(-10, -10, 25);
        
        polyDataFilter.SetPoints(points);
        
        vtkCellArray polys = new vtkCellArray();
        
        vtkQuad quad = new vtkQuad();
        quad.GetPointIds().SetId(0, 0);
        quad.GetPointIds().SetId(1, 1);
        quad.GetPointIds().SetId(2, 2);
        quad.GetPointIds().SetId(3, 3);
        
        vtkTriangle tri = new vtkTriangle();
        tri.GetPointIds().SetId(0, 0);
        tri.GetPointIds().SetId(1, 1);
        tri.GetPointIds().SetId(2, 2);
        
        polys.InsertNextCell(quad);
//        polys.InsertNextCell(tri);
        
        polyDataFilter.SetPolys(polys);
        
        vtkDoubleArray ele = new vtkDoubleArray();
        ele.SetName("Z");
        double[] data = {1, 2, 3, 4};
        ele.SetJavaArray(data);
        polyDataFilter.GetPointData().AddArray(ele);
        polyDataFilter.GetPointData().SetActiveScalars("Z");
        
//        System.out.println(polyDataFilter.toString());
        
//        vtkElevationFilter elev = new vtkElevationFilter();
//        elev.SetInputData(polyDataFilter);
        
        vtkSphere sphere = new vtkSphere();
        sphere.SetCenter(0, 10, 0);
        sphere.SetRadius(50);
        
        
        vtkImplicitDataSet implicit = new vtkImplicitDataSet();
//        implicit.SetDataSet(elev.GetOutput());
        implicit.SetDataSet(polyDataFilter);
       
        
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

//        vtkPolyPlane polyPlane = new vtkPolyPlane();
//        polyPlane.SetPolyLine(polyLine);
//		System.out.println(implicit.toString());
		vtkCutter cutter = new vtkCutter();
		//cutter.SetCutFunction(implicit);
        cutter.SetCutFunction(sphere);
        
        
//		vtkPolyData polyData;
		
		//logger.info(polyData.Print());
		
		// > purposely put setinput after getPolyData
		cutter.SetInputData(grid.getUnstructuredGrid());
        cutter.Update();
//        polyData = cutter.GetOutput();
		// > the polydata before and after Update() is the same
		// > object
		// > polyData = cutter.GetOutput(); 
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
//		cutterMapper.SetInputData(polyData); // decouple from underlying grid
        cutterMapper.SetInputConnection(cutter.GetOutputPort());
		cutterMapper.SetLookupTable(lut);
		cutterMapper.UseLookupTableScalarRangeOn();
		cutterMapper.SetScalarModeToUseCellData();
		
		vtkActor planeActor = new vtkActor();
		planeActor.SetMapper(cutterMapper);
		
		
//		polyData.GetCellData().SetActiveScalars(name);
		
        vframe.getSceneProp().showAxesActor();
        vframe.getSceneProp().showCubeAxesActor();
        vframe.getSceneProp().reset();
        
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
