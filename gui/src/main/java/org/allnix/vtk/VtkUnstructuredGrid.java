package org.allnix.vtk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.allnix.gui.Builder;
import org.allnix.gui.ColorList;
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
import vtk.vtkIdTypeArray;
import vtk.vtkIntArray;
import vtk.vtkLookupTable;
import vtk.vtkPointData;
import vtk.vtkPoints;
import vtk.vtkPolyVertex;
import vtk.vtkRenderWindowPanel;
import vtk.vtkScalarBarActor;
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
	
	private String activeScalarName;
	private String activePointScalarName;
	
	private vtkScalarBarActor scalarBarActor;
	
	private List<String> cellDataNameList = new ArrayList<String>();
	private List<String> pointDataNameList = new ArrayList<String>(); 
	
	public void setScalarBarActor(vtkScalarBarActor v) {
        scalarBarActor = v;
    }
    
	public List<String> getCellDataNameList() {
	    return cellDataNameList;
	}
	
	public List<String> getPointDataNameList() {
	    return pointDataNameList;
	}
	
    public vtkScalarBarActor getScalarBarActor() {
        return scalarBarActor;
    }
	
	public void addDoublePointData(String name, double[] data) {
		vtkPointData pointData = ugrid.GetPointData();
		vtkDoubleArray v;

		v = new vtkDoubleArray();
		v.SetName(name);
		
		// > New way: 30% faster
		v.SetJavaArray(data);

		
		pointData.AddArray(v);
		
		if (!this.pointDataNameList.contains(name)) {
            this.pointDataNameList.add(name);
        }
	}
	
	public void addTuple3DoublePointData(String name, double[] data) {
	    vtkPointData pointData = ugrid.GetPointData();
        vtkDoubleArray v;
        
        v = new vtkDoubleArray();
        v.SetName(name);
        
        v.SetNumberOfComponents(3);
        v.SetNumberOfTuples(data.length/3);
        
        // > New way: 30% faster
        v.SetJavaArray(data);
        
        pointData.AddArray(v);
	}
	
	public void addTuple6DoublePointData(String name, double[] data) {
        vtkPointData pointData = ugrid.GetPointData();
        vtkDoubleArray v;
        
        v = new vtkDoubleArray();
        v.SetName(name);
        
        v.SetNumberOfComponents(6);
        v.SetNumberOfTuples(data.length/6);
        
        // > New way: 30% faster
        v.SetJavaArray(data);
        
        pointData.AddArray(v);
    }
	public void removeDoubleCellData(String name) {
	    vtkCellData cellData = ugrid.GetCellData();
	    cellData.RemoveArray(name);
	    this.cellDataNameList.remove(name);
	}
	public boolean hasData(String name) {
	    boolean hasit = false;
	    
	    hasit = this.cellDataNameList.contains(name);
	    if (hasit) { // shot circuit
	        return hasit;
	    }
	    
	    hasit = this.pointDataNameList.contains(name);
	    return hasit;
	    
	}
	public void removeAllCellData() {
	    vtkCellData cellData = ugrid.GetCellData();
	    int count = cellData.GetNumberOfArrays();
	    for (int i = 0; i < count; i++) {
	        cellData.RemoveArray(i);
	    }
	    this.cellDataNameList.clear();
	}
	
	public void removeAllPointData() {
        vtkPointData pointData = ugrid.GetPointData();
        int count = pointData.GetNumberOfArrays();
        for (int i = 0; i < count; i++) {
            pointData.RemoveArray(i);
        }
        this.pointDataNameList.clear();
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
		
		if (!this.cellDataNameList.contains(name)) {
		    this.cellDataNameList.add(name);
		}
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
		
		if (!this.cellDataNameList.contains(name)) {
            this.cellDataNameList.add(name);
        }
	}
	
	public vtkActor getActor() {
		return actor;
	}
	
	public vtkLookupTable getLookupTable() {
		return lut;
	}
	
	public vtkDataSetMapper getMapper() {
		return mapper;
	}

	
	public double[] getRange(String name) {
		// TODO: what about point data
		vtkDataArray d = (vtkDataArray) ugrid.GetCellData().GetAbstractArray(name);
//		logger.info("vtkDataArray: {}", d);
		if (d == null) {
		    return null;
		}
		double[] minmax = d.GetRange();
		return minmax;
	}
	public double[] getPointRange(String name) {
		vtkDataArray d = (vtkDataArray) ugrid.GetPointData().GetAbstractArray(name);
		if (d == null) {
            return null;
        }
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
//		mapper.SetScalarModeToUsePointData();
		mapper.UseLookupTableScalarRangeOn();
		
		actor.SetMapper(mapper);
		
		//: Default color map to Jet
		if (lut == null) {
			lut = new vtkLookupTable();
			ColorList.buildJetColorTable(lut);
//			// > blue, low -> red, high
//			// > flip the number to inverse the color
//			lut.SetHueRange(0.6667, 0.);
//			
////			int count = 402;
////			double denominator = 401.; 
//			lut.SetNumberOfTableValues(402);
////			for (int i = 0; i < count; i++) {
////			    double value = i/denominator;
////			    lut.SetTableValue(i, value, value, value, 1);
////			}
//			
//			
//			
//			lut.Build();
		}

		mapper.SetLookupTable(lut);
		
		scalarBarActor = new vtkScalarBarActor();
	}

	public void setScalarModeToUsePointData() {
		mapper.SetScalarModeToUsePointData();
	}
	public void setScalarModeToUseCellData() {
		mapper.SetScalarModeToUseCellData();
	}
	
	public void setLookupTable(vtkLookupTable lut) {
		this.lut = lut;
		mapper.SetLookupTable(lut);
	}
	
	public void setLookupTableRange(double[] minmax) {
		lut.SetRange(minmax);
		lut.Build();
	}
	/**
	 * Do I need to worry about different data type
	 * TODO: check setting null
	 * @param name
	 */
	public void setActiveScalars(String name) {
		vtkCellData cellData = ugrid.GetCellData();
		int ans = cellData.SetActiveScalars(name);
//		if (ans != -1) { // success with array index
		this.activeScalarName = name;
//		}
		
		// cellData.SetScalars(doubleArrayDb.get(name));
	}
	
	public void setActivePointScalar(String name) {
		vtkPointData pointData = ugrid.GetPointData();
		pointData.SetActiveScalars(name);
		this.activePointScalarName = name;
	}
	
	public void setActivePointVector(String name) {
	    vtkPointData pointData = ugrid.GetPointData();
        pointData.SetActiveVectors(name);
        // TODO: save active name
	}
	/**
	 * Use the range of the named scalar
	 * @param name
	 */
	public void setAutoRange(String name) {
		double[] range = this.getRange(name);
		this.setLookupTableRange(range);
	}
	
	public void setPointAutoRange(String name) {
	    double[] range = this.getPointRange(name);
	    this.setLookupTableRange(range);
	}
	
	public void setAmbient(double value) {
		this.actor.GetProperty().SetAmbient(value);
	}
	
	public void setOpacity(double value) {
	    this.actor.GetProperty().SetOpacity(value);
	}
	public void setPolyVertex(int[] ids) {
	    int cellCount = 1;
	    
	    vtkPolyVertex polyVertex = new vtkPolyVertex();
	    vtkIdList idList = polyVertex.GetPointIds();
	    idList.SetNumberOfIds(ids.length);
	    for (int pointInd = 0; pointInd < ids.length; pointInd++) {
	        idList.SetId(pointInd, pointInd);
	    }
	    
	    ugrid.Allocate(1, 1);
	    ugrid.InsertNextCell(CellType.POLY_VERTEX.GetId(), polyVertex.GetPointIds());
	}
	/**
	 * Use stream
	 * 
	 * @param ids
	 */
	public void setHexahedronCellsFast(int[] ids) {
	    if ((ids.length % 9) != 0) {
            logger.warn("Number of IDs is not multiple of 9");
            throw new IllegalArgumentException("Number of IDs is not multiple of 9");
        }
        
        int cellCount = ids.length / 9;
//      logger.info("cellCount: {}", cellCount);

        vtkCellArray cellz = new vtkCellArray();
        vtkIdTypeArray conn = new vtkIdTypeArray();// cell connectivity
        conn.SetNumberOfValues(ids.length);
        IntStream.range(0, ids.length).parallel().forEach((ind) ->{
            conn.SetValue(ind, ids[ind]);
        });;
        cellz.SetCells(cellCount, conn);
                
//      logger.info("START: vtkUnstructuredGrid.SetCells()");
        ugrid.SetCells(CellType.HEXAHEDRON.GetId(), cellz);
//      logger.info("CLOSE: vtkUnstructuredGrid.SetCells()");
	}
	
	public void setHexahedronCells(int[] ids) {
		if ((ids.length % 8) != 0) {
			logger.warn("Number of IDs is not multiple of 8");
			throw new IllegalArgumentException("Number of IDs is not multiple of 8");
		}
		
		int cellCount = ids.length / 8;
//		logger.info("cellCount: {}", cellCount);

		vtkCellArray cellz = new vtkCellArray();
		vtkHexahedron hex = new vtkHexahedron();
		vtkIdList l;
		
		// TODO: Performance improvement
		// Seems pre-allocate is not the problem
		// the problem is copying one-by-one.
		// Unfortunately, vtk Java wrapper does not
		// provide direct memory access.
//		logger.info("START: Link hexahedron");
		for (int i = 0; i < cellCount; i++) {
			l = hex.GetPointIds();
			for (int localId = 0; localId < 8; localId++) {
				l.SetId(localId, ids[8 * i + localId]);
			}
			cellz.InsertNextCell(hex);
//			cellz.SetCells(id0, id1);
//			vtkIdTypeArray ita = new vtkIdTypeArray(
		}
//		logger.info("CLOSE: Link hexahedron");

		
//		logger.info("START: vtkUnstructuredGrid.SetCells()");
		ugrid.SetCells(CellType.HEXAHEDRON.GetId(), cellz);
//		logger.info("CLOSE: vtkUnstructuredGrid.SetCells()");
	}
	
	public void setPoints(double[] x, double[] y, double[] z) {

		// TODO: check length are the same

		int length = x.length;
		vtkPoints points = new vtkPoints();
		points.SetNumberOfPoints(length);
		for (int i = 0; i < length; i++) {
		    points.SetPoint(i, x[i], y[i], z[i]);
//			points.InsertNextPoint(x[i], y[i], z[i]);
		}

		ugrid.SetPoints(points);
	}

	public void setScalarRange(double min, double max) {
		mapper.SetScalarRange(min, max);
	}

	public void setUnstructuredGrid(vtkUnstructuredGrid v) {
		this.ugrid = v;
	}
	
	public String getActiveScalarName() {
		return activeScalarName;
	}
}
