package org.allnix.vtk.example;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import vtk.*;

public class UnstructuredGrid {
	static 
	  {
	    if (!vtkNativeLibrary.LoadAllNativeLibraries()) 
	    {
	      for (vtkNativeLibrary lib : vtkNativeLibrary.values()) 
	      {
	        if (!lib.IsLoaded()) 
	        {
	          System.out.println(lib.GetLibraryName() + " not loaded");
	        }
	      }
	    }
	    vtkNativeLibrary.DisableOutputWindow(null);
	  }
	
	static public void main(String[] args) {
		// > Points
		vtkPoints points = new vtkPoints();
		points.InsertNextPoint(0,0,0);
		points.InsertNextPoint(1,0,0);
		points.InsertNextPoint(1,1,0);
		points.InsertNextPoint(0,1,0);
		points.InsertNextPoint(0,0,1.75);
		points.InsertNextPoint(1,0,1.75);
		points.InsertNextPoint(1,1,1.75);
		points.InsertNextPoint(0,1,1.75);
		
		vtkUnstructuredGrid ugrid = new vtkUnstructuredGrid();
		ugrid.SetPoints(points);
		
		// > Cells
		vtkHexahedron hex = new vtkHexahedron();
		vtkIdList l = hex.GetPointIds();
		l.SetId(0, 0);
		l.SetId(1, 1);
		l.SetId(2, 2);
		l.SetId(3, 3);
		l.SetId(4, 4);
		l.SetId(5, 5);
		l.SetId(6, 6);
		l.SetId(7, 7);
		
		vtkCellArray cellz = new vtkCellArray();
		cellz.InsertNextCell(hex);
		
		ugrid.SetCells(CellType.HEXAHEDRON.GetId(), cellz);
		
		// > Cell data
		{
			vtkDoubleArray v = new vtkDoubleArray(); 
			v.InsertNextValue(13);
			vtkCellData cellData = ugrid.GetCellData();
			cellData.SetScalars(v);
		}
		
		// > point data
		{
			vtkDoubleArray v = new vtkDoubleArray();
			v.InsertNextValue(2);
			v.InsertNextValue(3);
			v.InsertNextValue(5);
			v.InsertNextValue(7);
//			v.InsertNextValue(11);
//			v.InsertNextValue(13);
//			v.InsertNextValue(17);
//			v.InsertNextValue(19);
			
			v.InsertNextValue(2);
			v.InsertNextValue(3);
			v.InsertNextValue(5);
			v.InsertNextValue(7);
			
			ugrid.GetPointData().SetScalars(v);
		}
		
		// > Mapper
		vtkDataSetMapper mapper = new vtkDataSetMapper();
		mapper.SetInputData(ugrid);
		
		// > comment out to switch between 
		// > cell data and point data
		//mapper.SetScalarModeToUseCellData();
		
		mapper.SetScalarModeToUsePointData();
		mapper.SetScalarRange(2, 7);
		//mapper.CreateDefaultLookupTable();
		
		// > Actor
		vtkActor actor = new vtkActor();
		actor.SetMapper(mapper);
	
		vtkRenderWindowPanel winPanel = new vtkRenderWindowPanel();  
		winPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		winPanel.GetRenderer().AddActor(actor);
		//winPanel.GetRenderer().SetBackground(color);
		winPanel.setPreferredSize(new Dimension(600,600));
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(winPanel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		winPanel.Render();
		
	}
}
