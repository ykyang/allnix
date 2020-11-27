package org.allnix.classic.ch6;

import java.util.ArrayList;
import java.util.List;

public class DataPoint {
  public final int dimensionCount;
  public List<Double> dimensions;
  private List<Double> original;
 
  public DataPoint(List<Double> initial) {
    original = initial;
    dimensions = new ArrayList<>(initial);
    dimensionCount = dimensions.size();
  }
  
  public double distance(DataPoint other) {
    double differences = 0;
    for (int i = 0; i < dimensionCount; i++) {
      double difference = dimensions.get(i) - other.dimensions.get(i);
      differences += difference * difference;
    }
    
    
    return Math.sqrt(differences);
  }
  
}
