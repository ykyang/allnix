package org.allnix.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Builder {
	static private Logger logger = LoggerFactory.getLogger(Builder.class);
	/**
	 * Build a 2-cell unstructured grid
	 * 
	 * with pressure and temperature data
	 * 
	 * @param grid
	 */
	static public void buildUnstructuredGrid2Cell(UnstructuredGrid grid) {
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
	
	
		grid.setPoints(x, y, z);
		
		int[] ids = new int[] {
				0, 1, 4, 3, 6, 7, 10, 9,
				1, 2, 5, 4, 7, 8, 11, 10
		}; 
		grid.setHexahedronCells(ids);
		
		
		double[] pressure = new double[] {2000,1000};
		grid.addDoubleCellData("Pressure", pressure);
		
		double[] temperatures = new double[] {13, 17};
		grid.addDoubleCellData("Temperature", temperatures);
	}
	
	static public void buildUnstructuredGrid7Cell(UnstructuredGrid grid) {
		double dx = 10.;
		double dy = 13;
		double dz = 2;
		int cellCount = 7;
		
		
		double[] x = new double[] {
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx, 
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx,
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx,
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx
		};
		double[] y = new double[] {
			0, 0, 0, 0, 0, 0, 0, 0,
			dy, dy, dy, dy, dy, dy, dy, dy,
			0, 0, 0, 0, 0, 0, 0, 0,
			dy, dy, dy, dy, dy, dy, dy, dy
		};
		double[] z = new double[] {
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			dz, dz, dz, dz, dz, dz, dz, dz,
			dz, dz, dz, dz, dz, dz, dz, dz
		};
	
	
		grid.setPoints(x, y, z);
		
		int[] ids = new int[] {
				0, 1, 9, 8, 16, 17, 25, 24,
				1, 2, 10, 9, 17, 18, 26, 25,
				2, 3, 11, 10, 18, 19, 27, 26,
				3, 4, 12, 11, 19, 20, 28, 27,
				4, 5, 13, 12, 20, 21, 29, 28,
				5, 6, 14, 13, 21, 22, 30, 29,
				6, 7, 15, 14, 22, 23, 31, 30
		}; 
		grid.setHexahedronCells(ids);
		
		
		double[] pressure = new double[] {7000, 6000, 5000, 4000, 3000, 2000,1000};
		grid.addDoubleCellData("Pressure", pressure);
		
		double[] temperatures = new double[] {13, 14, 15, 16, 17, 18, 19};
		grid.addDoubleCellData("Temperature", temperatures);
		
		int[] iIndex = new int[] {1,2,3,4,5,6,7};
		grid.addIntCellData("i_index", iIndex);
	}
	
	
	static public void buildUnstructuredGridMillionCell(UnstructuredGrid grid) {
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
	
	
		grid.setPoints(x, y, z);
		
		logger.info("Insert 2 million cells");
		int[] ids = new int[8*2000000];
		// Test the performance
		grid.setHexahedronCells(ids);
		logger.info("Done: Insert 2 million cells");
		
		
		double[] pressure = new double[] {2000,1000};
		grid.addDoubleCellData("Pressure", pressure);
		
		double[] temperatures = new double[] {13, 17};
		grid.addDoubleCellData("Temperature", temperatures);
	}

	
	static public void buildVtkUnstructuredGrid7Cell(VtkUnstructuredGrid grid) {
		double dx = 10.;
		double dy = 13;
		double dz = 2;
		int cellCount = 7;
		
		
		double[] x = new double[] {
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx, 
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx,
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx,
				0, dx, 2*dx, 3*dx, 4*dx, 5*dx, 6*dx, 7*dx
		};
		double[] y = new double[] {
			0, 0, 0, 0, 0, 0, 0, 0,
			dy, dy, dy, dy, dy, dy, dy, dy,
			0, 0, 0, 0, 0, 0, 0, 0,
			dy, dy, dy, dy, dy, dy, dy, dy
		};
		double[] z = new double[] {
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			dz, dz, dz, dz, dz, dz, dz, dz,
			dz, dz, dz, dz, dz, dz, dz, dz
		};
	
	
		grid.setPoints(x, y, z);
		
		int[] ids = new int[] {
				0, 1, 9, 8, 16, 17, 25, 24,
				1, 2, 10, 9, 17, 18, 26, 25,
				2, 3, 11, 10, 18, 19, 27, 26,
				3, 4, 12, 11, 19, 20, 28, 27,
				4, 5, 13, 12, 20, 21, 29, 28,
				5, 6, 14, 13, 21, 22, 30, 29,
				6, 7, 15, 14, 22, 23, 31, 30
		}; 
		grid.setHexahedronCells(ids);
		
		
		double[] pressure = new double[] {7000, 6000, 5000, 4000, 3000, 2000,1000};
		grid.addDoubleCellData("Pressure", pressure);
		
		double[] temperatures = new double[] {13, 14, 15, 16, 17, 18, 19};
		grid.addDoubleCellData("Temperature", temperatures);
		
		int[] iIndex = new int[] {1,2,3,4,5,6,7};
		grid.addIntCellData("i_index", iIndex);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		UnstructuredGrid ugrid = new UnstructuredGrid();
		Builder.buildUnstructuredGridMillionCell(ugrid);
//		ugrid.writeXML("2million.vtu");
	}
}
