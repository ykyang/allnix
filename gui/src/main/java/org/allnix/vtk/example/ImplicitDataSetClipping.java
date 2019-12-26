package org.allnix.vtk.example;

import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;

import vtk.vtkActor;
import vtk.vtkBox;
import vtk.vtkClipPolyData;
import vtk.vtkCubeSource;
import vtk.vtkDataSet;
import vtk.vtkIdFilter;
import vtk.vtkIdTypeArray;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkSphereSource;

/**
 * ./gradlew -PmainClass=org.allnix.vtk.example.ImplicitDataSetClipping runApp
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class ImplicitDataSetClipping {

	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		
//		vtkNamedColors colors = new vtkNamedColors();

//		colors.SetColor("Bkg", 0.2, 0.3, 0.4);

		vtkSphereSource sphereSource = new vtkSphereSource();
		sphereSource.SetCenter(.75, 0, 0);

		int res = 10;
		sphereSource.SetThetaResolution(res);
		sphereSource.SetPhiResolution(res);
		sphereSource.Update();

//		  std::cout << "The sphere has "
//		            << sphereSource.GetOutput().GetNumberOfPoints() << " points."
//		            << " and " << sphereSource.GetOutput().GetNumberOfCells()
//		            << " cells. " << std::endl;

		// Add ids to the points and cells of the sphere
		vtkIdFilter cellIdFilter = new vtkIdFilter();
		cellIdFilter.SetInputConnection(sphereSource.GetOutputPort());
		cellIdFilter.SetCellIds(1);
		cellIdFilter.SetPointIds(0);
		cellIdFilter.SetIdsArrayName("CellIds");
		cellIdFilter.Update();

//		  WriteDataSet(cellIdFilter.GetOutput(), "CellIdFilter.vtp");

		vtkIdFilter pointIdFilter = new vtkIdFilter();
		pointIdFilter.SetInputConnection(cellIdFilter.GetOutputPort());
		pointIdFilter.SetCellIds(0);
		pointIdFilter.SetPointIds(1);
		pointIdFilter.SetIdsArrayName("PointIds");
		pointIdFilter.Update();

		vtkDataSet sphereWithIds = pointIdFilter.GetOutput();

//		  WriteDataSet(sphereWithIds, "BothIdFilter.vtp");

		vtkCubeSource cubeSource = new vtkCubeSource();
		cubeSource.Update();

		vtkBox implicitCube = new vtkBox();
		implicitCube.SetBounds(cubeSource.GetOutput().GetBounds());

		vtkClipPolyData clipper = new vtkClipPolyData();
		clipper.SetClipFunction(implicitCube);
		clipper.SetInputData(sphereWithIds);
		clipper.InsideOutOn();
		clipper.Update();

//		  WriteDataSet(clipper.GetOutput(), "clipper.vtp");

		// Get the clipped cell ids
		vtkPolyData clipped = clipper.GetOutput();

//		  std::cout << "There are " << clipped.GetNumberOfPoints()
//		            << " clipped points." << std::endl;
//		  std::cout << "There are " << clipped.GetNumberOfCells() << " clipped cells."
//		            << std::endl;

		vtkIdTypeArray clippedCellIds = (vtkIdTypeArray) clipped.GetCellData().GetArray("CellIds");

//		  for (vtkIdType i = 0; i < clippedCellIds.GetNumberOfTuples(); i++)
//		  {
//		    std::cout << "Clipped cell id " << i << " : " << clippedCellIds.GetValue(i)
//		              << std::endl;
//		  }

		  // Create a mapper and actor for clipped sphere
		  vtkPolyDataMapper clippedMapper = new vtkPolyDataMapper();
		  clippedMapper.SetInputConnection(clipper.GetOutputPort());
		  clippedMapper.ScalarVisibilityOff();

		  vtkActor clippedActor = new vtkActor();
		  clippedActor.SetMapper(clippedMapper);
		  clippedActor.GetProperty().SetRepresentationToWireframe();

		  // Create a mapper and actor for cube
		  vtkPolyDataMapper cubeMapper = new vtkPolyDataMapper();
		  cubeMapper.SetInputConnection(cubeSource.GetOutputPort());

		  vtkActor cubeActor = new vtkActor();
		  cubeActor.SetMapper(cubeMapper);
		  cubeActor.GetProperty().SetRepresentationToWireframe();
		  cubeActor.GetProperty().SetOpacity(0.5);

		  VtkExternalFrame vframe = new VtkExternalFrame();
	      VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
		  
		  // Create a renderer, render window, and interactor
//		  auto renderer = vtkSmartPointer<vtkRenderer>::New();
//		  auto renderWindow = vtkSmartPointer<vtkRenderWindow>::New();
//		  renderWindow.AddRenderer(renderer);
//		  auto renderWindowInteractor =
//		      vtkSmartPointer<vtkRenderWindowInteractor>::New();
//		  renderWindowInteractor.SetRenderWindow(renderWindow);

		  // Add the actor to the scene
		  renderer.addActor(clippedActor);
		  renderer.addActor(cubeActor);
		  renderer.setBackground(0.2, 0.3, 0.4);

		  		  
		  vframe.setVisible(true);
		  renderer.resetCamera();
		  renderer.render();
		  
		  // Generate an interesting view
		  //
//		  renderer.ResetCamera();
//		  renderer.GetActiveCamera().Azimuth(-30);
//		  renderer.GetActiveCamera().Elevation(30);
//		  renderer.GetActiveCamera().Dolly(1.1);
//		  renderer.ResetCameraClippingRange();
//
//		  // Render and interact
//		  renderWindow.Render();
//		  renderWindowInteractor.Start();
//
//		  return EXIT_SUCCESS;

	}
}
