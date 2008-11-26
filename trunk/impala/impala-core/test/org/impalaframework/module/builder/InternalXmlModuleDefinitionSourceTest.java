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

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;

public class InternalXmlModuleDefinitionSourceTest extends TestCase {

	//FIXME Ticket #21 - add test for setting module dependencies
	
	private InternalXmlModuleDefinitionSource moduleDefinitionSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleDefinitionSource = new InternalXmlModuleDefinitionSource(new StandaloneModuleLocationResolver());
	}
	
	public void testGetModuleDefinitionGraph() {
		moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/modulegraph.xml"));
		RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println(moduleDefinition);
		
		ModuleDefinition definition1 = getDefinition(moduleDefinition, "sample-module1");
		assertEquals(ModuleTypes.APPLICATION, definition1.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module1-context.xml"}), definition1.getContextLocations());
		
		ModuleDefinition definition2 = getDefinition(moduleDefinition, "sample-module2");
		getDefinition(definition2, "sample-module3");
		getDefinition(definition2, "sample-module4");
		
	    ModuleDefinition definition5 = moduleDefinition.getSiblingModule("sample-module5");
	    assertNotNull(definition5);
		
		getDefinition(definition5, "sample-module6");
	}
	
	public void testGetModuleDefinition() {
		moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/moduledefinition.xml"));
		RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println(moduleDefinition);
		
		assertEquals(Arrays.asList(new String[]{"parentTestContext.xml"}), moduleDefinition.getContextLocations());
		assertEquals(2, moduleDefinition.getChildDefinitions().size());
		
		ModuleDefinition definition1 = getDefinition(moduleDefinition, "sample-module1");
		assertEquals(ModuleTypes.APPLICATION, definition1.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module1-context.xml"}), definition1.getContextLocations());
		
		ModuleDefinition definition2 = getDefinition(moduleDefinition, "sample-module2");
		assertEquals(ModuleTypes.APPLICATION, definition2.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module2-context.xml"}), definition2.getContextLocations());
		
		ModuleDefinition definition3 = getDefinition(definition2, "sample-module3");
		assertEquals(ModuleTypes.APPLICATION, definition3.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module3-context.xml"}), definition3.getContextLocations());
		
		ModuleDefinition definition4 = getDefinition(definition2, "sample-module4");
		assertEquals(ModuleTypes.APPLICATION_WITH_BEANSETS, definition4.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module4-context.xml"}), definition4.getContextLocations());
		
		SimpleBeansetModuleDefinition beansetDefinition = (SimpleBeansetModuleDefinition) definition4;
		assertEquals("myImports", beansetDefinition.getOverrides().get("alternative").iterator().next());
	}

	public void testNoNames() throws Exception {
		try {
			moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/nonames.xml"));
			moduleDefinitionSource.getModuleDefinition();
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Resource 'class path resource [xmlinternal/nonames.xml]' contains a non-empty 'names' element, which is illegal when using InternalModuleDefinitionSource", e.getMessage());
		}
	}

	public void testInvalidModules() throws Exception {
		try {
			moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/invalidmodule.xml"));
			moduleDefinitionSource.getModuleDefinition();
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Resource 'class path resource [xmlinternal/invalidmodule.xml]' contains no new properties for module 'sample-module4'. Has this module been declared in the 'names' element?", e.getMessage());
		}
	}
	
	private ModuleDefinition getDefinition(ModuleDefinition moduleDefinition,
			String moduleName) {
		ModuleDefinition def = moduleDefinition.getModule(moduleName);
		assertNotNull(def);
		return def;
	}

}
