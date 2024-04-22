package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
	void testLoadBackslashNIsLF() {
		/* property file is:
		 * foo = bar\nbaz
		 */
		loadFromString("foo = bar\\nbaz");
		assertEquals(properties.getProperty("foo"), "bar\nbaz");
	}

	@Test
	void testLoadBackslashRIsCR() {
		/* property file is:
		 * foo = bar\rbaz
		 */
		loadFromString("foo = bar\\rbaz");
		assertEquals(properties.getProperty("foo"), "bar\rbaz");
	}

	@Test
	void testLoadBackslashTIsHorizontalTab() {
		/* property file is:
		 * foo = bar\tbaz
		 */
		loadFromString("foo = bar\\tbaz");
		assertEquals(properties.getProperty("foo"), "bar\tbaz");
	}

	@Test
	void testLoadBackslashFIsFormFeed() {
		/* property file is:
		 * foo = bar\fbaz
		 */
		loadFromString("foo = bar\\fbaz");
		assertEquals(properties.getProperty("foo"), "bar\fbaz");
	}

	@Test
	void testLoadBackslashZeroIsAsciiNul() {
		/* property file is:
		 * foo = bar\0baz
		 */
		loadFromString("foo = bar\\0baz");
		assertEquals(properties.getProperty("foo"), "bar\0baz");
	}

	@Test
	void testLoadEscapedBackslash() {
		/* property file is:
		 * foo = bar\\baz
		 */
		loadFromString("foo = bar\\\\baz");
		assertEquals(properties.getProperty("foo"), "bar\\baz");
	}

	@Test
	void testLoadBackslashBIsNotAccepted() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\\b"));
	}

	@Test
	void testLoadFailsOnNonEscapingBackslash() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\\x"));
	}

	@Test
	void testLoadEscapedQuotesAreToleratedInKey() {
		/* property file is:
		 * f\'o\"o = bar
		 */
		loadFromString("f\\'o\\\"o = bar");
		assertEquals(properties.getProperty("f'o\"o"), "bar");
	}

	@Test
	void testLoadEscapedQuotesAreToleratedInValue() {
		/* property file is:
		 * foo = \'bar \"
		 */
		loadFromString("foo = \\'bar \\\"");
		assertEquals(properties.getProperty("foo"), "'bar \"");
	}

	@Test
	void testLoadEscapedDelimiterInKeyIsPartOfIt() {
		/* property file is:
		 * foo \= bar=baz
		 */
		loadFromString("foo \\= bar=baz");
		assertNull(properties.getProperty("foo"));
		assertEquals(properties.getProperty("foo = bar"), "baz");
	}

	@Test
	void testLoadEscapedColonInKeyIsTolerated() {
		/* property file is:
		 * foo \: bar=baz
		 */
		loadFromString("foo \\: bar=baz");
		assertNull(properties.getProperty("foo"));
		assertEquals(properties.getProperty("foo : bar"), "baz");
	}

	@Test
	void testLoadDelimiterBackslashInValueIsDiscarded() {
		/* property file is:
		 * foo = bar\=baz
		 */
		loadFromString("foo = bar\\=baz");
		assertEquals(properties.getProperty("foo"), "bar=baz");
	}

	@Test
	void testLoadColonBackslashInValueIsDiscarded() {
		/* property file is:
		 * foo = bar\:baz
		 */
		loadFromString("foo = bar\\:baz");
		assertEquals(properties.getProperty("foo"), "bar:baz");
	}

	@Test
	void testLoadEscapedWhitespaceIsPreserved() {
		/* property file is:
		 * foo = \ bar\ [trailing space]
		 */
		loadFromString("foo = \\ bar\\ ");
		assertEquals(properties.getProperty("foo"), " bar ");
	}

	@Test
	void testLoadBackslashEscapesLineBreak() {
		/* property file is:
		 * foo = bar \
		 * other = property
		 */
		loadFromString("foo = bar\\\n   other = property");
		assertEquals(properties.getProperty("foo"), "barother = property");
		assertEquals(properties.getProperty("other"), null);
	}

	@Test
	void testLoadEscapedBackslashDoesNotEscapeLineBreak() {
		/* property file is:
		 * foo = bar \\
		 * other = property
		 */
		loadFromString("foo = bar\\\\\n   other = property");
		assertEquals(properties.getProperty("foo"), "bar");
		assertEquals(properties.getProperty("other"), "property");
	}

	@Test
	void testLoadIgnoresHashComments() {
		/* property file is:
		 * foo = bar
		 * # comment
		 * baz = quux
		 */
		loadFromString("foo = bar\n# comment\nbaz = quux");
		assertEquals(properties.getProperty("foo"), "bar");
		assertEquals(properties.getProperty("baz"), "quux");
	}

	@Test
	void testLoadFailsOnBangComments() {
		/* property file is:
		 * foo = bar
		 * ! comment
		 * baz = quux
		 */
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\n! comment\nbaz = quux"));
	}


	/* The following tests reference features deliberately *not* provided, but that might still be implemented in the
	 * future */

	@Test
	void testLoadHasNoInlineComments() {
		loadFromString("foo = bar # not a comment");
		assertEquals(properties.getProperty("foo"), "bar # not a comment");
	}

	/* Special cases of non-handled escape sequence: */

	@Test
	void testLoadFailsOnUnicodeEscape() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = b\\u0061r"));
	}

	@Test
	void testLoadFailsOnOctalSequence() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = b\\061r"));
	}
}
