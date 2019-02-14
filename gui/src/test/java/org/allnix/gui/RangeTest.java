package org.allnix.gui;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ./gradlew test --tests RangeTest
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class RangeTest {
	static private Logger logger = LoggerFactory.getLogger(RangeTest.class);
	@BeforeAll
	public void beforeClass() {
		
	}
	/**
	 * Notice how (end == 15) but we have 17 in there
	 */
	@Test
	@Tag("second")
	public void testExpand1() {
		int end = 15;
		String text = "1  ,  2, 13   17";
		int[] expected = new int[] {1,2,13,17};
		
		Range range = new Range(end);
		
		int[] items = range.expand(text);
		assertArrayEquals(expected, items);
	}
	
	@Test
	@Tag("second")
	public void testExpandToSet1() {
		int end = 15;
		String text = "1  ,  2, 13   17";
		Integer[] expected = new Integer[] {1,2,13,17};
		
		Range range = new Range(end);
		
		Set<Integer> items = range.expandToSet(text);
		logger.info(Arrays.toString(items.toArray()));
		assertArrayEquals(expected, items.toArray(new Integer[0]));
	}
	
	@Test
	@Tag("second")
	public void testExpand2() {
		int end = 15;
		String text = "1 : 3 , 8:10 end";
		int[] expected = new int[] {1,2,3,8,9,10,15};
		
		Range range = new Range(end);
		
		int[] items = range.expand(text);
		logger.info(Arrays.toString(items));
		assertArrayEquals(expected, items);
	}
	
	@Test
	@Tag("second")
	public void testExpandToSet2() {
		int end = 15;
		String text = "1 : 3 , 8:10 end";
		Integer[] expected = new Integer[] {1,2,3,8,9,10,15};

		Range range = new Range(end);
		
		Set<Integer> items = range.expandToSet(text);
		logger.info(Arrays.toString(items.toArray()));
		assertArrayEquals(expected, items.toArray(new Integer[0]));
	}
	
	@Test
	@Tag("second")
	public void testExpand3() {
		int end = 15;
		String text = "1 : 3 , 8:10 :end";
		int[] expected = new int[] {1,2,3,8,9,10,11,12,13,14,15};
		
		Range range = new Range(end);
		
		int[] items = range.expand(text);
		logger.info(Arrays.toString(items));
		assertArrayEquals(expected, items);
	}

	
	@Test
	@Tag("second")
	public void testExpandToSpacedSet3() {
		int end = 15;
		String text = "1:3 5:2:10";
		Integer[] expected = new Integer[] {1, 2, 3, 5, 7, 9};
		
		Range range = new Range(end);
		
		Set<Integer> items = range.expandToSpacedSet(text);
		logger.info(Arrays.toString(items.toArray()));
		assertArrayEquals(expected, items.toArray(new Integer[0]));
	}
	
	@Test
	@Tag("second")
	public void testExpandToSpacedSet2() {
		int end = 15;
		String text = "1:2:8";
		Integer[] expected = new Integer[] {1, 3, 5, 7};
		
		Range range = new Range(end);
		
		Set<Integer> items = range.expandToSpacedSet(text);
		logger.info(Arrays.toString(items.toArray()));
		assertArrayEquals(expected, items.toArray(new Integer[0]));
	}
	
	@Test
	@Tag("second")
	public void testExpandToSet3() {
		int end = 15;
		String text = "1 : 3 , 8:10 :end";
		Integer[] expected = new Integer[] {1,2,3,8,9,10,11,12,13,14,15};

		Range range = new Range(end);
		
		Set<Integer> items = range.expandToSet(text);
		logger.info(Arrays.toString(items.toArray()));
		assertArrayEquals(expected, items.toArray(new Integer[0]));
	}
	
	
	
	/**
	 * Behavior changes
	 */
	@Deprecated
	@Test
	@Tag("second")
	public void testExpand4() {
		int end = 15;
		String text = "1 : 3 ,:, 8:10 end";
		int[] expected = new int[] {1,2,3,4,5,6,7,8,9,10,15};
		Range range = new Range(end);
		
		int[] items = range.expand(text);
		logger.info(Arrays.toString(items));
		assertArrayEquals(expected, items);
	}
	
	@Test
	@Tag("second")
	public void testExpand5() {
		int end = 15;
		String text = "0 : 3";
		int[] expected = new int[] {0,1,2,3};
		Range range = new Range(end);
		
		int[] items = range.expand(text);
		logger.info(Arrays.toString(items));
		assertArrayEquals(expected, items);
	}
	
	
	
}
