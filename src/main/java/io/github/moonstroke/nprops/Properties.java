package io.github.moonstroke.nprops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The programmatic interface to a text file where an application's properties
 * are stored.
 *
 * This interface facilitates the externalization of settings from the code, for
 * a clearer distinction between logic and configuration.
 *
 * The class is intended as a trimmed-down replacement for the standard
 * {@link java.util.Properties} class, with at the same time some improvements
 * introduced.
 *
 * <b>Definition of a property</b><br>
 * Properties are key-value pairs, expressed in {@code key = value} form. The
 * value can be empty, but not the key.
 * <p>
 * <b>Handling of whitespace characters</b><br>
 * Horizontal tabulations or spaces are ignored before the key and around the
 * separator as well as after the property value; the various platform-specific
 * forms of line terminator characters at the end of the line are all accepted,
 * even within a single property file.<br>
 * Empty lines, or lines consisting only of whitespace characters, are
 * accepted.
 * <p>
 * <b>Line wrapping</b><br>
 * Values (or keys, although this is not advised) can be split to the next text
 * line, by terminating the line with a single backslash character to indicate
 * continuation. The leading whitespace on the next line is totally ignored (in
 * particular, it is *not* replaced with a single space). This allows to align
 * the continuation text vertically with the previous line without changing the
 * content. Whitespace before the trailing backslash, on the other hand, is
 * preserved.
 * <p>
 * <b>Escape sequences</b><br>
 * An escape sequence is a combination of an escape character (the backslash)
 * followed by a second character, standing for another character, usually one
 * that is not easily printable or that has special meaning. Legal escape
 * sequences are: {@code \n} (line-feed), {@code \t} (horizontal tabulation),
 * {@code \r} (carriage return), {@code \\} (a backslash), {@code \f} (form
 * feed). Some additional sequences are accepted: {@code \=} in the property
 * key stands for a equals sign and removes its meaning of property delimiter.
 * It is also allowed in the value; although this combination has no special
 * meaning, it can convey more explicitly that this character is not meant as a
 * delimiter. The sequence {@code \:} is also accepted in both key and value,
 * even though the colon is not a special character in this implementation (it
 * is not accepted as property delimiter); the sequence evaluates to the colon
 * character itself. This feature is provided for compatibility with the
 * standard {@link java.util.Properties} class. For the same reason, sequences
 * {@code \'}, {@code \"} and {@code \ } (an escaped space character; this one
 * only in the key) are accepted, and evaluate to the single and double quotes
 * characters and the space character, respectively.<br>
 * In contrast with the original class, an illegal escape sequence is not
 * silently ignored, but an error is raised when parsing, to immediately report
 * possible syntax oversights.
 * <p>
 * <b>Comments</b><br>
 * Lines where the first non-whitespace character is a hash sign are considered
 * comments, destined to be read by human users, and are ignored by the code.
 * On the other hand, the exclamation mark is not considered a comment
 * indicator: line starting with this character are parsed for a property like
 * regular lines.<br>
 * Comments are only considered such when starting from the beginning of a line
 * (whitespace excluded), text after a hash sign at the end of a property
 * definition is part of the property's value.
 * <p>
 * Here is an example of what a properties declaration looks like:
 *
 * <pre>
 * <code>
 * projectName=NProps
 * className = io.github.moonstroke.nprops.Properties
 * emptyPropertyValue=
 * # This is a comment.
 *     #This is too!
 * !this is = not a comment
 *     whitespace   = not significant
 * key with spaces = value # still value
 * # The value of this property is "multi-line property value":
 * wrappedProperty = multi-\
 *                   line \
 *                   property \
 *                   value
 * # These are two distinct properties, because the trailing backslash is
 * # escaped:
 * windowsPath = C:\\Users\\Moonstroke\\dev\\nprops\\
 * unixPath = /home/moonstroke/dev/nprops/
 * # On the other hand, this trailing backslash is not escaped so the two lines
 * # are a single property, with value
 * # "C:\Users\Moonstroke\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Eclipse":
 * longWindowsPath = C:\\Users\\Moonstroke\\AppData\\Roaming\\Microsoft\\Windows\\\
 *     Start Menu\\Programs\\Eclipse
 * </code>
 * </pre>
 *
 * @author Moonstroke
 */
public class Properties implements Serializable {

	private static final long serialVersionUID = 8542475994347333139L;

	private final Map<String, String> properties = new HashMap<>();

	/* To be displayed in error messages */
	private transient int lineNumber;


	/**
	 * Load properties from the given input stream into this object.
	 *
	 * The stream is read in UTF-8.
	 *
	 * @param inputStream The stream to read from
	 *
	 * @throws IOException if an error occurs while reading
	 */
	public void load(InputStream inputStream) throws IOException {
		load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}

	/**
	 * Load properties from the given reader into this object.
	 *
	 * @param reader The reader to read from
	 *
	 * @throws IOException if an error occurs when reading
	 */
	public void load(Reader reader) throws IOException {
		BufferedReader br;
		if (reader instanceof BufferedReader) {
			br = (BufferedReader) reader;
		} else {
			br = new BufferedReader(reader);
		}
		load(br);
	}

	private void load(BufferedReader reader) throws IOException {
		String line;
		lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			++lineNumber;
			int firstSignificantIndex = skipWhitespaceFrom(line, 0);
			if (firstSignificantIndex < 0 || line.charAt(firstSignificantIndex) == '#') {
				/* Blank-only or comment line: ignore */
				continue;
			}
			if (isWrapped(line)) {
				line = unwrap(line, reader);
			}
			/* Extract delimiter: skip escaped equals signs (they are part of the key) */
			int delimiterIndex = firstSignificantIndex - 1;
			do {
				delimiterIndex = line.indexOf('=', delimiterIndex + 1);
			} while (delimiterIndex > 0 && line.charAt(delimiterIndex - 1) == '\\');
			if (delimiterIndex == firstSignificantIndex) {
				throw new IllegalStateException("Line " + lineNumber + ": key cannot be empty");
			}
			if (delimiterIndex < 0) {
				throw new IllegalStateException("Line " + lineNumber + ": missing delimiter");
			}
			String key = extractComponent(line, firstSignificantIndex, delimiterIndex);
			int firstSignificantValueIndex = skipWhitespaceFrom(line, delimiterIndex + 1);
			String value;
			if (firstSignificantValueIndex < 0) {
				value = "";
			} else {
				value = extractComponent(line, firstSignificantValueIndex, line.length());
			}
			setProperty(key, value);
		}
	}

	private static boolean isWrapped(String line) {
		int trailingBackslashesCount = 0;
		while (line.charAt(line.length() - 1 - trailingBackslashesCount) == '\\') {
			++trailingBackslashesCount;
		}
		/* Return true if the last backslash is not escaped */
		return trailingBackslashesCount % 2 == 1;
	}

	private String unwrap(String line, BufferedReader reader) throws IOException {
		StringBuilder unwrappedLine = new StringBuilder();
		/* Skip last character (line wrapping indicator) */
		unwrappedLine.append(line, 0, line.length() - 1);
		do {
			line = reader.readLine();
			if (line == null) {
				throw new IllegalStateException("Line " + lineNumber + ": last line cannot be wrapped");
			}
			++lineNumber;
			int firstSignificantIndex = skipWhitespaceFrom(line, 0);
			if (isWrapped(line)) {
				unwrappedLine.append(line, firstSignificantIndex, line.length() - 1);
			} else {
				unwrappedLine.append(line, firstSignificantIndex, line.length());
				break;
			}
		} while (true);
		return unwrappedLine.toString();
	}

	private static int skipWhitespaceFrom(String line, int index) {
		while (Character.isWhitespace(line.charAt(index))) {
			++index;
			if (index == line.length()) {
				return -1;
			}
		}
		return index;
	}

	private String extractComponent(String line, int from, int to) {
		StringBuilder component = new StringBuilder(to - from);
		int i = from;
		/* while, not for, so that the increment is not performed when continue is hit */
		while (i < to) {
			char c = line.charAt(i);
			if (c == '\\') {
				++i;
				/* No need to check if i < line.length, because the line has already been unwrapped */
				c = unescape(line.charAt(i));
			} else if (Character.isWhitespace(c)) {
				/* Is this whitespace run at the end of the requested range? */
				int firstCharNotWsIndex = skipWhitespaceFrom(line, i + 1);
				if (firstCharNotWsIndex < 0 || firstCharNotWsIndex == to) {
					/* ... yes: we are done processing the line. */
					break;
				} else {
					/* ... no: the whitespace must not be discarded */
					component.append(line, i, firstCharNotWsIndex);
					i = firstCharNotWsIndex;
					continue;
				}
				/* Note that firstCharNotWsIndex is never greater than to (because the value passed for it is either the
				 * length of the string or the index of the delimiter) so we don't have to check this case */
			}
			component.append(c);
			++i;
		}
		return component.toString();
	}

	private char unescape(char c) {
		switch (c) {
		case 'f':
			return '\f';
		case 'n':
			return '\n';
		case 'r':
			return '\r';
		case 't':
			return '\t';
		case '0':
			return '\0';
		case '\\':
		case ' ':
		case '\'':
		case '"':
		case '=':
		case ':':
			return c;
		}
		throw new IllegalStateException("Line " + lineNumber + ": invalid escape sequence: \\" + c);
	}

	/**
	 * Write the properties of this object to the given output stream.
	 *
	 * The output is encoded in UTF-8; the properties are written in a syntax
	 * compatible with what the method {@link load} accepts: any delimiter
	 * character in the key is escaped. No whitespace is added around the
	 * delimiter, properties lines are not wrapped at any length, and no blank
	 * lines or comments are output.
	 *
	 * @param outputStream The stream where to write
	 *
	 * @throws IOException if an error occurs while writing
	 */
	public void store(OutputStream outputStream) throws IOException {
		store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
	}

	/**
	 * Write the properties of this object to the given output stream, preceded
	 * with the given comment.
	 *
	 * The properties are encoded in UTF-8, and written in a manner that the
	 * method {@link load} would accept as input.
	 *
	 * If not null, the provided comments are output after a ASCII hash sign
	 * and a single space character. If they consist of several lines (i.e. it
	 * contains line separators), a hash sign and a space are output before
	 * each comment line that does not start with a hash sign.
	 *
	 * @param outputStream The stream where to write
	 * @param comments     The leading comments to output
	 *
	 * @throws IOException if an error occurs while writing
	 */
	public void store(OutputStream outputStream, String comments) throws IOException {
		store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), comments);
	}

	/**
	 * Write the properties of this object to the given writer object.
	 *
	 * The properties are written in a syntax compatible with what the method
	 * {@link load} accepts: any delimiter character in the key is escaped. No
	 * whitespace is added around the delimiter, properties lines are not
	 * wrapped at any length, and no blank lines or comments are output.
	 *
	 * @param writer The writer where to write
	 *
	 * @throws IOException if an error occurs while writing
	 */
	public void store(Writer writer) throws IOException {
		for (Map.Entry<String, String> property : properties.entrySet()) {
			writeKey(property.getKey(), writer);
			writer.write('=');
			writeValue(property.getValue(), writer);
			writer.write(System.lineSeparator());
		}
		writer.flush();
	}

	private static void writeKey(String key, Writer writer) throws IOException {
		for (int i = 0; i < key.length(); ++i) {
			char c = key.charAt(i);
			if (c == '=' || c == '\\') {
				writer.write('\\');
			}
			writer.write(c);
		}
	}

	private static void writeValue(String value, Writer writer) throws IOException {
		if (value.isEmpty()) {
			return;
		}
		writeEndChar(writer, value.charAt(0));
		for (int i = 1; i < value.length() - 1; ++i) {
			char c = value.charAt(i);
			switch (c) {
			case '\f':
				writer.write("\\f");
				break;
			case '\n':
				writer.write("\\n");
				break;
			case '\r':
				writer.write("\\r");
				break;
			case '\t':
				writer.write("\\t");
				break;
			case '\0':
				writer.write("\\0");
				break;
			case '\\':
				writer.write('\\');
				/* Fallthrough intended */
			default:
				writer.write(c);
			}
		}
		if (value.length() > 1) {
			writeEndChar(writer, value.charAt(value.length() - 1));
		}
	}

	/* Characters at the end of a property value need a different processing: a space character must be escaped there */
	private static void writeEndChar(Writer writer, char endChar) throws IOException {
		switch (endChar) {
		case '\f':
			writer.write("\\f");
			break;
		case '\n':
			writer.write("\\n");
			break;
		case '\r':
			writer.write("\\r");
			break;
		case '\t':
			writer.write("\\t");
			break;
		case '\0':
			writer.write("\\0");
			break;
		case ' ':
		case '\\':
			writer.write('\\');
			/* Fallthrough intended */
		default:
			writer.write(endChar);
		}
	}

	/**
	 * Write the properties of this object to the given writer object, preceded
	 * with the given comment.
	 *
	 * The properties are written in a manner that the method {@link load}
	 * would accept as input.
	 *
	 * If not null, the provided comments are output after a ASCII hash sign
	 * and a single space character. If they consist of several lines (i.e. it
	 * contains line separators), a hash sign and a space are output before
	 * each comment line that does not start with a hash sign.
	 *
	 * @param outputStream The writer where to write
	 * @param comments     The leading comments to output
	 *
	 * @throws IOException if an error occurs while writing
	 */
	public void store(Writer writer, String comments) throws IOException {
		if (comments != null) {
			for (String comment : comments.split("\\r?\\n", -1)) {
				if (!comment.isEmpty()) {
					if (comment.charAt(0) != '#') {
						writer.write("# ");
					}
					writer.write(comment);
				}
				writer.write(System.lineSeparator());
			}
		}
		store(writer);
	}


	/**
	 * Set the property of given key to the given value.
	 *
	 * Property keys and values cannot be null. The method fails if a null
	 * value is passed for the key; if for the value, this method removes the
	 * property.
	 * There are additional constraints on the key: it cannot be empty, start or end with a space character, or contain
	 * control characters. Spaces within the key (e.g. the key is a natural-language word group), are however accepted.
	 *
	 * @param key   The property key (not {@code null})
	 * @param value The property value
	 *
	 * @throws NullPointerException     iff key is {@code null}
	 * @throws IllegalArgumentException iff the key is invalid (empty, starts or ends with a space or contains control
	 *                                  chars)
	 */
	public void setProperty(String key, String value) {
		if (key == null) {
			throw new NullPointerException("Cannot set null property");
		}
		if (key.isEmpty() || key.charAt(0) == ' ' || key.charAt(key.length() - 1) == ' '
		    || key.chars().anyMatch(Character::isISOControl)) {
			throw new IllegalArgumentException("Invalid key");
		}
		properties.put(key, value);
	}

	/**
	 * Retrieve the value of the property of given key.
	 *
	 * @param key The property key
	 *
	 * @return the property value, or {@code null} if there is no property with the
	 *         given key
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * Retrieve the value of the property of given key, or the given default.
	 *
	 * @param key          The property key
	 * @param defaultValue The value to return if there is no property with the
	 *                     given key
	 *
	 * @return the property value, or the given default if there is no property with
	 *         the given key
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getOrDefault(key, defaultValue);
	}
}
