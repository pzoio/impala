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

package org.impalaframework.spring.jmx;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JMXBootstrapContextTest extends TestCase {

	private static final String plugin1 = "sample-module1";

	private static final String plugin2 = "sample-module2";

	private ModuleManagementFactory factory;

	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala");
	}

	public void tearDown() {
		try {
			factory.close();
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}

	public void testBootstrapContext() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml" ,
				"META-INF/impala-jmx-adaptor-bootstrap.xml"});
		Object bean = context.getBean("moduleManagementFactory");
		factory = ObjectUtils.cast(bean, ModuleManagementFactory.class);
		RootModuleDefinition moduleDefinition = new Provider().getModuleDefinition();

		TransitionSet transitions = factory.getModificationExtractorRegistry()
				.getModificationExtractor(ModificationExtractorType.STICKY).getTransitions(null, moduleDefinition);

		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		moduleStateHolder.processTransitions(transitions);

		ModuleManagementOperations operations = (ModuleManagementOperations) factory.getBean("pluginOperations");

		assertEquals("Could not find module duff", operations.reloadModule("duff"));
		assertEquals("Successfully reloaded sample-module1", operations.reloadModule(plugin1));
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getModuleDefinition() {
			return source.getModuleDefinition();
		}
	}
}
