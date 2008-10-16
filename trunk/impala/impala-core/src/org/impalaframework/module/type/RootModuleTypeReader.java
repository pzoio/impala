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

package org.impalaframework.module.type;

import static org.impalaframework.module.ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT;
import static org.impalaframework.module.ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT;

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class RootModuleTypeReader implements TypeReader {
	
	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		Assert.isNull(parent, "Root module cannot have a non-null parent");
		Assert.notNull(moduleName, "moduleName not set");
		Assert.notNull(properties, "properties not set");
		
		String configLocations = properties.getProperty(CONTEXT_LOCATIONS_ELEMENT);
		String[] configLocationsArray = StringUtils.tokenizeToStringArray(configLocations, ",", true, true);
		
		String rootProjectNames = properties.getProperty(ROOT_PROJECT_NAMES_ELEMENT);
		String[] rootProjectNamesArray = StringUtils.tokenizeToStringArray(rootProjectNames, ",", true, true);
		if (rootProjectNamesArray == null || rootProjectNamesArray.length == 0) {
			rootProjectNamesArray = new String[]{ moduleName };
		}		
		
		SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(rootProjectNamesArray, configLocationsArray);
		return definition;
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Element definitionElement) {
		Assert.isNull(parent, "Root module cannot have a non-null parent");
		Assert.notNull(definitionElement, "definitionElement not set");		
		
		List<String> locationNames = getLocationNames(definitionElement);
		
		List<String> projectNames = getRootProjectNames(definitionElement,
				locationNames);
		
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(projectNames, locationNames);
		return rootModuleDefinition;
	}

	public void readModuleDefinitionProperties(Properties properties, String moduleName,
			Element definitionElement) {

		List<String> locationNames = getLocationNames(definitionElement);
		List<String> projectNames = getRootProjectNames(definitionElement, locationNames);
		
		properties.put(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, StringUtils.collectionToCommaDelimitedString(locationNames));
		properties.put(ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT, StringUtils.collectionToCommaDelimitedString(projectNames));
	}

	List<String> getRootProjectNames(Element definitionElement,
			List<String> locationNames) {
		List<String> projectNames = TypeReaderUtils.readContextLocations(definitionElement, ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT, ModuleElementNames.NAME_ELEMENT);
		
		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT), ModuleElementNames.ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT);
		}
		return projectNames;
	}

	List<String> getLocationNames(Element definitionElement) {
		List<String> locationNames = TypeReaderUtils.readContextLocations(definitionElement, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
		
		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT), ModuleElementNames.ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
		}
		return locationNames;
	}
	
}
