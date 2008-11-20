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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.DelegatingContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public class ModuleLoaderRegistryTest extends TestCase {
	
	private String rootModuleName = "project1";

	private ModuleLoaderRegistry registry;
	
	@Override
	protected void setUp() throws Exception {
		registry = new ModuleLoaderRegistry();
	}
	public void testNoModuleLoader() {
		try {
			registry.getModuleLoader("unknowntype");
		}
		catch (NoServiceException e) {
			assertEquals("No org.impalaframework.module.ModuleLoader instance available for module definition type unknowntype", e.getMessage());
		}
	}
	
	public void testGetModuleLoader() {
		ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		registry.setModuleLoader(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		registry.setModuleLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));

		ModuleDefinition p = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parent-context.xml" });
		assertTrue(registry.getModuleLoader(p.getType()) instanceof RootModuleLoader);

		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					ModuleDefinition definition) {
				return null;
			}
		};
		registry.setDelegatingLoader("sometype", delegatingLoader);
		assertSame(delegatingLoader, registry.getDelegatingLoader("sometype"));
	}
	
	public void testSetModuleLoaders() {
		Map<String, ModuleLoader> moduleLoaders = setModuleLoaders();
		
		assertEquals(2, moduleLoaders.size());

		Map<String,DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();
		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					ModuleDefinition definition) {
				return null;
			}
		};
		delegatingLoaders.put("key", delegatingLoader);
		registry.setDelegatingLoaders(delegatingLoaders);

		assertEquals(1, delegatingLoaders.size());		
	}
	
	public void testExtraLoaders() throws Exception {
		setModuleLoaders();
		final ModuleLoader extraModuleLoader = EasyMock.createMock(ModuleLoader.class);
		final DelegatingContextLoader extraDelegatingLoader = EasyMock.createMock(DelegatingContextLoader.class);
		
		Map<String, ModuleLoader> extraModuleLoaders = new HashMap<String, ModuleLoader>();
		extraModuleLoaders.put(ModuleTypes.APPLICATION, extraModuleLoader);
		extraModuleLoaders.put("another", extraModuleLoader);
		registry.setExtraModuleLoaders(extraModuleLoaders);
		
		Map<String, DelegatingContextLoader> extraDelegatingLoaders = new HashMap<String, DelegatingContextLoader>();
		extraDelegatingLoaders.put(ModuleTypes.APPLICATION, extraDelegatingLoader);
		extraDelegatingLoaders.put("another", extraDelegatingLoader);
		registry.setExtraDelegatingLoaders(extraDelegatingLoaders);
		
		registry.afterPropertiesSet();
		
		assertSame(extraModuleLoader, registry.getModuleLoader(ModuleTypes.APPLICATION));
		assertSame(extraModuleLoader, registry.getModuleLoader("another"));
		assertSame(extraDelegatingLoader, registry.getDelegatingLoader(ModuleTypes.APPLICATION));
		assertSame(extraDelegatingLoader, registry.getDelegatingLoader("another"));
		
		assertFalse(registry.getModuleLoader(ModuleTypes.ROOT) == extraModuleLoader);
		assertFalse(registry.getDelegatingLoader(ModuleTypes.ROOT) == extraDelegatingLoader);
	}
	
	
	private Map<String, ModuleLoader> setModuleLoaders() {
		ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		Map<String,ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();
		moduleLoaders.put(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		moduleLoaders.put(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));
		registry.setModuleLoaders(moduleLoaders);
		return moduleLoaders;
	}
}
