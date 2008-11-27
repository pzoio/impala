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

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationModuleTypeReader implements TypeReader {

	//FIXME Ticket #21 - implements extracting depends-on into module definition
	
	/**
	 * Constructs new {@link ModuleDefinition} from the supplied properties 
	 */
	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		Assert.notNull(moduleName, "moduleName cannot be null");
		Assert.notNull(properties, "properties cannot be null");
		String[] locationsArray = TypeReaderUtils.readContextLocations(properties);
		return newDefinition(parent, moduleName, locationsArray, new String[0]);
	}

	/**
	 * Constructs new {@link ModuleDefinition} from XML using the supplied {@link Element} instance.
	 */
	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, 
			Element definitionElement) {
		List<String> contextLocations = TypeReaderUtils.readContextLocations(definitionElement);
		
		String[] locationsArray = contextLocations.toArray(new String[contextLocations.size()]);
		return newDefinition(parent, moduleName, locationsArray, new String[0]);
	}

	/**
	 * Populates the supplied {@link ModuleDefinition} properties from the supplied XML {@link Element}
	 */
	public void readModuleDefinitionProperties(Properties properties, 
			String moduleName, 
			Element definitionElement) {
		List<String> contextLocations = TypeReaderUtils.readContextLocations(definitionElement);
		properties.setProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, StringUtils.collectionToCommaDelimitedString(contextLocations));
	}

	protected ModuleDefinition newDefinition(ModuleDefinition parent, 
			String moduleName, 
			String[] locationsArray, 
			String[] dependencyNames) {
		return new SimpleModuleDefinition(parent, dependencyNames, moduleName, locationsArray);
	}

}
