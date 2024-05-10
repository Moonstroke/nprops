package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class NPropsPropertiesLoadInputStreamTest extends NPropsPropertiesLoadBaseTest {

	@Override
	protected void loadFromString(String str) {
		try (InputStream inputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
			properties.load(inputStream);
		} catch (IOException ioe) {
			fail(ioe);
		}
	}

	@Test
	void testLoadNullInputStreamFails() {
		assertThrows(NullPointerException.class, () -> properties.load((InputStream) null));
	}
}
