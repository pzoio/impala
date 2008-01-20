/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.resolver;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class PropertyModuleLocationResolverTest extends TestCase {

	private Properties props;

	private PropertyModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		props = new Properties();
		super.setUp();
	}

	public void testGetNoParentProject() {
		try {
			System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
			resolver = new PropertyModuleLocationResolver(props);
			resolver.getParentProject();
			fail();
		}
		catch (ConfigurationException e) {
		}
	}

	public void testGetParentProject() {
		try {
			props.put(LocationConstants.ROOT_PROJECTS_PROPERTY, "wineorder");
			resolver = new PropertyModuleLocationResolver(props);
			assertEquals("wineorder", resolver.getParentProject());
		}
		finally {
			System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		}
	}
	
	public void testGetParentProjectSysProperty() {
		try {
			System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "wineorder");
			resolver = new PropertyModuleLocationResolver(props);
			assertEquals("wineorder", resolver.getParentProject());
		}
		finally {
			System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		}
	}

	public void testGetApplicationPluginSpringLocation() throws IOException {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.module.spring.dir", "deploy/spring");
		resolver = new PropertyModuleLocationResolver(props);
		Resource actual = resolver.getApplicationModuleSpringLocation("myplugin");
		Resource expected = new FileSystemResource(System.getProperty("java.io.tmpdir") + "/myplugin/deploy/spring/myplugin-context.xml");
		
		System.out.println(actual);
		System.out.println(expected);
		
		assertEquals(expected.getFile(), actual.getFile());
	}

	public void testGetSystemPluginLocations() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.system.module.dir", "sysplugins");
		props.put("impala.module.spring.dir", "deploy/spring");
		resolver = new PropertyModuleLocationResolver(props);
		File location = resolver.getSystemPluginSpringLocation("myplugin");
		assertEquals(new File(System.getProperty("java.io.tmpdir")
				+ "/sysplugins/deploy/spring/myplugin/myplugin-context.xml"), location);

		location = resolver.getSystemPluginClassLocation("myplugin");
		assertEquals(new File(System.getProperty("java.io.tmpdir") + "/sysplugins/deploy/spring/myplugin"), location);
	}

	public void testNoSystemPluginLocations() {
		System.clearProperty("impala.system.module.dir");
		props.clear();
		resolver = new PropertyModuleLocationResolver(props);
		try {
			resolver.getSystemPluginSpringLocation("myplugin");
			fail("Should fail because property 'impala.system.module.dir' not set");
		}
		catch (ConfigurationException e) {
			assertEquals("Property 'impala.system.module.dir' not set. You need this to use system plugins", e
					.getMessage());
		}
	}

	public void testGetPluginClassLocations() throws IOException {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.module.class.dir", "deploy/classes");
		resolver = new PropertyModuleLocationResolver(props);
		Resource[] locations = ResourceUtils.toArray(resolver.getApplicationModuleClassLocations("myplugin"));
		Resource actual = locations[0];
		Resource expected = new FileSystemResource(System.getProperty("java.io.tmpdir") + "/myplugin/deploy/classes");
		assertEquals(expected.getFile(), actual.getFile());
	}

	public void testGetPluginTestLocations() throws IOException {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put(LocationConstants.ROOT_PROJECTS_PROPERTY, "myprefix");
		props.put("impala.module.test.dir", "deploy/testclasses");
		resolver = new PropertyModuleLocationResolver(props);
		Resource[] locations = ResourceUtils.toArray(resolver.getModuleTestClassLocations("project"));
		Resource actual = locations[0];
		Resource expected = new FileSystemResource(System.getProperty("java.io.tmpdir") + "/project/deploy/testclasses");
		assertEquals(expected.getFile(), actual.getFile());
	}

	public void testDefaultRootProperty() {
		resolver = new PropertyModuleLocationResolver(props);
		File file = new File("../");
		assertEquals(file, resolver.getRootDirectory());
	}

	public void testValidRootDirectory() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		resolver = new PropertyModuleLocationResolver(props);
		File file = new File(System.getProperty("java.io.tmpdir"));
		assertEquals(file, resolver.getRootDirectory());
	}

	public void testRootDirectoryNotFile() {
		props.put("workspace.root", ".classpath");
		resolver = new PropertyModuleLocationResolver(props);

		expectIllegalState("'workspace.root' (.classpath) is not a directory");
	}

	public void testRootDirectoryNotExists() {
		props.put("workspace.root", "a file that does not exist");
		resolver = new PropertyModuleLocationResolver(props);

		expectIllegalState("'workspace.root' (a file that does not exist) does not exist");
	}

	public void testMergePropertyDefault() {
		resolver = new PropertyModuleLocationResolver(props);
		resolver.mergeProperty("property.name", "default property value", null);
		assertEquals("default property value", resolver.getProperty("property.name"));
	}

	public void testMergePropertySupplied() {
		props.put("property.name", "supplied property value");
		resolver = new PropertyModuleLocationResolver(props);
		resolver.mergeProperty("property.name", "supplied property value", null);
		assertEquals("supplied property value", resolver.getProperty("property.name"));
	}

	public void testMergePropertySystem() {
		props.put("property.name", "supplied property value");
		try {
			System.setProperty("property.name", "system property value");
			resolver = new PropertyModuleLocationResolver(props);
			resolver.mergeProperty("property.name", "system property value", null);
			assertEquals("system property value", resolver.getProperty("property.name"));
		}
		finally {
			System.clearProperty("property.name");
		}
	}

	public void testMergePropertyAddSuffix() {
		props.put("property.name", "supplied property value");
		resolver = new PropertyModuleLocationResolver(props);
		resolver.mergeProperty("property.name", "supplied property value-", "-");
		assertEquals("supplied property value-", resolver.getProperty("property.name"));
	}

	public void testInit() {
		resolver = new PropertyModuleLocationResolver(props);
		assertNull(resolver.getProperty(LocationConstants.SYSTEM_MODULE_DIR_PROPERTY));
		assertNotNull(resolver.getProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY));
		assertNotNull(resolver.getProperty(LocationConstants.MODULE_SPRING_DIR_PROPERTY));
		assertNotNull(resolver.getProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY));
	}

	private void expectIllegalState(String expected) {
		try {
			resolver.getRootDirectory();
			fail();
		}
		catch (ConfigurationException e) {
			try {
				assertTrue(e.getMessage().contains(expected));
			}
			catch (AssertionFailedError ee) {
				System.out.println("Actual: " + e.getMessage());
				System.out.println("Expected: " + expected);
				throw ee;
			}
		}
	}

}
