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
	interact with it indirectly.

2. Unicode by default.

	Instead of using Latin-1 as the default encoding, this project uses UTF-8.
	It will be compatible for the most part, but this removes the need for
	Unicode escape sequences (`\uXXXX` constructs) in the properties file.

3. No secondary map for default values.

	This is unnecessary, really. One can simply call the `setProperty` for each
	default value before loading a properties file.

4. Dropped support for XML I/O.

	For the most part, it is the `.properties` format that is used to store
	properties; it is clear, legible and concise. The XML format is far seldom
	used, so support is not essential. Moreover, it is more verbose and less
	convenient.
