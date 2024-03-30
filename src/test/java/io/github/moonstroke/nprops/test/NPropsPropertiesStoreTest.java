package io.github.moonstroke.nprops.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NPropsPropertiesStoreTest extends BaseNpropsPropertiesTest {

	@Test
	void testStoreNullStreamFails() {
		assertThrows(NullPointerException.class, () -> properties.store(null, "comment"));
	}

	@Test
	void testStoreStripsCommentsAndWhitespace() {
		String propsStr = "foo = bar\n#comment\n\tbaz = quux";
		loadFromString(propsStr);
		String stored = storeToString("comments");
		assertEquals(stored, "# comments\nfoo = bar\nbaz = quux\n");
	}

	// TODO
}
