package org.allnix.classic.ch6;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public final class Statistics {
  private Collection<Double> values;
  private DoubleSummaryStatistics dss;

  public Statistics(Collection<Double> values) {
    this.values = values;
    dss = values.stream().collect(Collectors.summarizingDouble(value -> value));
  }

  public double sum() {
    return dss.getSum();
  }

  public double mean() {
    return dss.getAverage();
  }

  public double variance() {
    double mean = mean();
    double ans = values.stream().mapToDouble(x -> Math.pow((x - mean), 2))
        .average().getAsDouble();

    return ans;
  }
  
  public double std() {
    return Math.sqrt(variance());
  }
  /**
   * z-scores = (x - mean)/std
   * 
   * @return z-score for each value
   */
  public Collection<Double> zscored() {
    double mean = mean();
    double std = std();
    Collection<Double> ans = values.stream().map((x) -> {
      if (std == 0.0) {
        return 0.0;
      } else {
        return (x-mean)/std;
      }
    }).collect(Collectors.toList());
      
    return ans;
  }
  
  public double max() {
    return dss.getMax();
  }
  
  public double min() {
    return dss.getMin();
  }
}
