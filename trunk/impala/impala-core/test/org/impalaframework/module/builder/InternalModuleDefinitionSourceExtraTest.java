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

package org.impalaframework.module.builder;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class InternalModuleDefinitionSourceExtraTest extends TestCase {

	private StandaloneModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new StandaloneModuleLocationResolver();
	}

	public void testBuildMissing() {
		String[] moduleNames = new String[] { "sample-module4" };
		InternalModuleDefinitionSource moduleDefinitionSource = 
			new InternalModuleDefinitionSource(resolver, moduleNames, true);
		
		moduleDefinitionSource.loadProperties(moduleNames);
		moduleDefinitionSource.extractParentsAndChildren(moduleNames);
		String[] buildMissingModules = moduleDefinitionSource.buildMissingModules();
		System.out.println(Arrays.toString(buildMissingModules));
		assertEquals(1, buildMissingModules.length);
		assertEquals("sample-module2", buildMissingModules[0]);
	}
	
	public void testGetModuleDefinition() {
		String[] moduleNames = new String[] { "sample-module4" };
		InternalModuleDefinitionSource moduleDefinitionSource = 
			new InternalModuleDefinitionSource(resolver, moduleNames, true);
		
		moduleDefinitionSource.getModuleDefinition();
		
		String rootModuleName = "impala-core";
		Map<String, Set<String>> children = moduleDefinitionSource.getChildren();
		Map<String, String> parents = moduleDefinitionSource.getParents();
		Map<String, Properties> properties = moduleDefinitionSource.getModuleProperties();

		assertTrue(children.containsKey(rootModuleName));
		assertTrue(parents.containsKey(rootModuleName));
		assertTrue(properties.containsKey(rootModuleName));
		
		assertEquals(2, children.size());
		assertEquals(3, parents.size());
		assertEquals(3, properties.size());
	}
	
	
}
