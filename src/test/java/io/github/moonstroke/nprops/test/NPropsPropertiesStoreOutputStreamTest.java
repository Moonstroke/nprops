package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class NPropsPropertiesStoreOutputStreamTest extends NPropsPropertiesStoreBaseTest {

	@Override
	protected String storeToString(String comments) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			properties.store(outputStream, comments);
			return outputStream.toString(StandardCharsets.UTF_8);
		} catch (IOException ioe) {
			return fail(ioe);
		}
	}

	@Override
	protected String storeToString() {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			properties.store(outputStream);
			return outputStream.toString(StandardCharsets.UTF_8);
		} catch (IOException ioe) {
			return fail(ioe);
		}
	}

	@Test
	void testStoreNullOutputStreamFails() {
		assertThrows(NullPointerException.class, () -> properties.store((OutputStream) null, "comment"));
	}
}
