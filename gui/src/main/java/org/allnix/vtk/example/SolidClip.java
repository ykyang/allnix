package org.allnix.vtk.example;

import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;

import vtk.vtkActor;
import vtk.vtkClipPolyData;
import vtk.vtkPlane;
import vtk.vtkPolyDataMapper;
import vtk.vtkProperty;
import vtk.vtkSuperquadricSource;

/**
 * ./gradlew -PmainClass=org.allnix.vtk.example.SolidClip runApp
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class SolidClip {

	static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
		
		// Create a superquadric
		vtkSuperquadricSource superquadricSource = new vtkSuperquadricSource();
		superquadricSource.SetPhiRoundness(3.1);
		superquadricSource.SetThetaRoundness(2.2);

		// Define a clipping plane
		vtkPlane clipPlane = new vtkPlane();
//		clipPlane.SetNormal(1.0, -1.0, -1.0);
		clipPlane.SetNormal(1.0, 0.0, 0.0);
		clipPlane.SetOrigin(0.0, 0.0, 0.0);

		// Clip the source with the plane
		vtkClipPolyData clipper = new vtkClipPolyData();
		clipper.SetInputConnection(superquadricSource.GetOutputPort());
		clipper.SetClipFunction(clipPlane);  
//		clipper.InsideOutOff(); // Keep the side of the normal
		clipper.InsideOutOn();  // Keep the other side of the normal

		//Create a mapper and actor
		vtkPolyDataMapper superquadricMapper = new vtkPolyDataMapper();
		superquadricMapper.SetInputConnection(clipper.GetOutputPort());

		vtkActor superquadricActor = new vtkActor();
		superquadricActor.SetMapper(superquadricMapper);

		// Create a property to be used for the back faces. Turn off all
		// shading by specifying 0 weights for specular and diffuse. Max the
		// ambient.
		vtkProperty backFaces = new vtkProperty();
		backFaces.SetSpecular(0.0);
		backFaces.SetDiffuse(0.0);
		backFaces.SetAmbient(1.0);
		backFaces.SetAmbientColor(1.0000, 0.3882, 0.2784);

		// Backface Property, interesting
		superquadricActor.SetBackfaceProperty(backFaces);

		
		VtkExternalFrame vframe = new VtkExternalFrame();
        VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
        
        renderer.addActor(superquadricActor);
        
        vframe.setVisible(true);
        renderer.render();
		
//		// Create a renderer
//		vtkRenderer renderer = vtkRenderer();
//
//		  vtkSmartPointer<vtkRenderWindow> renderWindow = 
//		    vtkSmartPointer<vtkRenderWindow>::New();
//		   renderWindow.SetWindowName("SolidClip");
//
//		  renderWindow.AddRenderer(renderer);
//
//		  vtkSmartPointer<vtkRenderWindowInteractor> renderWindowInteractor = 
//		    vtkSmartPointer<vtkRenderWindowInteractor>::New();
//		  renderWindowInteractor.SetRenderWindow(renderWindow);
//
//		  //Add actors to the renderers
//		  renderer.AddActor(superquadricActor);
//		  renderWindow.Render();
//
//		  //Interact with the window
//		  renderWindowInteractor.Start();

		  
		}
	}

