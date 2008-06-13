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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.classloader.CustomClassLoader;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.PropertyUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which relies on the
 * presence of a "module.properties" file in the root of the module classpath
 * @author Phil Zoio
 */
public class InternalModuleDefinitionSource implements ModuleDefinitionSource {

	private static final String PARENT_PROPERTY = "parent";

	private static final String MODULE_PROPERTIES = "module.properties";

	private String[] moduleNames;

	private boolean loadDependendentModules;
	
	private Map<String, Properties> moduleProperties;
	
	private Map<String, String> parents;
	
	private Map<String, Set<String>> children;
	
	//TODO need to figure out where this is to come from
	private ModuleLocationResolver moduleLocationResolver;

	public InternalModuleDefinitionSource(ModuleLocationResolver resolver, String[] moduleNames) {
		this(resolver, moduleNames, true);
	}

	public InternalModuleDefinitionSource(ModuleLocationResolver resolver, String[] moduleNames, boolean loadDependendentModules) {
		super();
		this.moduleLocationResolver = resolver;
		this.moduleNames = moduleNames;
		this.loadDependendentModules = loadDependendentModules;
		this.moduleProperties = new HashMap<String, Properties>();
		this.parents = new HashMap<String, String>();
		this.children = new HashMap<String, Set<String>>();
	}

	public RootModuleDefinition getModuleDefinition() {
		
		buildMaps();
		String rootModuleName = determineRootDefinition();
		
		InternalModuleBuilder internalModuleBuilder = new InternalModuleBuilder(rootModuleName, moduleProperties, children);
		return internalModuleBuilder.getModuleDefinition();
	}

	void buildMaps() {
		String[] moduleNames = this.moduleNames;
		while (moduleNames.length != 0) {
			loadProperties(moduleNames);
			extractParentsAndChildren(moduleNames);
			moduleNames = buildMissingModules();
		}
	}

	String[] buildMissingModules() {
		List<String> missing = new ArrayList<String>();
		//go through and check that all modules have children but not parents
		for (String moduleName : children.keySet()) {
			if (!parents.containsKey(moduleName)) {
				if (!loadDependendentModules) {
					//FIXME
					throw new RuntimeException("FIXME");
				}
				missing.add(moduleName);
			}
		}
		return missing.toArray(new String[0]);
	}

	String determineRootDefinition() {
		String parentName = null;
		
		//go through and check that all modules have children but not parents
		for (String moduleName : children.keySet()) {
			if (parents.get(moduleName) == null) {
				
				if (parentName != null) {
					throw new ConfigurationException("Module hierarchy can only have one root module. This one has at least two: '" + moduleName + "' and '" + parentName + "'.");
				}
				
				parentName = moduleName;
			}
		}
		
		if (parentName == null) {
			throw new ConfigurationException("Module hierarchy does not have a root module.");
		}
		
		return parentName;
	}

	void extractParentsAndChildren(String[] moduleNames) {
		for (String moduleName : moduleNames) {
			Properties properties = moduleProperties.get(moduleName);
			
			String parent = properties.getProperty(PARENT_PROPERTY);
			if (parent != null) {
				parent = parent.trim();
				checkParent(parent, moduleName);
				Set<String> currentChildren = children.get(parent);
				if (currentChildren == null) {
					currentChildren = new LinkedHashSet<String>();
					children.put(parent, currentChildren);
				}
				currentChildren.add(moduleName);
			}
			parents.put(moduleName, parent);
		}
	}

	void checkParent(String parent, String moduleName) {
		if (moduleName.equals(parent)){
			throw new ConfigurationException("Module '" + moduleName + "' illegally declares itself as parent in " + MODULE_PROPERTIES);
		}
	}

	void loadProperties(String[] moduleNames) {
		for (String moduleName : moduleNames) {
			URL resource = getResourceForModule(moduleName, MODULE_PROPERTIES);
			Properties properties = PropertyUtils.loadProperties(resource);
			moduleProperties.put(moduleName, properties);
		}
	}

	URL getResourceForModule(String moduleName, String resourceName) {
		List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleName);
		CustomClassLoader cl = new ModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(locations) );
		NonDelegatingResourceClassLoader ndl = new NonDelegatingResourceClassLoader(cl);
		
		URL resource = ndl.getResource(resourceName);
		
		if (resource == null) {
			throw new ConfigurationException("Application is using internally defined module structure, but no " + MODULE_PROPERTIES + " file is present on the classpath for module " + moduleName);
		}
		return resource;
	}

	Map<String, Properties> getModuleProperties() {
		return moduleProperties;
	}

	Map<String, String> getParents() {
		return parents;
	}

	Map<String, Set<String>> getChildren() {
		return children;
	}

}
