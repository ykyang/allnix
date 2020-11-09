package org.allnix.simple.ch4;

import java.util.Map;

public class DijkstraResult {
    public final double[] distances;
    public final Map<Integer, WeightedEdge> pathDb;
    
    public DijkstraResult(double[] distances, Map<Integer, WeightedEdge> pathDb) {
        super();
        this.distances = distances;
        this.pathDb = pathDb;
    }
    
    
}
