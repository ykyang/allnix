package org.allnix.gui.hf;

import java.io.Serializable;

public class JsonWellPath implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Universal ID used in storage
	 */
	private String uid;
	/**
	 * Integer ID used in computation kernel
	 */
	private Integer id;
	
	private double[] x;
	private double[] y;
	private double[] z;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public double[] getX() {
		return x;
	}
	public void setX(double[] x) {
		this.x = x;
	}
	public double[] getY() {
		return y;
	}
	public void setY(double[] y) {
		this.y = y;
	}
	public double[] getZ() {
		return z;
	}
	public void setZ(double[] z) {
		this.z = z;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
}
