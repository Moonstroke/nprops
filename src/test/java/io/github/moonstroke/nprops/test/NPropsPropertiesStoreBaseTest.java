package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

abstract class NPropsPropertiesStoreBaseTest extends BaseNpropsPropertiesTest {

	private static final String EOL = System.lineSeparator();

	/* Overridden in subclasses to use either the OutputStream or Writer overload */
	protected abstract String storeToString(String comments);
	protected abstract String storeToString();

	@Test
	void testStoreAddsNoSpacesAroundDelimiters() {
		properties.setProperty("foo", "bar");
		String stored = storeToString();
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
		String stored = storeToString();
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
		try {
			properties.load(new StringReader(propsStr));
		} catch (IOException e) {
			fail(e);
		}
		String stored = storeToString("comments");
		assertEquals(stored, "# comments" + EOL + "foo=bar" + EOL + "baz=quux" + EOL);
	}

	@Test
	void testStorePreservesSignificantWhitespace() {
		/* expected property file is:
		 * # comments
		 * foo bar\ =\ baz\ [trailing space]
		 */
		properties.setProperty("foo bar ", " baz ");
		String stored = storeToString("comments");
		assertEquals(stored, "# comments" + EOL + "foo bar\\ =\\ baz\\ " + EOL);
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
