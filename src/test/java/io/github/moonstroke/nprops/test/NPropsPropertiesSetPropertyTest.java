package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NPropsPropertiesSetPropertyTest extends BaseNpropsPropertiesTest {

	@Test
	void testSetProperty() {
		String key = "key";
		String originalValue = "original value";
		properties.setProperty(key, originalValue);
		assertEquals(originalValue, properties.getProperty(key));
	}

	@Test
	void testSetPropertyNullKeyFails() {
		assertThrows(NullPointerException.class, () -> properties.setProperty(null, "value"));
	}

	@Test
	void testSetPropertyEmptyKeyFails() {
		assertThrows(IllegalArgumentException.class, () -> properties.setProperty("", "value"));
	}

	@Test
	void testSetPropertyNullValueRemovesProperty() {
		String key = "key";
		String originalValue = "original value";
		properties.setProperty(key, originalValue);
		properties.setProperty(key, null);
		assertNull(properties.getProperty(key));
	}

	@Test
	void testSetPropertyOverwritesExisting() {
		String key = "key";
		properties.setProperty(key, "initial value");
		String newValue = "new value";
		properties.setProperty(key, newValue);
		assertEquals(newValue, properties.getProperty(key));
	}
}
