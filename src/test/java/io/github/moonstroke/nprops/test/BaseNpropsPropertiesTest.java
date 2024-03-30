package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;

import io.github.moonstroke.nprops.Properties;

class BaseNpropsPropertiesTest {

	protected Properties properties;

	@BeforeEach
	void initProperties() {
		properties = new Properties();
	}


	/* Utility functions to convert between the Properties instance and Strings */

	protected final void loadFromString(String str) {
		try (InputStream inputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
			properties.load(inputStream);
		} catch (IOException ioe) {
			fail(ioe);
		}
	}

	protected final String storeToString(String comments) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			properties.store(outputStream, comments);
			return outputStream.toString(StandardCharsets.UTF_8);
		} catch (IOException ioe) {
			fail(ioe);
			return null; /* Not reachable; added to please the compiler */
		}
	}
}
