package org.allnix.gui.example;

import org.allnix.gui.Builder;
import org.allnix.gui.VtkFrame;
import org.allnix.gui.VtkGeomodel3DView;
import org.allnix.gui.VtkLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.*;

public class CutGeomodel {
	static private Logger logger = LoggerFactory.getLogger(CutGeomodel.class);
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
		
		
		
		
		vtkLookupTable lut = new vtkLookupTable();
		lut.SetRange(1000, 2000);
		//lut.SetValueRange(1000, 2000); // no good???
		lut.SetHueRange(0.66667, 0); //  blue -> low, red -> high
		lut.Build();
		logger.info("LookupTable: {}", lut.Print());
		
		view.getMapper().SetLookupTable(lut);
		cutterMapper.SetLookupTable(lut);

		
		
		
		VtkFrame vframe = new VtkFrame();
		
		vtkRenderer renderer = vframe.getVtkRenderWindowPanel().GetRenderer(); 
//		renderer.AddActor(view.getActor());
		renderer.AddActor(planeActor);
		renderer.ResetCamera();
		vframe.getVtkRenderWindowPanel().Render();
	}
}
