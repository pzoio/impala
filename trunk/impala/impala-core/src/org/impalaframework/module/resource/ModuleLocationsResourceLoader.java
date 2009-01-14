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

package org.impalaframework.module.resource;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.spring.resource.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ModuleLocationsResourceLoader implements SpringLocationsResourceLoader {

	private ResourceLoader resourceLoader;

	public ModuleLocationsResourceLoader() {
		super();
	}

	public Resource[] getSpringLocations(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		Assert.notNull(moduleDefinition);
		Assert.notNull(resourceLoader);

		List<String> configLocations = moduleDefinition.getConfigLocations();

		List<Resource> resourceList = new ArrayList<Resource>();
		for (String location : configLocations) {
			Resource resource = resourceLoader.getResource(location, classLoader);
			checkResource(resource, location, moduleDefinition);

			if (resource != null) {
				resourceList.add(resource);
			}
		}
		Resource[] resources = new Resource[resourceList.size()];
		resources = resourceList.toArray(resources);

		return resources;
	}

	protected void checkResource(Resource resource, String location, ModuleDefinition moduleDefinition) {
		if (resource == null || !resource.exists())
			throw new ConfigurationException("Unable to load resource from location '" + location
					+ "' for module definition '" + moduleDefinition.getName() + "'");
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
