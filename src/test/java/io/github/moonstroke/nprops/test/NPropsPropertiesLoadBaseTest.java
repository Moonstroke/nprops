package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

abstract class NPropsPropertiesLoadBaseTest extends BaseNpropsPropertiesTest {

	/* Overridden in subclasses to use either the InputStream or Reader overload */
	protected abstract void loadFromString(String props);

	@Test
	void testLoadOnePropertySucceeds() {
		loadFromString("foo=bar");
		assertEquals("bar", properties.getProperty("foo"));
	}

	@Test
	void testLoadIgnoresSurroundingWhitespace() {
		loadFromString("\tfoo = bar  \n");
		assertEquals("bar", properties.getProperty("foo"));
	}

	@Test
	void loadDoesNothingOnEmptyInput() {
		loadFromString("");
	}

	@Test
	void loadDoesNothingOnBlankInput() {
		loadFromString("  \t  \n \t  \r\n  ");
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
		assertEquals("bar\nbaz", properties.getProperty("foo"));
	}

	@Test
	void testLoadBackslashRIsCR() {
		/* property file is:
		 * foo = bar\rbaz
		 */
		loadFromString("foo = bar\\rbaz");
		assertEquals("bar\rbaz", properties.getProperty("foo"));
	}

	@Test
	void testLoadBackslashTIsHorizontalTab() {
		/* property file is:
		 * foo = bar\tbaz
		 */
		loadFromString("foo = bar\\tbaz");
		assertEquals("bar\tbaz", properties.getProperty("foo"));
	}

	@Test
	void testLoadBackslashFIsFormFeed() {
		/* property file is:
		 * foo = bar\fbaz
		 */
		loadFromString("foo = bar\\fbaz");
		assertEquals("bar\fbaz", properties.getProperty("foo"));
	}

	@Test
	void testLoadBackslashZeroIsAsciiNul() {
		/* property file is:
		 * foo = bar\0baz
		 */
		loadFromString("foo = bar\\0baz");
		assertEquals("bar\0baz", properties.getProperty("foo"));
	}

	@Test
	void testLoadEscapedBackslash() {
		/* property file is:
		 * foo = bar\\baz
		 */
		loadFromString("foo = bar\\\\baz");
		assertEquals("bar\\baz", properties.getProperty("foo"));
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
		assertEquals("bar", properties.getProperty("f'o\"o"));
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
		assertEquals("baz", properties.getProperty("foo = bar"));
	}

	@Test
	void testLoadEscapedColonInKeyIsTolerated() {
		/* property file is:
		 * foo \: bar=baz
		 */
		loadFromString("foo \\: bar=baz");
		assertNull(properties.getProperty("foo"));
		assertEquals("baz", properties.getProperty("foo : bar"));
	}

	@Test
	void testLoadBackslashBeforeDelimiterInValueIsDiscarded() {
		/* property file is:
		 * foo = bar\=baz
		 */
		loadFromString("foo = bar\\=baz");
		assertEquals("bar=baz", properties.getProperty("foo"));
	}

	@Test
	void testLoadBackslashBeforeColonInValueIsDiscarded() {
		/* property file is:
		 * foo = bar\:baz
		 */
		loadFromString("foo = bar\\:baz");
		assertEquals("bar:baz", properties.getProperty("foo"));
	}

	@Test
	void testLoadEscapedSpaceInKeyIsAccepted() {
		/* property file is:
		 * foo\ bar = baz
		 */
		loadFromString("foo\\ bar = baz");
		assertEquals("baz", properties.getProperty("foo bar"));
	}

	@Test
	void testLoadEscapedWhitespaceIsPreserved() {
		/* property file is:
		 * foo = \ bar\ [trailing space]
		 */
		loadFromString("foo = \\ bar\\ ");
		assertEquals(" bar ", properties.getProperty("foo"));
	}

	@Test
	void testLoadBackslashEscapesLineBreak() {
		/* property file is:
		 * foo = bar \
		 *    other = property
		 */
		loadFromString("foo = bar \\\n   other = property");
		assertEquals("bar other = property", properties.getProperty("foo"));
		assertEquals(properties.getProperty("other"), null);
	}

	@Test
	void testLoadEscapedBackslashDoesNotEscapeLineBreak() {
		/* property file is:
		 * foo = bar \\
		 *    other = property
		 */
		loadFromString("foo = bar \\\\\n   other = property");
		assertEquals("bar \\", properties.getProperty("foo"));
		assertEquals("property", properties.getProperty("other"));
	}

	@Test
	void testLoadFailsOnBackslashBeforeEof() {
		/* property file is:
		 * foo = bar\
		 */
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\\"));
	}

	@Test
	void testLoadIgnoresHashComments() {
		/* property file is:
		 * foo = bar
		 * # comment
		 * baz = quux
		 */
		loadFromString("foo = bar\n# comment\nbaz = quux");
		assertEquals("bar", properties.getProperty("foo"));
		assertEquals("quux", properties.getProperty("baz"));
	}

	@Test
	void testLoadFailsOnBangComments() {
		/* property file is:
		 * foo = bar
		 * ! not a comment
		 * baz = quux
		 */
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = bar\n! not a comment\nbaz = quux"));
	}


	/* The following tests reference features deliberately *not* provided, but that might still be implemented in the
	 * future */

	@Test
	void testLoadHasNoInlineComments() {
		loadFromString("foo = bar # not a comment");
		assertEquals("bar # not a comment", properties.getProperty("foo"));
	}

	/* Special cases of non-handled escape sequence: */

	@Test
	void testLoadFailsOnUnicodeEscape() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = b\\u0061r"));
	}

	@Test
	void testLoadFailsOnOctalSequence() {
		assertThrows(IllegalStateException.class, () -> loadFromString("foo = b\\161r"));
	}
}
