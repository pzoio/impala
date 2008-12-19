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

package org.impalaframework.module.spring.graph;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.classloader.graph.DependencyManager;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.spring.SpringModuleRuntime;
import org.impalaframework.module.spring.SpringRuntimeModule;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringGraphModuleRuntime extends SpringModuleRuntime {

	@Override
	protected ConfigurableApplicationContext getParentApplicationContext(ModuleDefinition definition) {
		
		ConfigurableApplicationContext parentApplicationContext = super.getParentApplicationContext(definition);
		
		if (parentApplicationContext == null) {
			return null;
		}
		
		GraphModuleStateHolder graphModuleStateHolder = ObjectUtils.cast(getModuleStateHolder(), GraphModuleStateHolder.class);
		getNonAncestorDependentContext(definition, parentApplicationContext,
				graphModuleStateHolder);		
		
		return parentApplicationContext;
		
	}

    List<ApplicationContext> getNonAncestorDependentContext(
			ModuleDefinition definition,
			ApplicationContext parentApplicationContext,
			GraphModuleStateHolder graphModuleStateHolder) {
		
		DependencyManager dependencyManager = graphModuleStateHolder.getDependencyManager();
		
		//get the dependencies in correct order
		final List<ModuleDefinition> dependencies = dependencyManager.getOrderedModuleDependencies(definition.getName());	
		
		//remove the current definition from this list
		dependencies.remove(definition);
		
		final List<ApplicationContext> applicationContexts = new ArrayList<ApplicationContext>();
		
		for (ModuleDefinition moduleDefinition : dependencies) {
			
			final String currentName = moduleDefinition.getName();
			final RuntimeModule runtimeModule = graphModuleStateHolder.getModule(currentName);
			if (runtimeModule instanceof SpringRuntimeModule) {
				SpringRuntimeModule spr = (SpringRuntimeModule) runtimeModule;
				applicationContexts.add(spr.getApplicationContext());
			}
		}
		
		List<ApplicationContext> parentList = new ArrayList<ApplicationContext>();
		parentList.add(parentApplicationContext);
		
		if (parentApplicationContext != null) {
			ApplicationContext hierarchyParent = parentApplicationContext;
			while ((hierarchyParent = hierarchyParent.getParent()) != null) {
				parentList.add(hierarchyParent);
			}
		}
		
		applicationContexts.removeAll(parentList);
		return applicationContexts;
	}

}
