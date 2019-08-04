package org.allnix.gui.json;

import org.apache.commons.lang3.ArrayUtils;

public class JsonDoubleArray {
	private String name;
	private double[] value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double[] getValue() {
		return value;
	}
	public void setValue(double[] value) {
		this.value = value;
	}
	public void cloneValue(double[] value) {
		this.value = ArrayUtils.clone(value);
	}
	
}
