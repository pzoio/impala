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

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.springframework.util.Assert;

/**
 * Responsible for adding to existing <code>RootModuleDefinition</code> from information provided in the form of properties
 * @author Phil Zoio
 */
public class IncrementalModuleBuilder extends BasePropertiesModuleBuilder implements ModuleDefinitionSource {
	
	private RootModuleDefinition rootModuleDefinition;
	private ModuleDefinition parentDefinition;
	private List<String> modulesToLoad;
	
	public IncrementalModuleBuilder(
			TypeReaderRegistry typeReaderRegistry, 
			RootModuleDefinition rootModuleDefinition, 
			ModuleDefinition parentDefinition, 
			Map<String, Properties> moduleProperties, List<String> modulesToLoad) {
		super(moduleProperties, typeReaderRegistry);
		Assert.notNull(rootModuleDefinition, "rootModuleDefinition cannot be null");
		Assert.notNull(modulesToLoad, "modulesToLoad cannot be null");
		Assert.isTrue(!modulesToLoad.isEmpty(), "modulesToLoad cannot be empty");
		
		//note parentDefinition will be null if root definition of modules to load is 
		
		this.rootModuleDefinition = rootModuleDefinition;
		this.parentDefinition = parentDefinition;
		this.modulesToLoad = modulesToLoad;
	}

	public RootModuleDefinition getModuleDefinition() {
		ModuleDefinition currentParentDefinition = parentDefinition;
		for (String moduleName : modulesToLoad) {
			ModuleDefinition definition = buildModuleDefinition(currentParentDefinition, moduleName);
			
			if (currentParentDefinition == null) {
				Assert.isTrue(!definition.getName().equals(rootModuleDefinition.getName()), "Module definition with no parent cannot be the root module definition");
				rootModuleDefinition.addSibling(definition);
			}
			
			currentParentDefinition = definition;
		}
		return rootModuleDefinition;
	}
	
}
