package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class NPropsPropertiesGetTest extends BaseNpropsPropertiesTest {

	@Test
	void testGetNonExistentKeyReturnsNull() {
		assertNull(properties.getProperty("non-existent key"));
	}

	@Test
	void testGetPropertyNonExistentKeyReturnsDefaultValue() {
		String defaultValue = "default value";
		assertSame(properties.getProperty("non-existent key", defaultValue), defaultValue);
	}

	// TODO
}
