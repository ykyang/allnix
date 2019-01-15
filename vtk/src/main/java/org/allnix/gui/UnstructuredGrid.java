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
		for (int i = 0; i < data.length; i++) {
			v.InsertNextValue(data[i]);
		}
	
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
		
		
		vtkCellArray cellz = new vtkCellArray();
		vtkHexahedron hex;
		vtkIdList l;
		
		
		hex = new vtkHexahedron();
		logger.info("Start: cellCount loop");
		for (int i = 0; i < cellCount; i++) {
			l = hex.GetPointIds();
 			for (int localId = 0; localId < 8; localId++) {
 				l.SetId(localId, ids[8*i + localId]);
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
