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
		view.init();
		
		Builder.buildUnstructuredGrid7Cell(view);
				
		view.setActiveScalars("Pressure");
		view.getMapper().SetUseLookupTableScalarRange(1);;
		
		//view.getMapper().SetScalarRange(1000, 2000);
		vtkLookupTable lut = new vtkLookupTable();
		//lut.SetHueRange(0, 0.6667); // red, low -> blue, high
		lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
		
		view.getMapper().SetLookupTable(lut);
		lut.SetRange(view.getRange("Pressure"));
		
		
		vtkScalarBarActor scalarBar = new vtkScalarBarActor();
		scalarBar.SetLookupTable(lut);
		scalarBar.GetLabelTextProperty().SetFontSize(2); // no effect???
		
		// > VTK/Examples/Python/Widgets/ScalarBarWidget
		vtkScalarBarWidget scalarWidget = new vtkScalarBarWidget();
		scalarWidget.SetScalarBarActor(scalarBar);
		
		vtkScalarBarRepresentation rep = scalarWidget.GetScalarBarRepresentation();
		rep.SetOrientation(0); // 0 = Horizontal, 1 = Vertical
		rep.SetPosition(0.1, 0.1);
		rep.SetPosition2(0.4,0.2);
		
		
		vtkPlane plane = new vtkPlane();
		plane.SetOrigin(5,7.5,1);
		plane.SetNormal(0,0,1);
		
		vtkCutter cutter = new vtkCutter();
		cutter.SetCutFunction(plane);
		cutter.SetInputData(view.getMapper().GetInput());
		cutter.Update();
		
		vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
		cutterMapper.SetInputConnection(cutter.GetOutputPort());
		cutterMapper.SetLookupTable(lut);
		cutterMapper.UseLookupTableScalarRangeOn();
		
//		cutterMapper.SetScalarRange(1000,2000);
		
		vtkActor planeActor = new vtkActor();
		planeActor.SetMapper(cutterMapper);
		
		
		VtkFrame vframe = new VtkFrame();
		scalarWidget.SetInteractor(vframe.getVtkRenderWindowPanel().getRenderWindowInteractor());
		
		
		vframe.pack();
		

		vframe.addActor(planeActor);
//		vframe.addActor(scalarBar);
		vframe.resetCamera();
		vframe.render();
		vframe.setVisible(true);
//		vframe.pack();
		vframe.render();
		scalarWidget.On();
		
		
		
//		vtkLookupTable lut = new vtkLookupTable();
//		lut.SetRange(1000, 2000);
//		//lut.SetValueRange(1000, 2000); // no good???
//		lut.SetHueRange(0.66667, 0); //  blue -> low, red -> high
//		lut.Build();
//		logger.info("LookupTable: {}", lut.Print());
//		view.getMapper().SetLookupTable(lut);
//		cutterMapper.SetLookupTable(lut);
//
//		
//		
//		
//		VtkFrame vframe = new VtkFrame();
//		
//		vtkRenderer renderer = vframe.getVtkRenderWindowPanel().GetRenderer(); 
////		renderer.AddActor(view.getActor());
//		renderer.AddActor(planeActor);
//		renderer.ResetCamera();
//		vframe.getVtkRenderWindowPanel().Render();
	}
}
