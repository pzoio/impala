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

package org.impalaframework.bootstrap;

import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StringPropertyValue;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.InstantiationUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link ContextLocationResolver} with responsiblility for
 * determining which of the context locations to add from those available in the
 * impala-core module. Uses properties from the supplied {@link PropertySource}
 * instance to make these determiniations.
 * 
 * Also responsible for delegating to the JMX
 * {@link org.impalaframework.jmx.bootstrap.JMXContextLocationResolver} if the
 * relevant classes are present on the class path.
 * 
 * @author Phil Zoio
 */
public class SimpleContextLocationResolver implements ContextLocationResolver {

	public boolean addContextLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
		
		logStandaloneProperties(configSettings, propertySource);
		
		if (!explicitlySetLocations(configSettings, propertySource)) {
		
			//FIXME make this method final
			
			addDefaultLocations(configSettings);
			
			//add context associated with class loader type
			addModuleType(configSettings, propertySource);
			
			maybeAddJmxLocations(configSettings, propertySource);
		
			explicitlyAddLocations(configSettings, propertySource);
			
			return false;
		} else {
			return true;
		}
	}

	private void logStandaloneProperties(ConfigurationSettings configSettings, PropertySource propertySource) {
		BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.EMBEDDED_MODE, false);
		//parentClassloaderFirst value is by default the opposite of embedded mode
		BooleanPropertyValue parentClassloaderFirst = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.PARENT_CLASS_LOADER_FIRST, !embeddedMode.getValue());
		StringPropertyValue workspaceRoot = new StringPropertyValue(propertySource, CoreBootstrapProperties.WORKSPACE_ROOT, "../");
		StringPropertyValue moduleClassDirectory = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_CLASS_DIRECTORY, "bin");
		StringPropertyValue moduleResourceDirectory = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_RESOURCE_DIRECTORY, "resources");

		configSettings.addProperty(CoreBootstrapProperties.PARENT_CLASS_LOADER_FIRST, parentClassloaderFirst);
		configSettings.addProperty(CoreBootstrapProperties.WORKSPACE_ROOT, workspaceRoot);
		configSettings.addProperty(CoreBootstrapProperties.MODULE_CLASS_DIRECTORY, moduleClassDirectory);
		configSettings.addProperty(CoreBootstrapProperties.MODULE_RESOURCE_DIRECTORY, moduleResourceDirectory);
		configSettings.addProperty(CoreBootstrapProperties.EMBEDDED_MODE, embeddedMode);
	}
	
	protected boolean explicitlySetLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
		boolean added = addNamedLocations(configSettings, propertySource, CoreBootstrapProperties.ALL_LOCATIONS);
		
		//TODO line left in for backward compatiblity. Remove after 1.0M5
		if (!added) added = addNamedLocations(configSettings, propertySource, CoreBootstrapProperties.BOOTSTRAP_LOCATIONS);
		return added;
	}

	protected void addDefaultLocations(ConfigurationSettings configSettings) {
		configSettings.add("META-INF/impala-bootstrap.xml");
	}

	protected void addModuleType(ConfigurationSettings configSettings,	PropertySource propertySource) {
		
		//check the classloader type
		StringPropertyValue moduleType = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_TYPE, "graph");
		configSettings.addProperty(CoreBootstrapProperties.MODULE_TYPE, moduleType);
		
		final String value = moduleType.getValue();
		if ("shared".equalsIgnoreCase(value)) {
			configSettings.add("META-INF/impala-shared-loader-bootstrap.xml");
		} else if ("graph".equalsIgnoreCase(value)) {
			configSettings.add("META-INF/impala-graph-bootstrap.xml");
			
			StringPropertyValue allLocations = new StringPropertyValue(propertySource, CoreBootstrapProperties.GRAPH_BEAN_VISIBILITY_TYPE, "graphOrdered");
			configSettings.addProperty(CoreBootstrapProperties.GRAPH_BEAN_VISIBILITY_TYPE, allLocations);
		} else if ("hierarchical".equalsIgnoreCase(value)) {
			//nothing to do here
		} else {
			throw new ConfigurationException("Invalid value for property 'classloader.type': " + value);
		}
	}

	protected void maybeAddJmxLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
		ContextLocationResolver c = null;
		try {
			c = InstantiationUtils.instantiate("org.impalaframework.jmx.bootstrap.JMXContextLocationResolver");
			c.addContextLocations(configSettings, propertySource);
		} catch (Exception e) {
		}
	}

	public void explicitlyAddLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
		addNamedLocations(configSettings, propertySource, CoreBootstrapProperties.EXTRA_LOCATIONS);
	}

	private boolean addNamedLocations(ConfigurationSettings configSettings,
			PropertySource propertySource, final String propertyName) {
		StringPropertyValue allLocations = new StringPropertyValue(propertySource, propertyName, null);
		configSettings.addProperty(propertyName, allLocations);
		
		final String allLocationsValue = allLocations.getValue();
		if (allLocationsValue != null) {
			final String[] allLocationsArray = StringUtils.tokenizeToStringArray(allLocationsValue, " ,");
			final String[] fullNamesArray = getFullNames(allLocationsArray);
			for (String location : fullNamesArray) {
				configSettings.add(location);
			}
			return true;
		}
		
		return false;
	}
	
	String[] getFullNames(String[] abridgedNames) {
		String[] fullNames = new String[abridgedNames.length];
		
		for (int i = 0; i < abridgedNames.length; i++) {
			if (!abridgedNames[i].endsWith(".xml"))
				fullNames[i] = "META-INF/impala-" + abridgedNames[i] + ".xml";
			else
				fullNames[i] = abridgedNames[i];
		}
		
		return fullNames;
	}

}
