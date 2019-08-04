package org.allnix.gui.hf;

import java.util.Map;
/**
 * Main use case
 * 
 *     1. Find well, stage by completions sequence
 *     2. Find stage per well
 * 
 * Completions sequence ranges from 1 to n where n is the total number of stages.
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class JsonMultiWellService {
	private JsonMultiWell jmw;
	/**
	 * UID to JsonWell mapping
	 * 
	 */
	private Map<String, JsonWell> jsonWellDb;
	/**
	 * Well ID to Well UID mapping
	 */
	private Map<Integer, String> wellUidDb;
	
	/**
	 * UID to fracture outline mapping
	 */
	private Map<String, JsonFractureOutline> fractureOutlineDb;
	
	public JsonWell findWellByUid(String uid) {
		return jsonWellDb.get(uid);
	}
	public JsonFractureOutline findFractureOutlineByUid(String uid) {
		throw new UnsupportedOperationException();
	}
	public JsonFractureOutline findFractureOutlineByStageId(int stageId) {
		throw new UnsupportedOperationException();
	}
//	public JsonFractureOutline findFractureOutlineBySeqId(int stageSeqId) {
//		String stageUid = jmw.getStageUid().get(stageSeqId);
//		return fractureOutlineDb.get(stageUid);
//	}
	
	public String findWellUidBySeq(int stageSeq) {
		return jmw.getWellUidBySeq().get(stageSeq);
	}
	public String findStageUidBySeq(int stageSeq) {
		return jmw.getStageUidBySeq().get(stageSeq);
	}
//	public String findFractureOutlineUidByWellUidStageId(String wellUid, int wellStageId) {
//		
//	}
//	public JsonFractureOutline findFractureOutlineByWellUidAndWellStageId(String wellUid, int wellStageId) {
//		JsonWell jsonWell = jsonWellDb.get(wellUid);
//		JsonFractureOutline jfo = jsonWell.getFractureOutlineUid();
//		
//		
//	}
}
