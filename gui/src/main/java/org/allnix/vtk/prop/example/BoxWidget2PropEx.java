package org.allnix.vtk.prop.example;

import org.allnix.vtk.prop.BoxWidget2Prop;
import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;

import vtk.vtkActor;
import vtk.vtkBoxRepresentation;
import vtk.vtkBoxWidget2;
import vtk.vtkConeSource;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;

public class BoxWidget2PropEx {
    /**
     * <pre>
     * ./gradlew -PmainClass=org.allnix.vtk.prop.example.BoxWidget2PropEx runApp
     * </pre>
     */
    static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
        VtkExternalFrame vframe = new VtkExternalFrame();
        VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
        
        vtkConeSource coneSource = new vtkConeSource();
        coneSource.SetHeight(1.5);
        
        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
        mapper.SetInputConnection(coneSource.GetOutputPort());
        
        vtkActor actor = new vtkActor();
        actor.SetMapper(mapper);
        
        renderer.addActor(actor);
        renderer.resetCamera();
        
        BoxWidget2Prop prop = new BoxWidget2Prop();
        prop.setVtkRenderer(renderer);
        prop.setOutlineColor(new double[] {0.1, 0.1, 0.1});
        prop.placeWidget(actor.GetBounds());
        prop.on();
        
//        prop.translate(new double[] {0.75, 0, 0});
        prop.rotateZ(15);
        
        
        vframe.setVisible(true);
        vframe.getVtkRenderer().setBackground(0.95, 0.95, 0.95);
        renderer.render();
        
    }
}
