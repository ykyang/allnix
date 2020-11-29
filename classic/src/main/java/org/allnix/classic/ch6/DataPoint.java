package org.allnix.classic.ch6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataPoint {
  public final int dimensionCount;
  public List<Double> normValueList; // a.k.a. z-score
  private List<Double> valueList;
 
  public DataPoint(Collection<Double> initial) {
    valueList = new ArrayList<>(initial);
    normValueList = new ArrayList<>(initial);
    dimensionCount = normValueList.size();
  }
  
  public double distance(DataPoint other) {
    double differences = 0;
    for (int i = 0; i < dimensionCount; i++) {
      double difference = normValueList.get(i) - other.normValueList.get(i);
      differences += difference * difference;
    }
    
    return Math.sqrt(differences);
  }

  public String toString() {
    return valueList.toString();
  }
  
}
