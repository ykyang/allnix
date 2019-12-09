package org.allnix.gui.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ./gradlew test --tests JsonDoubleArrayTest
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class JsonDoubleArrayTest {
	static final private Logger logger = LoggerFactory.getLogger(JsonDoubleArray.class);
	
	@Test
	@Tag("seconds")
	public void testClone() {
		Random random = new Random(13);
		double[] value; // = new double[1000];
		value = random.doubles(1000).toArray();
		JsonDoubleArray jda = new JsonDoubleArray();
		jda.cloneValue(value);
		assertArrayEquals(value, jda.getValue());
		
		//: Cloned so values should be different
		jda.getValue()[0] = -5;
		assertNotEquals(jda.getValue()[0], value[0]);
	}
	@Test
	@Tag("second")
	public void testSet() {
		Random random = new Random(13);
		double[] value; // = new double[1000];
		value = random.doubles(1000).toArray();
		JsonDoubleArray jda = new JsonDoubleArray();
		jda.setValue(value);
		assertArrayEquals(value, jda.getValue());
		
		//: Set so value should be the same
		jda.getValue()[0] = -5;
		assertEquals(jda.getValue()[0], value[0]);
	}
}
