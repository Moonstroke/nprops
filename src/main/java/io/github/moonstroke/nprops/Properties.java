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
	 * The properties are written in UTF-8.
	 *
	 * @param outputStream The stream where to write
	 *
	 * @throws IOException if an error occurs while writing
	 */
	public void store(OutputStream outputStream) throws IOException {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}

	/**
	 * Write the properties of this object to the given output stream, preceded with
	 * the given comments.
	 *
	 * The properties are written in UTF-8.
	 *
	 * If the comments consist of several lines (i.e. it contains line separator
	 * characters), the comment characters are injected by the method.
	 *
	 * On the other hand, if the comments are null, no leading comment at all is
	 * written to the stream.
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
