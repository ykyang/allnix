package org.allnix.vtk.example;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import vtk.*;

public class PolyLine {
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
	public static void main(String[] args) {
		vtkNamedColors colors = new vtkNamedColors();

		vtkPoints points = new vtkPoints();
		
		double[] origin = {0, 0, 0};
		double[] p0 = {1, 0, 0};
		double[] p1 = {0, 1, 0};
		double[] p2 = {0, 1, 2};
		double[] p3 = {1, 2, 3}; 
		points.InsertNextPoint(origin);
		points.InsertNextPoint(p0);
		points.InsertNextPoint(p1);
		points.InsertNextPoint(p2);
		points.InsertNextPoint(p3);
		
		vtkPolyLine polyLine = new vtkPolyLine();
		polyLine.GetPointIds().SetNumberOfIds(5);
		for (int ind = 1; ind < 5; ind++) {
			polyLine.GetPointIds().SetId(ind, ind);
		}
	
		vtkCellArray cells = new vtkCellArray();
		cells.InsertNextCell(polyLine);
		
		vtkPolyData polyData = new vtkPolyData();
		polyData.SetPoints(points);
		
		polyData.SetLines(cells);
		
		vtkPolyDataMapper mapper = new vtkPolyDataMapper();
		mapper.SetInputData(polyData);
		
		vtkActor actor = new vtkActor();
		actor.SetMapper(mapper);
		double[] color = new double[4];
		colors.GetColor("Tomato", color);
		actor.GetProperty().SetColor(color);

		color = new double[4];
		colors.GetColor("DarkOliveGreen", color);
		
		
		vtkRenderWindowPanel winPanel = new vtkRenderWindowPanel();  
		winPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
		winPanel.GetRenderer().AddActor(actor);
		winPanel.GetRenderer().SetBackground(color);
		winPanel.setPreferredSize(new Dimension(600,600));
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(winPanel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		winPanel.Render();
		
		
//		vtkRenderer renderer = new vtkRenderer();
//		vtkRenderWindow renWin = new vtkRenderWindow();
//		renWin.SetWindowName("Poly Line");
//		renWin.AddRenderer(renderer);
//		vtkRenderWindowInteractor renWinInt = new vtkRenderWindowInteractor();
//		renWinInt.SetRenderWindow(renWin);
//		renderer.AddActor(actor);
//		
//		color = new double[4];
//		colors.GetColor("DarkOliveGreen", color);
//		renderer.SetBackground(color);
//		
//		renWin.Render();
//		renWinInt.Start();
//	    renWinInt.Initialize();
		
	}
}
