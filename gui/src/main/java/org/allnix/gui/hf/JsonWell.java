package org.allnix.gui.hf;

import java.util.List;

public class JsonWell {
	private String uid;
	private Integer id;
	private String name;
	private String wellPathUid;
	private int stageCount;
	/**
	 * Fracture outline UID ordered by well stage ID
	 */
	private List<String> fractureOutlineUid;
	private List<String> fractureData3DUid;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWellPathUid() {
		return wellPathUid;
	}
	public void setWellPathUid(String wellPathUid) {
		this.wellPathUid = wellPathUid;
	}
	public int getStageCount() {
		return stageCount;
	}
	public void setStageCount(int stageCount) {
		this.stageCount = stageCount;
	}
	public List<String> getFractureOutlineUid() {
		return fractureOutlineUid;
	}
	public void setFractureOutlineUid(List<String> fractureOutlineUid) {
		this.fractureOutlineUid = fractureOutlineUid;
	}
	public List<String> getFractureData3DUid() {
		return fractureData3DUid;
	}
	public void setFractureData3DUid(List<String> fractureData3DUid) {
		this.fractureData3DUid = fractureData3DUid;
	}
	
	
}
