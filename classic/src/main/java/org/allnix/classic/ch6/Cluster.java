package org.allnix.classic.ch6;

import java.util.List;

public class Cluster<Point extends DataPoint> {
  public List<Point> pointList;
  public DataPoint centroid;
  
  public Cluster(List<Point> points, DataPoint randPoint) {
    this.pointList = points;
    this.centroid = randPoint;
  }
}
