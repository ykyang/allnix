package org.allnix.gui.hf;

import java.io.Serializable;

public class JsonFractureOutline implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id; // computation ID
	private Integer wellId; // no need
	private Integer stageId; // multi-well stage ID
	private Integer wellStageId; // well stage ID
	
	private double[] x;
	private double[] y;
	private double[] z;
	
	/**
	 * Always 1 for now
	 * @return
	 */
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWellId() {
		return wellId;
	}
	public void setWellId(Integer wellId) {
		this.wellId = wellId;
	}
	public Integer getStageId() {
		return stageId;
	}
	public void setStageId(Integer stageId) {
		this.stageId = stageId;
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
	
}
