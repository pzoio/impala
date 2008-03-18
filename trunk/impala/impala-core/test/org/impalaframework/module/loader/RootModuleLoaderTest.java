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

package org.impalaframework.module.loader;

import junit.framework.TestCase;

import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.classloader.ModuleClassLoaderFactory;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class RootModuleLoaderTest extends TestCase {

	private static final String plugin1 = "sample-module1";

	private static final String plugin2 = "sample-module2";	
	
	private static final String rootProjectName = "impala-core";

	private RootModuleLoader moduleLoader;

	private ModuleDefinitionSource source;

	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala-core");
		StandaloneModuleLocationResolver locationResolver = new StandaloneModuleLocationResolver();
		moduleLoader = new RootModuleLoader(locationResolver);
		moduleLoader.setClassLoaderFactory(new ModuleClassLoaderFactory());
		source = new SimpleModuleDefinitionSource(rootProjectName, "parentTestContext.xml", new String[] { plugin1, plugin2 });
	}

	public void tearDown() {
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}

	public final void testGetClassLocations() {
		final Resource[] classLocations = moduleLoader.getClassLocations(source.getModuleDefinition());
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}

	public final void testGetClassLoader() {
		final ClassLoader classLoader = moduleLoader.newClassLoader(source.getModuleDefinition(), null);
		assertTrue(classLoader instanceof ModuleClassLoader);
		assertTrue(classLoader.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));
	}

	public void testGetSpringLocations() {
		final ClassLoader classLoader = moduleLoader.newClassLoader(source.getModuleDefinition(), null);
		final Resource[] springConfigResources = moduleLoader.getSpringConfigResources(source.getModuleDefinition(),
				classLoader);

		assertEquals(1, springConfigResources.length);
		assertEquals(ClassPathResource.class, springConfigResources[0].getClass());
		assertTrue(springConfigResources[0].exists());

	}
	
	public void testGetParentLocations() {

		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala-core, impala-interactive");
		StandaloneModuleLocationResolver locationResolver = new StandaloneModuleLocationResolver();
		moduleLoader = new RootModuleLoader(locationResolver);
		
		Resource[] parentClassLocations = moduleLoader.getClassLocations(source.getModuleDefinition());
		assertEquals(2, parentClassLocations.length);
		assertTrue(parentClassLocations[0].getDescription().contains("impala-core"));
		assertTrue(parentClassLocations[1].getDescription().contains("impala-interactive"));
	}

}
