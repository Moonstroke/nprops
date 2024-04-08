package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

class NPropsPropertiesLoadTest extends BaseNpropsPropertiesTest {

	@Test
	void testLoadNullInputStreamFails() {
		assertThrows(NullPointerException.class, () -> properties.load((InputStream) null));
	}

	@Test
	void testLoadOnePropertySucceeds() {
		loadFromString("foo=bar");
		assertEquals(properties.getProperty("foo"), "bar");
	}

	@Test
	void testLoadIgnoresSurroundingWhitespace() {
		loadFromString("\tfoo = bar\n");
		assertEquals(properties.getProperty("foo"), "bar");
	}

	@Test
	void testLoadFailsWithoutDelimiter() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo"));
	}

	@Test
	void testLoadFailsWithColonAsDelimiter() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo: bar"));
	}

	@Test
	void testLoadFailsOnNonEscapingBackslash() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\\x"));
	}

	// TODO test all authorized backslash escapes

	@Test
	void testLoadIgnoresHashComments() {
		loadFromString("foo = bar\n# comment\nbaz = quux");
		assertEquals(properties.getProperty("foo"), "bar");
		assertEquals(properties.getProperty("baz"), "quux");
	}

	@Test
	void testLoadFailsOnBangComments() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\n! comment\nbaz = quux"));
	}


	/* The following tests reference features deliberately *not* provided, but that might still be implemented in the
	 * future */

	@Test
	void testLoadHasNoInlineComments() {
		loadFromString("foo = bar # not a comment");
		assertEquals(properties.getProperty("foo"), "bar # not a comment");
	}

	/* Special case of testLoadFailsOnNonEscapingBackslash */
	@Test
	void testLoadFailsOnUnicodeEscape() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = b\\u0061r"));
	}
}
