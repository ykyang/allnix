package org.allnix.hf;

import java.util.List;

public class Well {
    private String id;
    private Integer wellId;
    private List<Double> diameter; // inch
    private List<Double> roughness; // inch
    
    /**
     * Not sure if this is ideal
     */
    private List<Stage> stageList;
    
    public void toCsv() {
        throw new UnsupportedOperationException();
    }
    
}
