package org.allnix.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.CellType;
import vtk.vtkActor;
import vtk.vtkCellArray;
import vtk.vtkCellData;
import vtk.vtkDataArray;
import vtk.vtkDataSetMapper;
import vtk.vtkDoubleArray;
import vtk.vtkHexahedron;
import vtk.vtkIdList;
import vtk.vtkIntArray;
import vtk.vtkLookupTable;
import vtk.vtkPoints;
import vtk.vtkUnstructuredGrid;

/**
 * Convenient class for vtkUnstructuredGrid 
 * 
 * In case of overwriting grid, setVtkUnstructuredGrid before calling init().
 * 
 * This is how to write to XML.
 * <pre>
 * vtkXMLUnstructuredGridWriter writer = new vtkXMLUnstructuredGridWriter();
 * writer.SetFileName(filepath);
 * writer.SetDataMode(0); // ascii
 * writer.SetInputData(ugrid);
 * writer.Write();
 * </pre>
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class VtkUnstructuredGrid {
	static final private Logger logger = LoggerFactory.getLogger(VtkUnstructuredGrid.class);
	
	private vtkUnstructuredGrid ugrid;
	private vtkDataSetMapper mapper;
	private vtkActor actor;
	private vtkLookupTable lut;
	
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
	
	/**
	 * Data type needs to specified in method name considering MATLAB
	 * 
	 * @param name
	 * @param data
	 */
	public void addIntCellData(String name, int[] data) {
		vtkCellData cellData = ugrid.GetCellData();
		vtkIntArray v = new vtkIntArray();
		v.SetName(name);
		
		v.SetJavaArray(data);
		
		cellData.AddArray(v);
	}
	
	public vtkActor getActor() {
		return actor;
	}
	
	public vtkDataSetMapper getMapper() {
		return mapper;
	}

	
	public double[] getRange(String name) {
		vtkDataArray d = (vtkDataArray) ugrid.GetCellData().GetAbstractArray(name);
		double[] minmax = d.GetRange();
		return minmax;
	}
	
	public vtkUnstructuredGrid getUnstructuredGrid() {
		return ugrid;
	}
	
	public void init() {
		if (ugrid == null) {
			ugrid = new vtkUnstructuredGrid();
		}
		// Check for null???
		mapper = new vtkDataSetMapper();
		actor = new vtkActor();
		
				
		// > Not sure if this is OK
		mapper.SetInputData(ugrid);
		mapper.SetScalarModeToUseCellData();
		mapper.UseLookupTableScalarRangeOn();
		
		actor.SetMapper(mapper);
		
		lut = new vtkLookupTable();
		// > blue, low -> red, high
		// > flip the number to inverse the color
		lut.SetHueRange(0.6667, 0.);
		lut.Build();
		mapper.SetLookupTable(lut);
	}

	public void setLookupTableRange(double[] minmax) {
		lut.SetRange(minmax);
		lut.Build();
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

	public void setScalarRange(double min, double max) {
		mapper.SetScalarRange(min, max);
	}

	public void setUnstructuredGrid(vtkUnstructuredGrid v) {
		this.ugrid = v;
	}
	
	
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		VtkFrame vframe = new VtkFrame();
				
		String name = "Temperature";
		
		VtkUnstructuredGrid me = new VtkUnstructuredGrid();
		me.init();
		
		Builder.buildVtkUnstructuredGrid7Cell(me);
		
		double[] range = me.getRange(name);
		
		me.setActiveScalars("Temperature");
		me.setLookupTableRange(range);
	
		vframe.pack();
		vframe.addActor(me.getActor());
		vframe.render();
		vframe.setVisible(true);
		vframe.render();
	}
}
