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

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which relies on the
 * presence of a "module.properties" file in the root of the module classpath
 * @author Phil Zoio
 */
public class InternalModuleDefinitionSource extends BaseInternalModuleDefinitionSource implements ModuleDefinitionSource {

	private String[] moduleNames;

	private String rootModuleName;
	
	public InternalModuleDefinitionSource(String[] moduleNames) {
		this(Impala.getFacade().getModuleLocationResolver(), moduleNames);
	}

	public InternalModuleDefinitionSource(ModuleLocationResolver resolver, String[] moduleNames) {
		this(resolver, moduleNames, true);
	}

	public InternalModuleDefinitionSource(ModuleLocationResolver resolver, String[] moduleNames, boolean loadDependendentModules) {
		super(resolver, loadDependendentModules);
		this.moduleNames = moduleNames;
	}

	public RootModuleDefinition getModuleDefinition() {
		inspectModules();
		return buildModules();
	}

	protected RootModuleDefinition buildModules() {
		ModuleDefinitionSource internalModuleBuilder = getModuleBuilder();
		return internalModuleBuilder.getModuleDefinition();
	}

	protected ModuleDefinitionSource getModuleBuilder() {
		InternalModuleBuilder internalModuleBuilder = new InternalModuleBuilder(rootModuleName, getModuleProperties(), getChildren());
		return internalModuleBuilder;
	}

	void inspectModules() {
		buildMaps();
		this.rootModuleName = determineRootDefinition();
	}

	void buildMaps() {
		String[] thisModuleNames = this.moduleNames;
		buildMaps(thisModuleNames);
	}

	void buildMaps(String[] thisModuleNames) {
		String[] moduleNames = thisModuleNames;
		while (moduleNames.length != 0) {
			loadProperties(moduleNames);
			extractParentsAndChildren(moduleNames);
			moduleNames = buildMissingModules();
		}
	}

	String determineRootDefinition() {
		String parentName = null;
		
		//go through and check that all modules have children but not parents
		for (String moduleName : getChildren().keySet()) {
			if (getParents().get(moduleName) == null) {
				
				if (parentName != null) {
					throw new ConfigurationException("Module hierarchy can only have one root module. This one has at least two: '" + moduleName + "' and '" + parentName + "'.");
				}
				
				parentName = moduleName;
			}
		}
		
		if (parentName == null) {
			if (moduleNames.length > 1) {
				throw new ConfigurationException("Module hierarchy does not have a root module.");
			} else {
				parentName = moduleNames[0];
			}
		}
		
		return parentName;
	}

	protected String getRootModuleName() {
		return rootModuleName;
	}

}
