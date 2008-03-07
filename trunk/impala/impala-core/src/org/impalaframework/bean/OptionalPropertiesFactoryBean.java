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

package org.impalaframework.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Phil Zoio
 */
public class OptionalPropertiesFactoryBean extends PropertiesFactoryBean {

	Log logger = LogFactory.getLog(OptionalPropertiesFactoryBean.class);

	@Override
	public void setLocation(Resource location) {
		if (location.exists())
			super.setLocations(new Resource[] { location });
	}

	@Override
	public void setLocations(Resource[] locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (int i = 0; i < locations.length; i++) {
			final Resource resource = locations[i];
			if (resource.exists()) {
				resources.add(resource);
				if (logger.isDebugEnabled())
					logger.debug("Extracting properties from resource " + resource.getDescription());
			}
			else {
				logger.info("Not extracting properties from resource " + resource.getDescription()
						+ " as this resource does not exist");
			}
		}
		Resource[] existingArray = new Resource[resources.size()];
		resources.toArray(existingArray);
		super.setLocations(existingArray);
	}
}
