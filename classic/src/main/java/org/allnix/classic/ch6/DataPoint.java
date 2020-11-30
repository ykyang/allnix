package org.allnix.classic.ch6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataPoint {
  public final int dimensionCount;
  public List<Double> zscore; // a.k.a. z-score
  private List<Double> value;
 
  public DataPoint(Collection<Double> initial) {
    value = new ArrayList<>(initial);
    zscore = new ArrayList<>(initial);
    dimensionCount = value.size(); zscore.size();
  }
  
  public double distance(DataPoint other) {
    double differences = 0;
    for (int i = 0; i < dimensionCount; i++) {
      double difference = zscore.get(i) - other.zscore.get(i);
      differences += difference * difference;
    }
    
    return Math.sqrt(differences);
  }

  
  public String toString() {
    return value.toString();
  }
  
}
