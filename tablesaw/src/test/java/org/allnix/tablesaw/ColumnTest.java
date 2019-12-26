package org.allnix.tablesaw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.selection.Selection;

/**
 * Learn Tablesaw Column
 *  
 * https://jtablesaw.github.io/tablesaw/gettingstarted
 * 
 * ./gradlew test --tests ColumnTest
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class ColumnTest {
	static final private Logger logger = LoggerFactory.getLogger(ColumnTest.class);
	@Test
	public void createColumn() {
		logger.info("createColumn:");
		// > create
		double[] numbers = {1, 2, 3, 4};
		DoubleColumn nc = DoubleColumn.create("Test", numbers);
		
		// > print
		logger.info("\n{}", nc.print());
		
		// > get value
		assertEquals(3., nc.getDouble(2));
	}
	
	@Test
	public void operations() {
		logger.info("operations:");
		double[] numbers = {1, 2, 3, 4};
		DoubleColumn nc = DoubleColumn.create("Test", numbers);
		
		DoubleColumn nc2 = nc.multiply(2.);
		logger.info("multiply(2.):\n{}", nc2.print());
		assertEquals(6., nc2.getDouble(2));
		
		Selection filter = nc.isLessThan(3.);
		logger.info("Filter: {}", Arrays.toString(filter.toArray()));
//		assertEquals(1, filter.get(1));
//		assertEquals(0, filter.get(2));
		
		DoubleColumn filtered = nc.where(filter);
		logger.info("Filtered Column:\n{}", filtered.print());
	}
}
