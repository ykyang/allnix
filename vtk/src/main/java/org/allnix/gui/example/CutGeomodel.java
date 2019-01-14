package org.allnix.gui.example;

import org.allnix.gui.Builder;
import org.allnix.gui.VtkFrame;
import org.allnix.gui.VtkGeomodel3DView;
import org.allnix.gui.VtkLoader;

import vtk.*;

public class CutGeomodel {

	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		
		VtkGeomodel3DView view = new VtkGeomodel3DView();
		Builder.buildUnstructuredGrid2Cell(view);
		
		
		
		view.setScalars("Pressure");
		view.getMapper().SetScalarRange(1000, 2000);
		
		vtkPlane plane = new vtkPlane();
		plane.SetOrigin(5,7.5,1);
		plane.SetNormal(0,0,1);
		
		vtkCutter cutter = new vtkCutter();
		cutter.SetCutFunction(plane);
		cutter.SetInputData(view.getMapper().GetInput());
		cutter.Update();
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
		cutterMapper.SetInputConnection(cutter.GetOutputPort());
		cutterMapper.SetScalarRange(1000,2000);
		
		vtkActor planeActor = new vtkActor();
		planeActor.SetMapper(cutterMapper);
		
		
		
		
		
		
		VtkFrame vframe = new VtkFrame();
		
		vtkRenderer renderer = vframe.getVtkRenderWindowPanel().GetRenderer(); 
//		renderer.AddActor(view.getActor());
		renderer.AddActor(planeActor);
		renderer.ResetCamera();
		vframe.getVtkRenderWindowPanel().Render();
	}
}
