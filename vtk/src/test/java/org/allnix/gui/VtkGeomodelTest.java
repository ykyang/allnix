package org.allnix.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class VtkGeomodelTest {
	@BeforeAll
	public void beforeClass() {
		
	}
	
	/**
	 * LayzMap create, put and return default values when the key
	 * does not exist.
	 */
	@Test
	@Tag("second")
	public void testLazyMap() {
		Map<String, Object[]> db = LazyMap.lazyMap(new HashMap<String, Object[]>(), new Factory<Object[]>() {
			@Override
			public Object[] create() {
				return new Object[3];
			}

		});
		
		Object[] objz = db.get("nada");
		assertEquals(3, objz.length);
		
	}
}
