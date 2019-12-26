package org.allnix.vtk.example;

import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;
import vtk.vtkActor;
import vtk.vtkClipPolyData;
import vtk.vtkCubeSource;
import vtk.vtkDataSetMapper;
import vtk.vtkDelaunay3D;
import vtk.vtkElevationFilter;
import vtk.vtkImplicitDataSet;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderer;
import vtk.vtkSphereSource;

/**
 *
 * https://lorensen.github.io/VTKExamples/site/Python/ImplicitFunctions/ImplicitDataSet/
 * <pre>
 * ./gradlew -PmainClass=org.allnix.vtk.example.ImplicitDataSet runApp
 * </pre>
 * @author Yi-Kun Yang
 */
public class ImplicitDataSet {
    static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
        
        vtkSphereSource sphere = new vtkSphereSource();
        sphere.SetCenter(1, 1, 1);
        sphere.SetRadius(1);
        sphere.Update();

        vtkCubeSource cube = new vtkCubeSource();
        cube.SetBounds(-1, 1, -1, 1, -1, 1);
        cube.Update();

        // Create 3D cells so vtkImplicitDataSet evaluates inside vs outside correctly
        vtkDelaunay3D tri = new vtkDelaunay3D();
        tri.SetInputConnection(cube.GetOutputPort());
        tri.BoundingTriangulationOff();

        // vtkImplicitDataSet needs some scalars to interpolate to find inside/outside
        vtkElevationFilter elev = new vtkElevationFilter();
        elev.SetInputConnection(tri.GetOutputPort());

        vtkImplicitDataSet implicit = new vtkImplicitDataSet();
        implicit.SetDataSet(elev.GetOutput());

        vtkClipPolyData clipper = new vtkClipPolyData();
        clipper.SetClipFunction(implicit);
        clipper.SetInputConnection(sphere.GetOutputPort());
        clipper.InsideOutOn(); // Inside out???
        clipper.Update();

        // Vis for clipped sphere
        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
        mapper.SetInputConnection(clipper.GetOutputPort());
        vtkActor actor = new vtkActor();
        actor.SetMapper(mapper);

        // Vis for cube so can see it in relation to clipped sphere
        vtkDataSetMapper mapper2 = new vtkDataSetMapper();
        mapper2.SetInputConnection(elev.GetOutputPort());
        vtkActor actor2 = new vtkActor();
        actor2.SetMapper(mapper2);
        actor2.GetProperty().SetRepresentationToWireframe();

        VtkExternalFrame vframe = new VtkExternalFrame();
        VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
        
        renderer.addActor(actor);
        renderer.addActor(actor2);
        renderer.setBackground(0.1, 0.2, 0.4);

        vframe.setVisible(true);
        renderer.render();
    }
}
