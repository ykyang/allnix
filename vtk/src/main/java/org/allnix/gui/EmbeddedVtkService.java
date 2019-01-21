package org.allnix.gui;

import java.util.HashMap;
import java.util.Map;

public class EmbeddedVtkService {
	private Map<String, VtkUnstructuredGrid> ugridDb;
	/**
	 * ID -> VtkTrheshold mapping
	 */
	private Map<String, VtkThreshold> thresholdDb;
	
	public EmbeddedVtkService() {
		ugridDb = new HashMap<>();
		thresholdDb = new HashMap<>();
	}
	
	
	public void putVtkThreshold(String id, VtkThreshold v) {
		thresholdDb.put(id, v);
	}
	public VtkThreshold getVtkThreshold(String id) {
		return thresholdDb.get(id);
	}
}
