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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.CompositePropertySource;
import org.impalaframework.config.PropertiesHolder;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.SimplePropertiesLoader;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.config.SystemPropertiesPropertySource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleLocationsRetriever {

	private static final Log logger = LogFactory.getLog(SimpleLocationsRetriever.class);
	
	private String defaultBootstrapResource = "impala.properties";
	private final ContextLocationResolver delegate;
	
	public SimpleLocationsRetriever(ContextLocationResolver delegate) {
		super();
		Assert.notNull(delegate, "ContextLocationResolver delegate cannot be null");
		this.delegate = delegate;
	}

	public String[] getContextLocations() {
		final ArrayList<String> contextLocations = new ArrayList<String>();
		Properties properties = getProperties();
		List<PropertySource> propertySources = getPropertySources(properties);
		delegate.addContextLocations(contextLocations, new CompositePropertySource(propertySources));

		logger.info("Loaded context loctions: " + contextLocations);
		return contextLocations.toArray(new String[0]);
	}

	protected List<PropertySource> getPropertySources(Properties properties) {
		List<PropertySource> propertySources = new ArrayList<PropertySource>();
		
		//property value sought first in system property
		propertySources.add(new SystemPropertiesPropertySource());
		
		//then in impala properties file
		propertySources.add(new StaticPropertiesPropertySource(properties));
		
		return propertySources;
	}

	protected Properties getProperties() {
		
		final Properties properties = new SimplePropertiesLoader(defaultBootstrapResource).loadProperties();
		PropertiesHolder.getInstance().setProperties(properties);
		return properties;
	}

	public void setDefaultBootstrapResource(String defaultResource) {
		Assert.notNull(defaultResource);
		this.defaultBootstrapResource = defaultResource;
	}

	public String getDefaultBootstrapResource() {
		return defaultBootstrapResource;
	}

}
