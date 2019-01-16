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
	
	static public void main(String[] args) {
		VtkLoader.loadAllNativeLibraries();
		UnstructuredGrid ugrid = new UnstructuredGrid();
		Builder.buildUnstructuredGridMillionCell(ugrid);
//		ugrid.writeXML("2million.vtu");
	}
}
