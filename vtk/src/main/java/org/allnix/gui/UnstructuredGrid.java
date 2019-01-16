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
	private vtkDataSetMapper mapper;
	private vtkActor actor;
	
	private Map<String, vtkDoubleArray> doubleArrayDb;
	
	public UnstructuredGrid() {
		ugrid = new vtkUnstructuredGrid();
		mapper = new vtkDataSetMapper();
		actor = new vtkActor();
		
		// > Not sure if this is OK
		mapper.SetInputData(ugrid);
		actor.SetMapper(mapper);
		
		doubleArrayDb = new HashMap<>();
	}
	
	
	
	
	public void addDoubleCellData(String name, double[] data) {
		vtkCellData cellData = ugrid.GetCellData();
		vtkDoubleArray v;
		
		v = new vtkDoubleArray();
		v.SetName(name);
//		for (int i = 0; i < data.length; i++) {
//			v.InsertNextValue(data[i]);
//		}
		v.SetJavaArray(data); // TaDa
//		cellData.SetScalars(v);
		cellData.AddArray(v);
		
		doubleArrayDb.put(name, v);
		
		
	}
	
	public vtkActor getActor() {
		return actor;
	}
	
	public vtkDataSetMapper getMapper() {
		return mapper;
	}
	
	
	
	public vtkUnstructuredGrid getUnstructuredGrid() {
		return ugrid;
	}
	
	public void setHexahedronCells(int[] ids) {
		// TODO: check ids.length() % 8 == 0
		int cellCount = ids.length / 8;
		logger.info("cellCount: {}", cellCount);
		
		
//		vtkIntArray idArray = new vtkIntArray();
//		idArray.SetJavaArray(ids);
		
		
		vtkCellArray cellz = new vtkCellArray();
		vtkHexahedron hex = new vtkHexahedron();
		vtkIdList l = hex.GetPointIds();
		vtkIdTypeArray idz = new vtkIdTypeArray();
		idz.SetNumberOfValues(ids.length);
//		idz.InsertTuples(0, ids.length, 0, idArray); // type mismatch exception
		
		
//		// memory error at 2,000,000 cells
//		for (int i = 0; i < ids.length; i++) {
//			idz.SetValue(i, ids[i]);
//		}
//		cellz.SetCells(cellCount, idz);
		
		
//		cellz.Allocate(cellCount*8*2, 1000000); // does not help
		logger.info("Start: cellCount loop");
		int globalId = 0;
		vtkIdList idList = new vtkIdList();
		idList.SetNumberOfIds(8);

//		int cellType = CellType.HEXAHEDRON.GetId();
//		for (int i = 0; i < cellCount; i++) {
//			for (int localId = 0; localId < 8; localId++) {
// 				idList.SetId(localId, ids[globalId++]);
// 			}
//			ugrid.InsertNextCell(cellType, idList);
//		}
		
		
		
		
		
		for (int i = 0; i < cellCount; i++) {
			// > this for-loop is the bottleneck
			for (int localId = 0; localId < 8; localId++) {
 				l.SetId(localId, ids[globalId++]);
 			}
 			cellz.InsertNextCell(hex);
		}
		logger.info("Done: cellCount loop");
		
		ugrid.SetCells(CellType.HEXAHEDRON.GetId(), cellz);

		
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
	public void setScalars(String name) {
		//TODO: error checking
		vtkCellData cellData = ugrid.GetCellData();
		cellData.SetActiveScalars(name);
		//cellData.SetScalars(doubleArrayDb.get(name));
	}
	
	/**
	 * should not be here
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
