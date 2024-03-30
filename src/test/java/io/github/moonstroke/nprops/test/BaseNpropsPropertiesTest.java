package io.github.moonstroke.nprops.test;

import org.junit.jupiter.api.BeforeEach;

import io.github.moonstroke.nprops.Properties;

class BaseNpropsPropertiesTest {

	protected Properties properties;

	@BeforeEach
	void initProperties() {
		properties = new Properties();
	}
}
