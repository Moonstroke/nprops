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
	void testStoreAddsNoSpacesAroundDelimiters() {
		properties.setProperty("foo", "bar");
		String stored = storeToString(null);
		assertEquals(stored, "foo=bar" + EOL);
	}

	@Test
	void testStoreSeparatesPropertiesWithPlatformEOL() {
		/* expected property file is:
		 * foo=bar
		 * baz=quux
		 */
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString(null);
		assertEquals(stored, "foo=bar" + EOL + "baz=quux" + EOL);
	}

	@Test
	void testStoreStripsCommentsAndWhitespace() {
		/* expected property file is:
		 * # comments
		 * foo=bar
		 * baz=quux
		 */
		String propsStr = "foo = bar\n#comment\n\tbaz = quux";
		loadFromString(propsStr);
		String stored = storeToString("comments");
		assertEquals(stored, "# comments" + EOL + "foo=bar" + EOL + "baz=quux" + EOL);
	}

	@Test
	void testStoreHandlesMultiLineLeadingComments() {
		/* expected property file is:
		 * # multi-line
		 * # comments
		 * foo=bar
		 * baz=quux
		 */
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString("multi-line\ncomments");
		assertEquals(stored, "# multi-line" + EOL + "# comments" + EOL + "foo=bar" + EOL + "baz=quux" + EOL);
	}

	@Test
	void testStoreHandlesCommentsInLeadingComments() {
		/* expected property file is:
		 * # multi-line
		 * # comments
		 * #with lines
		 * #already commented
		 * foo=bar
		 * baz=quux */
		properties.setProperty("foo", "bar");
		properties.setProperty("baz", "quux");
		String stored = storeToString("multi-line\ncomments\n#with lines\n#already commented");
		assertEquals(stored, "# multi-line" + EOL + "# comments" + EOL + "#with lines" + EOL + "#already commented"
		                     + EOL + "foo=bar" + EOL + "baz=quux" + EOL);
	}

	// TODO
}
