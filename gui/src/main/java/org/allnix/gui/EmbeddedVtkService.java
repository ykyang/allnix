package org.allnix.gui;

import java.util.HashMap;
import java.util.Map;

import org.allnix.vtk.VtkPlaneCutter;
import org.allnix.vtk.VtkThreshold;
import org.allnix.vtk.VtkUnstructuredGrid;

public class EmbeddedVtkService {
	private Map<String, VtkUnstructuredGrid> ugridDb;
	/**
	 * ID -> VtkTrheshold mapping
	 */
	private Map<String, VtkThreshold> thresholdDb;
	private Map<String, VtkPlaneCutter> planeCutterDb;
	
	public EmbeddedVtkService() {
		ugridDb = new HashMap<>();
		thresholdDb = new HashMap<>();
		planeCutterDb = new HashMap<>();
	}
	
	
	public void putVtkThreshold(String id, VtkThreshold v) {
		thresholdDb.put(id, v);
	}
	public VtkThreshold getVtkThreshold(String id) {
		return thresholdDb.get(id);
	}
	
	public void putVtkPlaneCutter(String id, VtkPlaneCutter v) {
		planeCutterDb.put(id, v);
	}
	
	public VtkPlaneCutter getVtkPlaneCutter(String id) {
		return planeCutterDb.get(id);
	}
}
