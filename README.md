# NProps

New Properties for Java

Joachim “Moonstroke” MARIE - 2024

## Description

This project aims to offer a simple and handy substitute to the standard class
`java.util.Properties`. It consists of a single class file, that can be dropped
in a project and used almost out of the box (only the package name must be
updated).

## Main differences with `java.util.Properties`

Although this project aims to provide a substitute to the mentioned class,
there are some notable deviations. Some things are done differently, or not at
all, whether it be to get rid of historical but obsolete aspects, or due to
divergences in view; some features are deliberately left out to keep the
project simple. Below is a list, hopefully complete, of these differences.

1. Not a hash table.

	The properties class does not extend `java.util.Hashtable`. It *is* not a
	properties map, it *contains* a properties map, and offers methods to
	interact with it indirectly. This also means that the public methods of the
	`Map` API are unavailable in this class.

2. Unicode by default.

	Instead of using Latin-1 as the default encoding, this project uses UTF-8.
	It will be compatible for the most part, but this removes the need for
	Unicode escape sequences (`\uXXXX` constructs), allowing for a more
	locale-friendly property file.

3. No secondary map for default values.

	This is unnecessary, really. One can simply call the `setProperty` for each
	default value before loading a properties file.

4. Dropped support for XML I/O.

	For the most part, it is the `.properties` format that is used to store
	properties; it is clear, legible and concise. The XML format is far seldom
	used, so support is not essential. Moreover, it is more verbose and less
	convenient.

5. No timestamp comment when writing to a file

	The systematic injection of the current date and time as header of the file
	produced breaks reproducibility and is usually not required. Thus, this
	class does not generate it by default. If it is really necessary, it can be
	passed as the `comments`	parameter to the 2-argument overload of the
	method `store`.

6. No non-valued properties

	Property definitions where the delimiter and value are both omitted are not
	accepted. This syntax is not clear, having a trailing delimiter conveys
	more clearly the idea that the property value is empty.

7. No colon as delimiter

	There is no point in allowing several distinct delimiters. One is enough,
	and allows for homogeneous properties files. Also, this allows to use URIs
	(with a protocol) as keys.

8. No silent discard of non-escaping backslashes

	Simply ignoring backslashes that are not part of an escape sequence is one
	of the most decried features of the standard `Properties` class. This one
	instead considers them to be malformed input and fails loudly.
