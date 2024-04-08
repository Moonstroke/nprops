package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NPropsPropertiesStoreTest extends BaseNpropsPropertiesTest {

	private static final String EOL = System.lineSeparator();

	@Test
	void testStoreNullStreamFails() {
		assertThrows(NullPointerException.class, () -> properties.store(null, "comment"));
	}

	@Test
	void testStoreAddsSpacesAroundDelimiters() {
		properties.setProperty("foo", "bar");
		String stored = storeToString(null);
		assertEquals(stored, "foo = bar" + EOL);
	}

	@Test
	void testStoreSeparatesPropertiesWithPlatformEOL() {
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString(null);
		assertEquals(stored, "foo = bar" + EOL + "baz = quux" + EOL);
	}

	@Test
	void testStoreStripsCommentsAndWhitespace() {
		String propsStr = "foo = bar\n#comment\n\tbaz = quux";
		loadFromString(propsStr);
		String stored = storeToString("comments");
		assertEquals(stored, "# comments" + EOL + "foo = bar" + EOL + "baz = quux" + EOL);
	}

	@Test
	void testStoreHandlesMultiLineLeadingComments() {
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString("multi-line\ncomments");
		assertEquals(stored, "# multi-line" + EOL + "# comments" + EOL + "foo = bar" + EOL + "baz = quux" + EOL);
	}

	// TODO
}
