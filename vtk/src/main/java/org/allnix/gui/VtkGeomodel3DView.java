package org.allnix.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.*;

/**
 * TODO: need to understand memory management
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class VtkGeomodel3DView {
	
	static {
		if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
			for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
				if (!lib.IsLoaded()) {
					System.out.println(lib.GetLibraryName() + " not loaded");
				}
			}
		}
		vtkNativeLibrary.DisableOutputWindow(null);
	}
	
	static final private Logger logger = LoggerFactory.getLogger(VtkGeomodel3DView.class); 

	private vtkPoints points; // no need
	private vtkUnstructuredGrid ugrid;
	private vtkDataSetMapper mapper;
	private vtkActor actor;
	
	public VtkGeomodel3DView() {
		ugrid = new vtkUnstructuredGrid();
		mapper = new vtkDataSetMapper();
		actor = new vtkActor();
		
		// > Not sure if this is ok
		mapper.SetInputData(ugrid);
		actor.SetMapper(mapper);
	}
	
	public void setPoints(double[] x, double[] y, double[] z) {
		
		// TODO: check length are the same
		
		int length = x.length;
		points = new vtkPoints();
		
		for (int i = 0; i < length; i++) {
			points.InsertNextPoint(x[i], y[i], z[i]);
		}
		
		ugrid.SetPoints(points);
	}
	
	public void setHexahedronCells(int[] ids) {
		// TODO: check ids.length() % 8 == 0
		int cellCount = ids.length / 8;
		logger.info("cellCount: {}", cellCount);
		
		
		vtkCellArray cellz = new vtkCellArray();
		vtkHexahedron hex;
		vtkIdList l;
		
		for (int i = 0; i < cellCount; i++) {
			hex = new vtkHexahedron();
			l = hex.GetPointIds();
 			for (int localId = 0; localId < 8; localId++) {
 				l.SetId(localId, ids[8*i + localId]);
 			}
 			cellz.InsertNextCell(hex);
		}
		
		ugrid.SetCells(CellType.HEXAHEDRON.GetId(), cellz);
	}
	
	public void addDoubleCellData(String name, double[] data) {
		vtkCellData cellData = ugrid.GetCellData();
		vtkDoubleArray v;
		
		v = new vtkDoubleArray();
		v.SetName(name);
		for (int i = 0; i < data.length; i++) {
			v.InsertNextValue(data[i]);
		}
	
//		cellData.SetScalars(v);
		cellData.AddArray(v);
	}
	public void addIntCellData(String name, int[] data) {
		
	}
	
	// >>> Not sure about exposing internal vtk stuff
	
	public vtkUnstructuredGrid getUnstructuredGrid() {
		return ugrid;
	}
	/**
	 * SetArrayName(String)
	 * 
	 * @return
	 */
	public vtkDataSetMapper getMapper() {
		return mapper;
	}
	
	public vtkActor getActor() {
		
		return actor;
	}
	
	public void writeXML(String filepath) {
		vtkXMLUnstructuredGridWriter writer = new vtkXMLUnstructuredGridWriter();
		writer.SetFileName(filepath);
		writer.SetDataMode(0); // ascii
		writer.SetInputData(ugrid);
		writer.Write();
	}
	/**
	 * Got to find a way to move this out
	 * @param args
	 */
	static public void main(String[] args) {
		VtkGeomodel3DView view = new VtkGeomodel3DView();
		
//		vtkDataSetMapper mapper = new vtkDataSetMapper();
//		mapper.SetInputData(view.getUnstructuredGrid());
//		
//		vtkActor actor = new vtkActor();
//		actor.SetMapper(mapper);
		
		double dx = 10.;
		double dy = 13;
		double dz = 2;
		double[] x = new double[] {
				0, dx, 2*dx, 0, dx, 2*dx,
				0, dx, 2*dx, 0, dx, 2*dx
		};
		double[] y = new double[] {
			0, 0, 0, dy, dy, dy,
			0, 0, 0, dy, dy, dy
		};
		double[] z = new double[] {
			0, 0, 0, 0, 0, 0,
			dz, dz, dz, dz, dz, dz
		};
	
	
		view.setPoints(x, y, z);
		
		int[] ids = new int[] {
				0, 1, 4, 3, 6, 7, 10, 9,
				1, 2, 5, 4, 7, 8, 11, 10
		}; 
		view.setHexahedronCells(ids);
		
		
		double[] pressure = new double[] {1000,1000};
		view.addDoubleCellData("Pressure", pressure);
		
		double[] temperatures = new double[] {13, 17};
		view.addDoubleCellData("Temperature", temperatures);
		
		
		
		
		
		// > write to XML for debugging
		view.writeXML("geomodel.vtu");
		

		// > Mapper
		vtkUnstructuredGrid ugrid = view.getUnstructuredGrid();
		vtkCellData cellData = ugrid.GetCellData();
		

		vtkMapper mapper = view.getMapper();
		mapper.SetScalarModeToUseCellData();

//		cellData.SetScalars(cellData.GetArray(1));
//		mapper.SetScalarRange(10, 20);
		
		//vtkAbstractArray a = cellData.GetAbstractArray(1);
		cellData.SetScalars((vtkDataArray) cellData.GetAbstractArray(1));
		mapper.SetScalarRange(10, 20);
		
		
		//int ret = cellData.SetActiveScalars("Temperature");
		//logger.info("Return: {}", ret);
//		logger.info("Active Scalar: {}", cellData.GetScalars());
//		ugrid.GetCellData().SetActiveAttribute("Temperature", 0);
//		mapper.SetScalarRange(0, 2000);
//		logger.info("ArrayName: {}", mapper.GetArrayName());
//		mapper.SetArrayName("Temperature");
		
		
		// > Replace code above
		VtkFrame vframe = new VtkFrame();
		
		vtkRenderer renderer = vframe.getVtkRenderWindowPanel().GetRenderer(); 
		renderer.AddActor(view.getActor());
		renderer.ResetCamera();
		vframe.getVtkRenderWindowPanel().Render();		
		//mapper.Update();
	}
}
