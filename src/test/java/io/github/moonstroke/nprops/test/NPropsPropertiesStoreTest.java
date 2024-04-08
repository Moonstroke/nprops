package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NPropsPropertiesStoreTest extends BaseNpropsPropertiesTest {

	@Test
	void testStoreNullStreamFails() {
		assertThrows(NullPointerException.class, () -> properties.store(null, "comment"));
	}

	@Test
	void testStoreAddsSpacesAroundDelimiters() {
		properties.setProperty("foo", "bar");
		String stored = storeToString(null);
		assertEquals(stored, "foo = bar" + System.lineSeparator());
	}

	@Test
	void testStoreSeparatesPropertiesWithPlatformEOL() {
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString(null);
		assertEquals(stored, "foo = bar" + System.lineSeparator() + "baz = quux" + System.lineSeparator());
	}

	@Test
	void testStoreStripsCommentsAndWhitespace() {
		String propsStr = "foo = bar\n#comment\n\tbaz = quux";
		loadFromString(propsStr);
		String stored = storeToString("comments");
		assertEquals(stored, "# comments" + System.lineSeparator() + "foo = bar" + System.lineSeparator() + "baz = quux"
		             + System.lineSeparator());
	}

	@Test
	void testStoreHandlesMultiLineLeadingComments() {
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString("multi-line\ncomments");
		assertEquals(stored, "# multi-line" + System.lineSeparator() + "# comments" + System.lineSeparator()
		             + "foo = bar" + System.lineSeparator() + "baz = quux" + System.lineSeparator());
	}

	// TODO
}
