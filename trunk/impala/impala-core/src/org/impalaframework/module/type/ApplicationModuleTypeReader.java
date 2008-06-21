package org.impalaframework.module.type;

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationModuleTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		Assert.notNull(moduleName, "moduleName cannot be null");
		Assert.notNull(properties, "properties cannot be null");
		String[] locationsArray = null;
		
		String contextLocations = properties.getProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT);
		if (StringUtils.hasText(contextLocations)) {
			locationsArray = StringUtils.tokenizeToStringArray(contextLocations, ", ", true, true);
		}
		return newDefinition(parent, moduleName, locationsArray);
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		List<String> contextLocations = TypeReaderUtils.readContextLocations(definitionElement);
		
		String[] locationsArray = contextLocations.toArray(new String[contextLocations.size()]);
		return newDefinition(parent, moduleName, locationsArray);
	}

	public void readModuleDefinitionProperties(Properties properties, ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		//FIXME test
		List<String> contextLocations = TypeReaderUtils.readContextLocations(definitionElement);
		properties.setProperty(ModuleElementNames.NAME_ELEMENT, moduleName);
		properties.setProperty(ModuleElementNames.PARENT_ELEMENT, parent.getName());
		properties.setProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, StringUtils.collectionToCommaDelimitedString(contextLocations));
	}

	protected ModuleDefinition newDefinition(ModuleDefinition parent, String moduleName, String[] locationsArray) {
		return new SimpleModuleDefinition(parent, moduleName, locationsArray);
	}

}
