package org.allnix.gui.hf;

import java.util.List;

/**
 * Two types of stage ID:
 *     1. Multi-well stage ID or simply stage ID
 *     2. Single-well stage ID or simply sell stage ID
 *     
 * Stage ID is the continuous numerical ID number from 1 to N 
 * for stages in mulit-well setting.  Where N is the total number of stages
 * in all wells.
 * 
 * Well stage ID is number per well from 1 to n.    
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class JsonMultiWell {
	private String uid;
	private int stageCount;
	/**
	 * Well IDs in the same order as wellUid
	 */
	private List<Integer> wellId;
	/**
	 * Well UID in the same order as wellId
	 */
	private List<String> wellUid;
	/**
	 * Stage ID in order of completions sequencing
	 */
	private List<String> stageId;
	/**
	 * Stage UID in order of completions sequencing
	 */
	private List<String> stageUidBySeq;
	/**
	 * Well UID ordered in completions sequence 
	 */
	private List<String> wellUidBySeq;
		
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getStageCount() {
		return stageCount;
	}
	public void setStageCount(int stageCount) {
		this.stageCount = stageCount;
	}
	public List<Integer> getWellId() {
		return wellId;
	}
	public void setWellId(List<Integer> wellId) {
		this.wellId = wellId;
	}
	public List<String> getWellUid() {
		return wellUid;
	}
	public void setWellUid(List<String> wellUid) {
		this.wellUid = wellUid;
	}
	public List<String> getStageId() {
		return stageId;
	}
	public void setStageId(List<String> stageId) {
		this.stageId = stageId;
	}
	public List<String> getStageUidBySeq() {
		return stageUidBySeq;
	}
	public void setStageUidBySeq(List<String> stageUid) {
		this.stageUidBySeq = stageUid;
	}
	public List<String> getWellUidBySeq() {
		return wellUidBySeq;
	}
	public void setWellUidBySeq(List<String> seqWellUid) {
		this.wellUidBySeq = seqWellUid;
	}
	
}
