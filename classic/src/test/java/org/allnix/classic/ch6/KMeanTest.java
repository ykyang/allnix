package org.allnix.classic.ch6;
/**
 * ./gradlew test --tests KMeanTest gradlew.bat test --tests KMeanTest
 * 
 * @author ykyang@gmail.com
 *
 */

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import jdk.jfr.SettingDefinition;

@TestInstance(Lifecycle.PER_CLASS)
public class KMeanTest {
  // private PrintStream out = new PrintStream(OutputStream.nullOutputStream());
  private PrintStream out = System.out;

  DataPoint pt1;
  DataPoint pt2;
  DataPoint pt3;

  @BeforeAll
  public void setup() {
    // The book uses 5.0 instead 5.1 and it seems it is on the boundary
    // and the result is not always consistent.
    pt1 = new DataPoint(List.of(2.0, 1.0, 1.0));
    pt2 = new DataPoint(List.of(2.0, 2.0, 5.0));
    pt3 = new DataPoint(List.of(3.0, 1.5, 2.5));
  }

  @AfterAll
  public void teardown() {

  }

  @Test
  public void randomDouble() {
    Random random = new Random();
    // origin = min
    // bound = max
    random.doubles(1000, 1.5, 2.0)
        .forEach(v -> assertTrue(1.5 <= v && v < 2.0));
  }

  @Test
  public void normValueSlice() {
    KMean<DataPoint> kmean = new KMean<>(2, List.of(pt1, pt2, pt3));

    List<Double> normValue = null;

    // > normValue[:,0]
//    normValue = kmean.normValueSlice(0);
//    assertIterableEquals(
//        List.of(-0.7071067811865478, -1.224744871391589, -1.111167799007432),
//        normValue);
//    // > normValue[:,1]
//    normValue = kmean.normValueSlice(1);
//    assertIterableEquals(List.of(-0.7071067811865478, 1.224744871391589, 1.313198307917874),
//        normValue);
//    // > normValue[:,2]
//    normValue = kmean.normValueSlice(2);
//    assertIterableEquals(
//        List.of(1.4142135623730947, 0.0, -0.20203050891044225),
//        normValue);
  }
  

  @Test
  public void kmean() {
    KMean<DataPoint> kmean = new KMean<>(2, List.of(pt1, pt2, pt3));
    Collection<DataPoint> points = kmean.getPoints();
    out.println("Norm Value List:");
    for (DataPoint pt : points) {
      out.println(pt.zscore);
    }

    List<Cluster<DataPoint>> clusters = kmean.run(100);

    for (int ind = 0; ind < clusters.size(); ind++) {
      out.println("Cluster " + ind + ": " + clusters.get(ind).pointList);
    }
  }
}
