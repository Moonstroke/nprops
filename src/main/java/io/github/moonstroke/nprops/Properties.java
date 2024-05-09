package io.github.moonstroke.nprops;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

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
 * {@code \'} and {@code \"} are accepted, and evaluate to the single and
 * double quotes characters, respectively.<br>
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
		throw new UnsupportedOperationException("Not implemented"); // TODO
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
		throw new UnsupportedOperationException("Not implemented"); // TODO
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
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}


	/**
	 * Set the property of given key to the given value.
	 *
	 * Property keys and values cannot be null. Passing The method fails if a null
	 * value is passed for the key; if for the value, this method removes the
	 * property.
	 *
	 * @param key   The property key (not {@code null})
	 * @param value The property value
	 */
	public void setProperty(String key, String value) {
		throw new UnsupportedOperationException("Not implemented"); // TODO
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
		throw new UnsupportedOperationException("Not implemented"); // TODO
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
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}
}
