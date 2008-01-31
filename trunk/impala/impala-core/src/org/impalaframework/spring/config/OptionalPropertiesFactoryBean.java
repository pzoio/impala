package org.impalaframework.spring.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Phil Zoio
 */
public class OptionalPropertiesFactoryBean extends PropertiesFactoryBean {

	@Override
	public void setLocation(Resource location) {
		if (location.exists())
			super.setLocations(new Resource[0]);
	}

	@Override
	public void setLocations(Resource[] locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (int i = 0; i < locations.length; i++) {
			if (locations[0].exists()) {
				resources.add(locations[0]);
			} else {
				//FIXME log only at info level
			}
		}
		Resource[] existingArray = new Resource[resources.size()];
		resources.toArray(existingArray);
		super.setLocations(existingArray);
	}
}
