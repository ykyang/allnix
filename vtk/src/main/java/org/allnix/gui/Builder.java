package org.allnix.gui;

public class Builder {
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
}
