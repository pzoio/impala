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

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.builder.SingleStringModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class SingleStringModuleDefinitionSourceTest extends TestCase {

	private String rootModuleName = "project1";

	public void testEmptyString() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(rootModuleName , new String[] { "parent-context" });
		String moduleString = "";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(rootDefinition, moduleString);
		RootModuleDefinition result = builder.getModuleDefinition();
		assertSame(result, rootDefinition);
	}
	
	public void testModuleWithoutBeanSpec() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parent-context" });
		String moduleString = " example-hibernate , example-dao ";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(rootDefinition, moduleString);
		RootModuleDefinition result = builder.getModuleDefinition();
		assertSame(result, rootDefinition);
		assertEquals(2, rootDefinition.getModuleNames().size());
		System.out.println(rootDefinition.getModuleNames());
		assertNotNull(result.getModule("example-hibernate"));
		assertNotNull(result.getModule("example-dao"));
	}
	
	public void testModuleWithBeanOverrides() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parent-context" });
		String moduleString = " example-hibernate ,example-service ( null: set1, set2; mock: set3, duff ), example-dao ()";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(rootDefinition, moduleString);
		RootModuleDefinition result = builder.getModuleDefinition();
		assertSame(result, rootDefinition);
		assertEquals(3, rootDefinition.getModuleNames().size());
		System.out.println(rootDefinition.getModuleNames());
		assertNotNull(result.getModule("example-hibernate"));
		assertNotNull(result.getModule("example-dao"));
		assertNotNull(result.getModule("example-service"));
		assertTrue(result.getModule("example-dao") instanceof SimpleBeansetModuleDefinition);
		assertTrue(result.getModule("example-service") instanceof SimpleBeansetModuleDefinition);
	}	
	
	public void testInvalidBrackets() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parent-context" });
		String moduleString = "module (( null: set1, set2; mock: set3, duff )";
		SingleStringSourceDelegate builder = new SingleStringSourceDelegate(rootDefinition, moduleString);
		try {
			builder.doDefinitionSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (ConfigurationException e) {
			assertEquals("Invalid definition string module (( null: set1, set2; mock: set3, duff ). Invalid character '(' at column 9", e.getMessage());
		}
		
		moduleString = "module ( null: set1, set2; mock: set3, duff ))";
		builder = new SingleStringSourceDelegate(rootDefinition, moduleString);
		try {
			builder.doDefinitionSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (ConfigurationException e) {
			assertEquals("Invalid definition string module ( null: set1, set2; mock: set3, duff )). Invalid character ')' at column 46", e.getMessage());
		}
	}

}
