package org.allnix.vtk.example;

import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;

import vtk.vtkActor;
import vtk.vtkClipDataSet;
import vtk.vtkConeSource;
import vtk.vtkDataSetMapper;
import vtk.vtkDoubleArray;
import vtk.vtkFloatArray;
import vtk.vtkImplicitPolyDataDistance;
import vtk.vtkNamedColors;
import vtk.vtkPolyDataMapper;
import vtk.vtkRectilinearGrid;
import vtk.vtkRectilinearGridGeometryFilter;

/**
 * ./gradlew -PmainClass=org.allnix.vtk.example.ClipDataSetWithPolyData runApp
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class ClipDataSetWithPolyData {
	static public void main(String[] args) {
        VtkLoader.loadAllNativeLibraries();
        
		vtkNamedColors colors = new vtkNamedColors();
		double[] color = new double[4];
		// Create polydata to slice the grid with. In this case, use a cone. This could
		// be any polydata including a stl file.
		vtkConeSource cone = new vtkConeSource();
		cone.SetResolution(20);
		cone.Update();

		// implicit function that will be used to slice the mesh
		vtkImplicitPolyDataDistance implicitPolyDataDistance = new vtkImplicitPolyDataDistance();
		implicitPolyDataDistance.SetInput(cone.GetOutput());

	    // create a grid
	    vtkDoubleArray xCoords = new vtkDoubleArray();
	    int xgrid = 15;
	    double xi = -1.0;
	    double xdelta = (1.0 - (-1.0)) / xgrid;
	    for (int i = 0; i <= xgrid; i++) {
	    	double x = xi + i*xdelta;
	    	xCoords.InsertNextValue(x);
	    }
	    //for x, i in enumerate(np.linspace(-1.0, 1.0, 15)):
	        
	    	
	    vtkDoubleArray yCoords = new vtkDoubleArray();
	    int ygrid = 15;
	    double yi = -1.0;
	    double ydelta = (1.0 - (-1.0)) / ygrid;
	    for (int i = 0; i < ygrid; i++) {
	    	double y = yi + i * ydelta;
	    	yCoords.InsertNextValue(y);
	    }
//	    for y, i in enumerate(np.linspace(-1.0, 1.0, 15)):
//	        yCoords.InsertNextValue(i)

	    
	    vtkDoubleArray zCoords = new vtkDoubleArray();
	    int zgrid = 15;
	    double zi = -1.0;
	    double zdelta = (1.0 -(-1.0)) / zgrid;
	    for (int i = 0; i < zgrid; i++) {
	    	double z = zi + i * zdelta;
	    	zCoords.InsertNextValue(z);
	    }
//	    for z, i in enumerate(np.linspace(-1.0, 1.0, 15)):
//	        zCoords.InsertNextValue(i)

	    // The coordinates are assigned to the rectilinear grid. Make sure that
	    // The number of values in each of the XCoordinates, YCoordinates,
	    // and ZCoordinates is equal to what is defined in SetDimensions().
	    vtkRectilinearGrid rgrid = new vtkRectilinearGrid();
	    rgrid.SetDimensions(xgrid + 1, ygrid + 1, zgrid + 1);
	    rgrid.SetXCoordinates(xCoords);
	    rgrid.SetYCoordinates(yCoords);
	    rgrid.SetZCoordinates(zCoords);

	    // Create an array to hold distance information
	    vtkDoubleArray signedDistances = new vtkDoubleArray();
	    signedDistances.SetNumberOfComponents(1);
	    signedDistances.SetName("SignedDistances");

	    // Evaluate the signed distance function at all of the grid points
	    for (int pointId = 0; pointId < rgrid.GetNumberOfPoints(); pointId++) {
	    	double[] p = rgrid.GetPoint(pointId);
	    	double signedDistance = implicitPolyDataDistance.EvaluateFunction(p);
	    	signedDistances.InsertNextValue(signedDistance);
	    }
	    
//	    for pointId in range(rgrid.GetNumberOfPoints()):
//	        p = rgrid.GetPoint(pointId)
//	        signedDistance = implicitPolyDataDistance.EvaluateFunction(p)
//	        signedDistances.InsertNextValue(signedDistance)

	    // add the SignedDistances to the grid
	    rgrid.GetPointData().SetScalars(signedDistances);

	    // use vtkClipDataSet to slice the grid with the polydata
	    vtkClipDataSet clipper = new vtkClipDataSet();
	    clipper.SetInputData(rgrid);
	    clipper.InsideOutOn();
	    clipper.SetValue(0.0);
	    clipper.Update();

	    // --- mappers, actors, render, etc. ---
	    // mapper and actor to view the cone
	    vtkPolyDataMapper coneMapper = new vtkPolyDataMapper();
	    coneMapper.SetInputConnection(cone.GetOutputPort());
	    vtkActor coneActor = new vtkActor();
	    coneActor.SetMapper(coneMapper);

	    // geometry filter to view the background grid
	    vtkRectilinearGridGeometryFilter geometryFilter = new vtkRectilinearGridGeometryFilter();
	    geometryFilter.SetInputData(rgrid);
	    //geometryFilter.SetExtent(0, xgrid + 1, 0, ygrid + 1, (zgrid + 1) / 2, (zgrid + 1) / 2);
	    geometryFilter.SetExtent(0, xgrid + 1, 0, ygrid + 1, 5, 5);
	    geometryFilter.Update();

	    vtkPolyDataMapper rgridMapper = new vtkPolyDataMapper();
	    rgridMapper.SetInputConnection(geometryFilter.GetOutputPort());

	    vtkActor wireActor = new vtkActor();
	    wireActor.SetMapper(rgridMapper);
	    wireActor.GetProperty().SetRepresentationToWireframe();
	    colors.GetColor("Black", color);
	    wireActor.GetProperty().SetColor(color);
	    

	    // mapper and actor to view the clipped mesh
	    vtkDataSetMapper clipperMapper = new vtkDataSetMapper();
	    clipperMapper.SetInputConnection(clipper.GetOutputPort());

	    vtkActor clipperActor = new vtkActor();
	    clipperActor.SetMapper(clipperMapper);
	    clipperActor.GetProperty().SetRepresentationToWireframe();
	    colors.GetColor("Black", color);
	    clipperActor.GetProperty().SetColor(color);

	    
	    VtkExternalFrame vframe = new VtkExternalFrame();
        VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
        vframe.getSceneProp().showAxesActor();
	    
	    
	    // A renderer and render window
        colors.GetColor("White", color);
	    renderer.setBackground(color);

	    // add the actors
	    //renderer.addActor(coneActor);
	    renderer.addActor(wireActor);
	    renderer.addActor(clipperActor);

	    renderer.resetCamera();
	    vframe.setVisible(true);
	    renderer.render();
//	    renderer.GetActiveCamera().SetPosition(0, -1, 0)
//	    renderer.GetActiveCamera().SetFocalPoint(0, 0, 0)
//	    renderer.GetActiveCamera().SetViewUp(0, 0, 1)
//	    renderer.GetActiveCamera().Azimuth(30)
//	    renderer.GetActiveCamera().Elevation(30)
//	    renderer.ResetCamera()
	    
		
	}
}
