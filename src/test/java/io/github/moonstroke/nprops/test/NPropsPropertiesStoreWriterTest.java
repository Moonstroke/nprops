package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.Test;

class NPropsPropertiesStoreWriterTest extends NPropsPropertiesStoreBaseTest {

	@Override
	protected String storeToString(String comments) {
		Writer writer = new StringWriter();
		try {
			properties.store(writer, comments);
		} catch (IOException ioe) {
			return fail(ioe);
		}
		return writer.toString();
	}

	@Test
	void testStoreNullWriterFails() {
		assertThrows(NullPointerException.class, () -> properties.store((Writer) null, "comment"));
	}
}
