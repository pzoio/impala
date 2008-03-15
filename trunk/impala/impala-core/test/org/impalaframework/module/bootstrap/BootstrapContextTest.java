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

package org.impalaframework.module.bootstrap;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.resolver.LocationConstants;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootstrapContextTest extends TestCase {

	private static final String plugin1 = "sample-module1";

	private static final String plugin2 = "sample-module2";

	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala");
	}

	public void tearDown() {
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}

	public void testBootstrapContext() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/impala-bootstrap.xml");
		ModificationExtractorRegistry calculatorRegistry = (ModificationExtractorRegistry) context
				.getBean("modificationExtractorRegistry");
		ModuleLoaderRegistry registry = (ModuleLoaderRegistry) context.getBean("moduleLoaderRegistry");
		
		assertNotNull(registry.getModuleLoader(ModuleTypes.ROOT));
		assertNotNull(registry.getModuleLoader(ModuleTypes.APPLICATION));
		assertNotNull(registry.getModuleLoader(ModuleTypes.APPLICATION_WITH_BEANSETS));

		ModuleStateHolder moduleStateHolder = (ModuleStateHolder) context.getBean("moduleStateHolder");

		RootModuleDefinition definition = new Provider().getModuleDefinition();
		TransitionSet transitions = calculatorRegistry.getModificationExtractor(ModificationExtractorType.STRICT).getTransitions(null, definition);
		moduleStateHolder.processTransitions(transitions);

		ConfigurableApplicationContext parentContext = moduleStateHolder.getRootModuleContext();
		FileMonitor bean = (FileMonitor) parentContext.getBean("bean1");
		bean.lastModified((File) null);
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getModuleDefinition() {
			return spec.getModuleDefinition();
		}
	}
}
