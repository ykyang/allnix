package org.allnix.vtk.example;

import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;
import vtk.vtkActor;
import vtk.vtkIntersectionPolyDataFilter;
import vtk.vtkPolyDataMapper;
import vtk.vtkSphereSource;


/**
 * https://lorensen.github.io/VTKExamples/site/Cxx/PolyData/IntersectionPolyDataFilter/
 * 
 * <pre>
 * ./gradlew -PmainClass=org.allnix.vtk.example.IntersectionPolyDataFilter runApp
 * </pre>
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class IntersectionPolyDataFilter {

    static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
        
        vtkSphereSource sphereSource1 = new vtkSphereSource();
        sphereSource1.SetCenter(0, 0, 0);
        sphereSource1.SetRadius(2);
        sphereSource1.Update();

        vtkPolyDataMapper sphere1Mapper = new vtkPolyDataMapper();
        sphere1Mapper.SetInputConnection(sphereSource1.GetOutputPort());
//        sphere1Mapper.ScalarVisibilityOff();
        sphere1Mapper.ScalarVisibilityOn();
        vtkActor sphere1Actor = new vtkActor();
        sphere1Actor.SetMapper( sphere1Mapper );
        sphere1Actor.GetProperty().SetOpacity(.3);
        sphere1Actor.GetProperty().SetColor(1,0,0);
        
        vtkSphereSource sphereSource2 = new vtkSphereSource();
        sphereSource2.SetCenter(1.0, 0.0, 0.0);
        sphereSource2.SetRadius(2.0);
        vtkPolyDataMapper sphere2Mapper = new vtkPolyDataMapper();
        sphere2Mapper.SetInputConnection( sphereSource2.GetOutputPort() );
        sphere2Mapper.ScalarVisibilityOff();
        vtkActor sphere2Actor = new vtkActor();
        sphere2Actor.SetMapper( sphere2Mapper );
        sphere2Actor.GetProperty().SetOpacity(.3);
        sphere2Actor.GetProperty().SetColor(0,1,0);

        vtkIntersectionPolyDataFilter intersectionPolyDataFilter =
            new vtkIntersectionPolyDataFilter();
        intersectionPolyDataFilter.SetInputConnection( 0, sphereSource1.GetOutputPort() );
        intersectionPolyDataFilter.SetInputConnection( 1, sphereSource2.GetOutputPort() );
        intersectionPolyDataFilter.Update();

        vtkPolyDataMapper intersectionMapper = new vtkPolyDataMapper();
        intersectionMapper.SetInputConnection( intersectionPolyDataFilter.GetOutputPort() );
        intersectionMapper.ScalarVisibilityOff();

        vtkActor intersectionActor = new vtkActor();
        intersectionActor.SetMapper( intersectionMapper );
        intersectionActor.GetProperty().SetColor(0.1,0.1,0.1);

        VtkExternalFrame vframe = new VtkExternalFrame();
        VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
        
        renderer.addActor(sphere1Actor);
        renderer.addActor(sphere2Actor);
        renderer.addActor(intersectionActor);
        
        vframe.setVisible(true);
        renderer.render();
    }
}



