package org.allnix.classic.ch6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * ./gradlew test --tests DataPointTest
 * gradlew.bat test --tests DataPointTest
 * 
 * @author ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class DataPointTest {
  @Test
  public void constructor() {
    List<Double> list = Arrays.asList(1.0,2.0); 
    DataPoint p1 = new DataPoint(list);
    assertEquals(2, p1.dimensionCount);
    assertEquals(1.0, p1.normValueList.get(0));
    assertEquals(2.0, p1.normValueList.get(1));
  }
  
  @Test
  public void distance() {
    List<Double> list = Arrays.asList(1.0,2.0); 
    DataPoint p1 = new DataPoint(list);
    list = Arrays.asList(2.0, 2.0);
    DataPoint p2 = new DataPoint(list);
    assertEquals(1.0, p1.distance(p2));
    
    list = Arrays.asList(1.0, 1.0);
    p1 = new DataPoint(list);
    list = Arrays.asList(-2.0, -3.0);
    p2 = new DataPoint(list);
    assertEquals(5.0, p1.distance(p2));
  }
}

