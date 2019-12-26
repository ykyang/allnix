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
import vtk.vtkIntersectionPolyDataFilter;
import vtk.vtkLookupTable;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkQuad;
import vtk.vtkTriangle;

/**
 * ./gradlew -PmainClass=org.allnix.vtk.example.PolyDataIntersection runApp
 * @author ykyang
 */
public class PolyDataIntersection {
//    static public void main(String[] args) {
//        VtkLoader.loadAllNativeLibraries();
//		VtkExternalFrame vframe = new VtkExternalFrame();
//        
//        // > Value to show
//		String name = "Temperature";
//		
//		VtkUnstructuredGrid grid = new VtkUnstructuredGrid();
//		grid.init();
//		
//        // (0,20) --- (70,20)
//        // (0,0)  --- (70,0)
//		Builder.buildVtkUnstructuredGrid7Cell(grid, 10, 20, 50);
//		
//		// > Value range to color from blue - red
//		double[] range = grid.getRange(name);
//
//		// > lookup table
////		vtkLookupTable lut = new vtkLookupTable();
////		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
////		lut.SetRange(range);
//		
//        grid.setActiveScalars("Temperature");
//        grid.setLookupTableRange(range);
//        grid.setOpacity(0.3);
//        
//        
//        vtkPolyData polyDataFilter = new vtkPolyData();
//        
////        vtkPolyLine polyLine = new vtkPolyLine();
//        vtkPoints points = new vtkPoints();
////        points.InsertNextPoint(0,0,-1);
////        points.InsertNextPoint(25, 15, -1);
////        points.InsertNextPoint(25, 15, 70);
////        points.InsertNextPoint(0, 0, 70);
//        points.InsertNextPoint(-10,-10,25);
//        points.InsertNextPoint(100, 100, 25);
//        points.InsertNextPoint(100, 100, 25);
//        points.InsertNextPoint(-10, -10, 25);
//        
//        polyDataFilter.SetPoints(points);
//        
//        vtkCellArray polys = new vtkCellArray();
//        
//        vtkQuad quad = new vtkQuad();
//        quad.GetPointIds().SetId(0, 0);
//        quad.GetPointIds().SetId(1, 1);
//        quad.GetPointIds().SetId(2, 2);
//        quad.GetPointIds().SetId(3, 3);
//        
//        vtkTriangle tri = new vtkTriangle();
//        tri.GetPointIds().SetId(0, 0);
//        tri.GetPointIds().SetId(1, 1);
//        tri.GetPointIds().SetId(2, 2);
//        
//        polys.InsertNextCell(quad);
////        polys.InsertNextCell(tri);
//        
//        polyDataFilter.SetPolys(polys);
//        
//        
//        
//        vtkIntersectionPolyDataFilter intersectionPolyDataFilter =
//            new vtkIntersectionPolyDataFilter();
//        intersectionPolyDataFilter.SetInputData(0, grid.getUnstructuredGrid());
//        intersectionPolyDataFilter.SetInputData(1, polyDataFilter);
////        intersectionPolyDataFilter.SetInputConnection( 0, sphereSource1.GetOutputPort() );
////        intersectionPolyDataFilter.SetInputConnection( 1, sphereSource2.GetOutputPort() );
//        intersectionPolyDataFilter.Update();
//
//        vtkPolyDataMapper intersectionMapper = new vtkPolyDataMapper();
//        intersectionMapper.SetInputConnection( intersectionPolyDataFilter.GetOutputPort() );
//        intersectionMapper.ScalarVisibilityOff();
//        intersectionMapper.SetLookupTable(grid.getLookupTable());
//        intersectionMapper.UseLookupTableScalarRangeOn();
//        intersectionMapper.SetScalarModeToUseCellData();
//        
//        vtkActor planeActor = new vtkActor();
//        planeActor.SetMapper(intersectionMapper);
//        
//        
//        vframe.getSceneProp().showAxesActor();
//        VtkRenderer renderer = vframe.getVtkRenderer();
//        
//        renderer.addActor(grid.getActor());
//        renderer.addActor(planeActor);
//        
//        renderer.resetCamera();
//        
//        vframe.setVisible(true);
//        
////        renderer.render();
////        TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
//        renderer.render();
//    }
}
