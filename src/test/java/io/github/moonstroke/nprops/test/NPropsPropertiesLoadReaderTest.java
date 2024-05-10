package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

class NPropsPropertiesLoadReaderTest extends NPropsPropertiesLoadBaseTest {

	@Override
	protected void loadFromString(String str) {
		try {
			properties.load(new StringReader(str));
		} catch (IOException ioe) {
			fail(ioe);
		}
	}

	@Test
	void testLoadNullReaderFails() {
		assertThrows(NullPointerException.class, () -> properties.load((Reader) null));
	}
}
