package org.allnix.gui.example;

import javax.swing.SwingUtilities;

import org.allnix.gui.Builder;
import org.allnix.gui.VtkFrame;
import org.allnix.gui.VtkGeomodel3DView;
import org.allnix.gui.VtkLoader;

import vtk.vtkBorderRepresentation;
import vtk.vtkCellData;
import vtk.vtkDataArray;
import vtk.vtkDoubleArray;
import vtk.vtkLookupTable;
import vtk.vtkScalarBarActor;
import vtk.vtkScalarBarRepresentation;
import vtk.vtkScalarBarWidget;
import vtk.vtkWidgetRepresentation;

public class LookupTable {

	static public void main(String[] args) {
		
			VtkLoader.loadAllNativeLibraries();
			
			VtkGeomodel3DView view = new VtkGeomodel3DView();
			view.init();
			
			Builder.buildUnstructuredGrid7Cell(view);
			
			view.setActiveScalars("Temperature");
//			view.setScalarRange(10, 20);
			view.getMapper().UseLookupTableScalarRangeOn();
			
			vtkLookupTable lut = new vtkLookupTable();
			//lut.SetHueRange(0, 0.6667); // red, low -> blue, high
			lut.SetHueRange(0.6667, 0.); // blue, low -> red, high
			//lut.SetHueRange(0.7777, 0.); // purple, low -> red, high
//			lut.SetRange(13,19);

			view.getMapper().SetLookupTable(lut);
			
			vtkCellData cellData = view.getUnstructuredGrid().GetCellData();
			vtkDataArray d = (vtkDataArray) cellData.GetAbstractArray("Temperature");
			double[] minmax = d.GetRange();
			lut.SetRange(minmax);

			
			vtkScalarBarActor scalarBar = new vtkScalarBarActor();
			scalarBar.SetLookupTable(lut);
//			scalarBar.SetOrientationToVertical();
			scalarBar.SetOrientationToHorizontal();
			scalarBar.SetPosition(0.85, 0.1); // lower left corner
			scalarBar.SetPosition2(0.1, 0.3); // width and height
//			scalarBar.SetDragable(1);
			scalarBar.DragableOn();
			scalarBar.GetLabelTextProperty().SetFontSize(2);
			
			// > VTK/Examples/Python/Widgets/ScalarBarWidget
			vtkScalarBarWidget scalarWidget = new vtkScalarBarWidget();
			scalarWidget.SetScalarBarActor(scalarBar);
			
			// > http://vtk.1045678.n5.nabble.com/vtkScalarbarWidget-td2841060.html
			vtkScalarBarRepresentation rep = scalarWidget.GetScalarBarRepresentation();
			rep.SetOrientation(0); // 0 = Horizontal, 1 = Vertical
			rep.SetPosition(0.1, 0.1);
			rep.SetPosition2(0.4,0.2);
			
			
			
			VtkFrame vframe = new VtkFrame();
			scalarWidget.SetInteractor(vframe.getVtkRenderWindowPanel().getRenderWindowInteractor());
			
			
			vframe.pack();
			

			vframe.addActor(view.getActor());
//			vframe.addActor(scalarBar);
			vframe.resetCamera();
			vframe.render();
			vframe.setVisible(true);
//			vframe.pack();
			vframe.render();
			scalarWidget.On();
			
	}
}
