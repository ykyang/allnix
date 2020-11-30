package org.allnix.classic.ch6;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Streams;

public class KMean<Point extends DataPoint> {
  private PrintStream out = System.out;
  
  private int dimensionCount;
  private Collection<Point> points;
  private List<Cluster<Point>> clusters;
  
  public Collection<Point> getPoints() {
    return points;
  }
  public KMean(int k, Collection<Point> points) {
    if (k < 1) {
      throw new IllegalArgumentException("k must be >= 1");
    }

    // All points must have the same dimension
    long count =
        points.stream().mapToInt(pt -> pt.dimensionCount).distinct().count();
    if (count != 1) {
      throw new IllegalArgumentException("Points have different dimensions");
    }

    // Get the dimension count
    // Is this the right way?
    points.stream().limit(1).forEach(pt -> dimensionCount = pt.dimensionCount);

    this.points = points; //new ArrayList<>(points);

    computeZScore();

    // initialize empty clusters with random centroids
    clusters = new ArrayList<>();
    for (int i = 0; i < k; i++) {
      DataPoint randPoint = randomPoint();
      Cluster<Point> cluster = new Cluster<>(new ArrayList<>(), randPoint);
      clusters.add(cluster);
    }
  }
  
  /**
   * Use the values from a set of points to compute the corresponding
   * z-score.
   * 
   * Formally normalizeZScore
   * @param points
   */
  @VisibleForTesting
  void computeZScore(Collection<Point> points) {
    // zscore[point,dimension] = zscore
    List<List<Double>> zscoreMat = new ArrayList<>();

    // initialize
    for (Point point : points) {
      zscoreMat.add(new ArrayList<Double>());
    }

    for (int dimInd = 0; dimInd < dimensionCount; dimInd++) {
      // normValue[:,dimInd]
      List<Double> normValueCol = zscoreColumn(dimInd); // size = points
      Statistics stat = new Statistics(normValueCol);
      // z-score[:,dimInd]
      Collection<Double> zscoreCol = stat.zscored(); // size = points
//      out.println("zscoresOfDimInd");
//      out.println(zscoresOfDimInd);
      // Distribute zscores into zscoreList
      // Guava library
      // Append 1 column to the matrix
      Streams.forEachPair(zscoreMat.stream(), // Row of 
          zscoreCol.stream(), // Double: dimension of a point
          (list, zscore) -> list.add(zscore));
    }

//    out.println("zscoreList:");
//    out.println(zscoreList);
    
    //
    Streams.forEachPair(
        points.stream(), // -> point 
        zscoreMat.stream(),// -> zscoreRow
        (point, normValueRow) -> point.zscore = normValueRow);
    out.println("zscore:");
    points.stream().forEach(pt -> out.println(pt.zscore));
  }
  
  /**
   * normalizeZScore
   */
  @VisibleForTesting
  void computeZScore() {
    computeZScore(points);
  }


  /**
   * Get the norm value of all points at a dimension index.
   * 
   * That is normValue[:,dimInd]
   * 
   * @param dimInd
   * @return
   */
  @VisibleForTesting
  List<Double> zscoreColumn(int dimInd) {
    return zscoreColumn(dimInd, points);
  }

  @VisibleForTesting
  List<Double> zscoreColumn(int dimInd, Collection<Point> points) {
    return points.stream().map(pt -> pt.zscore.get(dimInd))
        .collect(Collectors.toList());
  }

  /**
   * DataPoint with normalized random values
   * 
   * @return
   */
  private DataPoint randomPoint() {
    // List of norm value for one point
    List<Double> randNormValueList =
        new ArrayList<>(Collections.nCopies(dimensionCount, 0.0));
    Random random = new Random();

    // Generate a list of norm value for one DataPoint
    IntStream.range(0, dimensionCount).forEach(dimInd -> {
      // Collection<Double> randNormValueList = new ArrayList<>();
      List<Double> normValueSliceList = zscoreColumn(dimInd);
      // Need to limit rand between min/max
      Statistics stat = new Statistics(normValueSliceList);
      Double randNormValue =
          random.doubles(1, stat.min(), stat.max()).findFirst().getAsDouble();
      randNormValueList.set(dimInd, randNormValue);
    });

    return new DataPoint(randNormValueList);
  }

  private void assignCluster() {
    for (Point point : points) {
      double lowestDistance = Double.MAX_VALUE;
      Cluster<Point> closestCluster = clusters.get(0);

      for (Cluster<Point> cluster : clusters) {
        double centroidDistance = point.distance(cluster.centroid);

        if (centroidDistance < lowestDistance) {
          lowestDistance = centroidDistance;
          closestCluster = cluster;
        }
      }

      closestCluster.pointList.add(point);
    }
  }

  /**
   * Calculate and set the centroid of each cluster
   */
  private void generateCentroids() {
    for (Cluster<Point> cluster : clusters) {
      if (cluster.pointList.isEmpty()) {
        continue;
      }

      List<Double> centroid = new ArrayList<>(Collections.nCopies(dimensionCount,0.0));
      for (int dimInd = 0; dimInd < dimensionCount; dimInd++) {
        // norm value of all points at dimInd
        // [:, dimInd]
        Collection<Double> normValueList =
            zscoreColumn(dimInd, cluster.pointList);
        // mean([:,dimInd])
        Double meanNormValue =
            normValueList.stream().mapToDouble(x -> x).average().getAsDouble();
        centroid.set(dimInd, meanNormValue);
      }

      cluster.centroid = new DataPoint(centroid);
    }
  }

  private boolean listsEqual(List<DataPoint> lhs, List<DataPoint> rhs) {
    if (lhs.size() != rhs.size()) {
      return false;
    }

    for (int ptInd = 0; ptInd < lhs.size(); ptInd++) {
      for (int dimInd = 0; dimInd < dimensionCount; dimInd++) {
        if (lhs.get(ptInd).zscore
            .get(dimInd).doubleValue() != rhs.get(ptInd).zscore.get(dimInd).doubleValue()) {
          return false;
        }
      }
    }

    return true;
  }

  private List<DataPoint> centroidList() {
    return clusters.stream().map(cluster -> cluster.centroid)
        .collect(Collectors.toList());
  }

  public List<Cluster<Point>> run(int maxIteration) {
    for (int it = 0; it < maxIteration; it++) {
      // clear points of clusters
      clusters.stream().forEach(cluster -> cluster.pointList.clear());
      assignCluster();
      List<DataPoint> oldCentroidList = new ArrayList<>(centroidList());
      generateCentroids();
      if (listsEqual(oldCentroidList, centroidList())) {
        out.println("Converged after " + it + " iterations.");
        return clusters;
      }
    }

    return clusters;
  }
}
