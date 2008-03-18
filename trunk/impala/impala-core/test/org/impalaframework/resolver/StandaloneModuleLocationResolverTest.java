/*
 * Copyright 2007-2008 the original author or authors.
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

import java.io.IOException;
import java.util.Properties;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class StandaloneModuleLocationResolverTest extends TestCase {

	private Properties props;

	private StandaloneModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		props = new Properties();
		super.setUp();
		System.clearProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY);
		System.clearProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY);
		System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
		System.clearProperty(LocationConstants.APPLICATION_VERSION);
	}

	public void testGetPluginClassLocations() throws IOException {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.module.class.dir", "deploy/classes");
		resolver = new StandaloneModuleLocationResolver(props);
		Resource[] locations = ResourceUtils.toArray(resolver.getApplicationModuleClassLocations("myplugin"));
		Resource actual = locations[0];
		Resource expected = new FileSystemResource(System.getProperty("java.io.tmpdir") + "/myplugin/deploy/classes");
		assertEquals(expected.getFile(), actual.getFile());
	}

	public void testGetModuleTestLocations() throws IOException {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.module.test.dir", "deploy/testclasses");
		resolver = new StandaloneModuleLocationResolver(props);
		Resource[] locations = ResourceUtils.toArray(resolver.getModuleTestClassLocations("project"));
		Resource actual = locations[0];
		Resource expected = new FileSystemResource(System.getProperty("java.io.tmpdir") + "/project/deploy/testclasses");
		assertEquals(expected.getFile(), actual.getFile());
	}

	public void testDefaultRootProperty() {
		resolver = new StandaloneModuleLocationResolver(props);
		FileSystemResource file = new FileSystemResource("../");
		assertEquals(file, resolver.getRootDirectory());
	}

	public void testValidRootDirectory() throws IOException {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		resolver = new StandaloneModuleLocationResolver(props);
		FileSystemResource file = new FileSystemResource(System.getProperty("java.io.tmpdir"));
		assertEquals(file.getFile().
				getPath(), resolver.getRootDirectory().getFile().getPath());
	}

	public void testRootDirectoryNotFile() {
		props.put("workspace.root", ".classpath");
		resolver = new StandaloneModuleLocationResolver(props);

		expectIllegalState("'workspace.root' (.classpath) is not a directory");
	}

	public void testRootDirectoryNotExists() {
		props.put("workspace.root", "a file that does not exist");
		resolver = new StandaloneModuleLocationResolver(props);

		expectIllegalState("'workspace.root' (a file that does not exist) does not exist");
	}

	public void testMergePropertyDefault() {
		resolver = new StandaloneModuleLocationResolver(props);
		resolver.mergeProperty("property.name", "default property value", null);
		assertEquals("default property value", resolver.getProperty("property.name"));
	}

	public void testMergePropertySupplied() {
		props.put("property.name", "supplied property value");
		resolver = new StandaloneModuleLocationResolver(props);
		resolver.mergeProperty("property.name", "supplied property value", null);
		assertEquals("supplied property value", resolver.getProperty("property.name"));
	}

	public void testMergePropertySystem() {
		props.put("property.name", "supplied property value");
		try {
			System.setProperty("property.name", "system property value");
			resolver = new StandaloneModuleLocationResolver(props);
			resolver.mergeProperty("property.name", "system property value", null);
			assertEquals("system property value", resolver.getProperty("property.name"));
		}
		finally {
			System.clearProperty("property.name");
		}
	}

	public void testMergePropertyAddSuffix() {
		props.put("property.name", "supplied property value");
		resolver = new StandaloneModuleLocationResolver(props);
		resolver.mergeProperty("property.name", "supplied property value-", "-");
		assertEquals("supplied property value-", resolver.getProperty("property.name"));
	}

	public void testInit() {
		resolver = new StandaloneModuleLocationResolver(props);
		assertNotNull(resolver.getProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY));
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
