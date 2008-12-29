package org.impalaframework.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.exception.ConfigurationException;

public class SimpleContextLocationResolverTest extends TestCase {

	private SimpleContextLocationResolver resolver;
	private Properties properties;
	private StaticPropertiesPropertySource propertySource;
	private List<String> contextLocations;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new SimpleContextLocationResolver();
		propertySource = new StaticPropertiesPropertySource();
		properties = new Properties();
		propertySource.setProperties(properties);
		contextLocations = new ArrayList<String>();
	}

	public void testAddDefaultClassLoaderType() {
		resolver.addClassLoaderType(propertySource, contextLocations);
		assertLocations();
	}

	public void testAddHierarchicalClassLoaderType() {
		properties.setProperty("moduleType", "hierarchical");
		resolver.addClassLoaderType(propertySource, contextLocations);
		assertLocations();
	}

	public void testAddGraphClassLoaderType() {
		properties.setProperty("moduleType", "graph");
		resolver.addClassLoaderType(propertySource, contextLocations);
		assertLocations("impala-graph");
	}

	public void testAddSharedClassLoaderType() {
		properties.setProperty("moduleType", "shared");
		resolver.addClassLoaderType(propertySource, contextLocations);
		assertLocations("impala-shared-loader");
	}

	public void testAddInvalidClassLoaderType() {
		properties.setProperty("moduleType", "invalid");
		try {
			resolver.addClassLoaderType(propertySource, contextLocations);
		} catch (ConfigurationException e) {
			assertEquals("Invalid value for property 'moduleType': invalid", e.getMessage());
		}
	}
	
	private void assertLocations(String... locations) {
		assertEquals(locations.length, contextLocations.size());
		for (int i = 0; i < locations.length; i++) {
			String actualLocation = contextLocations.get(i);
			String expectedLocation = locations[i];
			assertTrue(actualLocation.contains(expectedLocation));
		}
	}
}
