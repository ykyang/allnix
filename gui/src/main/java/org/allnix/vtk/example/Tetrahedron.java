package org.allnix.vtk.example;

import vtk.*;

public class Tetrahedron {

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
		vtkNamedColors colors = new vtkNamedColors();
		double[] color;
		
		vtkPoints points = new vtkPoints();
		points.InsertNextPoint(0, 0, 0);
	    points.InsertNextPoint(1, 0, 0);
	    points.InsertNextPoint(1, 1, 0);
	    points.InsertNextPoint(0, 1, 1);
	    points.InsertNextPoint(5, 5, 5);
	    points.InsertNextPoint(6, 5, 5);
	    points.InsertNextPoint(6, 6, 5);
	    points.InsertNextPoint(5, 6, 6);
	    
	    vtkUnstructuredGrid unstGrid1 = new vtkUnstructuredGrid();
	    unstGrid1.SetPoints(points);
	    
	    vtkTetra tetra = new vtkTetra(); 
	    
	    tetra.GetPointIds().SetId(0, 0);
	    tetra.GetPointIds().SetId(1, 1);
	    tetra.GetPointIds().SetId(2, 2);
	    tetra.GetPointIds().SetId(3, 3);

	    vtkCellArray cellArray = new vtkCellArray();
	    cellArray.InsertNextCell(tetra);
	    unstGrid1.SetCells(CellType.TETRA.GetId(), cellArray);
	    
	    vtkUnstructuredGrid unstGrid2 = new vtkUnstructuredGrid();
	    unstGrid2.SetPoints(points);
	    
	    tetra = new vtkTetra();
	    
	    tetra.GetPointIds().SetId(0, 4);
	    tetra.GetPointIds().SetId(1, 5);
	    tetra.GetPointIds().SetId(2, 6);
	    tetra.GetPointIds().SetId(3, 7);
	    
	    cellArray = new vtkCellArray();
	    cellArray.InsertNextCell(tetra);
	    unstGrid2.SetCells(CellType.TETRA.GetId(), cellArray);

	    // Create a mapper and actor
	    vtkDataSetMapper mapper1 = new vtkDataSetMapper();
	    mapper1.SetInputData(unstGrid1);

	    color = new double[4];
	    colors.GetColor("Cyan", color);
	    vtkActor actor1 = new vtkActor();
	    actor1.SetMapper(mapper1);
	    actor1.GetProperty().SetColor(color);

	    // Create a mapper and actor
	    vtkDataSetMapper mapper2 = new vtkDataSetMapper();
	    mapper2.SetInputData(unstGrid2);

	    color = new double[4];
	    colors.GetColor("Yellow", color);
	    vtkActor actor2 = new vtkActor();
	    actor2.SetMapper(mapper2);
	    actor2.GetProperty().SetColor(color);

	    // Create a renderer, render window, and interactor
	    vtkRenderer renderer = new vtkRenderer();
	    vtkRenderWindow renderWindow = new vtkRenderWindow();
	    renderWindow.SetWindowName("Tetrahedron");
	    renderWindow.AddRenderer(renderer);
	    vtkRenderWindowInteractor renderWindowInteractor = new vtkRenderWindowInteractor();
	    renderWindowInteractor.SetRenderWindow(renderWindow);

	    // Add the actor to the scene
	    color = new double[4];
	    colors.GetColor("DarkGreen", color);
	    renderer.AddActor(actor1);
	    renderer.AddActor(actor2);
	    renderer.SetBackground(color);
	    renderer.ResetCamera();
	    renderer.GetActiveCamera().Azimuth(-10);
	    renderer.GetActiveCamera().Elevation(-20);


	    // Render and interact
	    renderWindow.Render();
	    renderWindowInteractor.Start();
	    
	    
	    
	}
}
