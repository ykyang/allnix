package org.allnix.gui;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.CellType;
import vtk.vtkActor;
import vtk.vtkCellArray;
import vtk.vtkCellData;
import vtk.vtkDataSetMapper;
import vtk.vtkDoubleArray;
import vtk.vtkHexahedron;
import vtk.vtkIdList;
import vtk.vtkIdTypeArray;
import vtk.vtkIntArray;
import vtk.vtkPoints;
import vtk.vtkUnstructuredGrid;
import vtk.vtkXMLUnstructuredGridWriter;
public class UnstructuredGrid {
	static final private Logger logger = LoggerFactory.getLogger(UnstructuredGrid.class);

	private vtkUnstructuredGrid ugrid;

//	private Map<String, vtkDoubleArray> doubleArrayDb;

	public UnstructuredGrid() {

	}

	public void init() {
		ugrid = new vtkUnstructuredGrid();
	}

	public void addDoubleCellData(String name, double[] data) {
		vtkCellData cellData = ugrid.GetCellData();
		vtkDoubleArray v;

		v = new vtkDoubleArray();
		v.SetName(name);

		
		// > Old way: slower
		// for (int i = 0; i < data.length; i++) {
		// v.InsertNextValue(data[i]);
		// }
		
		// > New way: 30% faster
		v.SetJavaArray(data);

		
		cellData.AddArray(v);
		
		// > do not keep extra copy of data
		// doubleArrayDb.put(name, v);
	}

	public vtkUnstructuredGrid getUnstructuredGrid() {
		return ugrid;
	}

	public void setHexahedronCells(int[] ids) {
		if ((ids.length % 8) != 0) {
			logger.warn("Number of IDs is not multiple of 8");
			throw new IllegalArgumentException("Number of IDs is not multiple of 8");
		}
		
		int cellCount = ids.length / 8;
		logger.info("cellCount: {}", cellCount);

		vtkCellArray cellz = new vtkCellArray();


		vtkHexahedron hex = new vtkHexahedron();
		vtkIdList l;

		// TODO: Performance improvement
		// Seems pre-allocate is not the problem
		// the problem is copying one-by-one.
		// Unfortunately, vtk Java wrapper does not
		// provide direct memory access.
		logger.info("START: Link hexahedron");
		for (int i = 0; i < cellCount; i++) {
			l = hex.GetPointIds();
			for (int localId = 0; localId < 8; localId++) {
				l.SetId(localId, ids[8 * i + localId]);
			}
			cellz.InsertNextCell(hex);
		}
		logger.info("CLOSE: Link hexahedron");

		
		logger.info("START: vtkUnstructuredGrid.SetCells()");
		ugrid.SetCells(CellType.HEXAHEDRON.GetId(), cellz);
		logger.info("CLOSE: vtkUnstructuredGrid.SetCells()");
	}

	public void setPoints(double[] x, double[] y, double[] z) {

		// TODO: check length are the same

		int length = x.length;
		vtkPoints points = new vtkPoints();

		for (int i = 0; i < length; i++) {
			points.InsertNextPoint(x[i], y[i], z[i]);
		}

		ugrid.SetPoints(points);
	}

	/**
	 * Do I need to worry about different data type
	 * 
	 * @param name
	 */
	public void setActiveScalars(String name) {
		// TODO: error checking
		vtkCellData cellData = ugrid.GetCellData();
		cellData.SetActiveScalars(name);
		// cellData.SetScalars(doubleArrayDb.get(name));
	}

	/**
	 * should not be here
	 * 
	 * @param filepath
	 */
	public void writeXML(String filepath) {
		vtkXMLUnstructuredGridWriter writer = new vtkXMLUnstructuredGridWriter();
		writer.SetFileName(filepath);
		writer.SetDataMode(0); // ascii
		writer.SetInputData(ugrid);
		writer.Write();
	}
}