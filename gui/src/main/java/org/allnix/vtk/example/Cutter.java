package org.allnix.vtk.example;

import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;
import vtk.vtkActor;
import vtk.vtkCubeSource;
import vtk.vtkCutter;
import vtk.vtkNamedColors;
import vtk.vtkNativeLibrary;
import vtk.vtkPlane;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderer;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;

/**
 * https://lorensen.github.io/VTKExamples/site/Java/VisualizationAlgorithms/Cutter/
 * 
 * <pre>
 * ./gradlew -PmainClass=org.allnix.vtk.example.Cutter runApp
 * </pre> 
 * @author ykyang
 */
public class Cutter {

  public static void main(String[] args) {
      VtkLoader.loadAllNativeLibraries();
      
    vtkNamedColors color = new vtkNamedColors();
    double planeColor[] = new double[4];
    color.GetColor("Yellow", planeColor);
    double cubeColor[] = new double[4];
    color.GetColor("Aquamarine", cubeColor);
    double bgColor[] = new double[4];
    color.GetColor("Silver", bgColor);


    vtkCubeSource cube = new vtkCubeSource();
    cube.SetXLength(40);
    cube.SetYLength(30);
    cube.SetZLength(20);
    vtkPolyDataMapper cubeMapper = new vtkPolyDataMapper();
    cubeMapper.SetInputConnection(cube.GetOutputPort());

    // create a plane to cut,here it cuts in the XZ direction (xz normal=(1,0,0);XY =(0,0,1),YZ =(0,1,0)
    vtkPlane plane = new vtkPlane();
    plane.SetOrigin(10, 0, 0);
    plane.SetNormal(1, 0, 0);

    //create cutter
    vtkCutter cutter = new vtkCutter();
    cutter.SetCutFunction(plane);
//    cutter.SetInputData(cubeMapper.GetInput());
    cutter.SetInputData(cube.GetOutput());
    cutter.Update();

    vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
    cutterMapper.SetInputConnection(cutter.GetOutputPort());

    //create plane actor
    vtkActor planeActor = new vtkActor();
    planeActor.GetProperty().SetColor(planeColor);
    planeActor.GetProperty().SetLineWidth(10);
    planeActor.SetMapper(cutterMapper);

    //create cube actor
    vtkActor cubeActor = new vtkActor();
    cubeActor.GetProperty().SetColor(cubeColor);
    cubeActor.GetProperty().SetOpacity(0.3);
    cubeActor.SetMapper(cubeMapper);

    VtkExternalFrame vframe = new VtkExternalFrame();
    VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
    
    //create renderers and add actors of plane and cube
    vtkRenderer ren = renderer.getRenderer();
    ren.AddActor(planeActor);
//    ren.AddActor(cubeActor);
    
    //Add renderer to renderwindow and render
//    vtkRenderWindow renWin = new vtkRenderWindow();
//    renWin.AddRenderer(ren);
//    renWin.SetSize(600, 600);
//
//    vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
//    iren.SetRenderWindow(renWin);
//    ren.SetBackground(bgColor);
//    renWin.Render();
//
//    iren.Start();

        vframe.setVisible(true);
        renderer.resetCamera();
        renderer.render();
        
  }
}
