package org.allnix.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class ExampleTest {
	static final private Logger logger = LoggerFactory.getLogger(ExampleTest.class);
	
	@BeforeAll
	public void beforeClass() {
		logger.info("@BeoforeAll");
	}
	
	@Test
	@Tag("second")
	public void testEqual() {
		int expected = 1;
		int actual = 1;
		assertEquals(expected, actual);
	}
}
