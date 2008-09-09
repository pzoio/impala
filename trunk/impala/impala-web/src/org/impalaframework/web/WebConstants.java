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

package org.impalaframework.web;

import org.springframework.web.context.WebApplicationContext;

public interface WebConstants {

	String MODULE_NAMES_PARAM = "moduleNames";
	
	String ROOT_PROJECT_NAMES_PARAM = "rootProjectNames";
	
	String IMPALA_FACTORY_ATTRIBUTE = WebApplicationContext.class.getName() + ".FACTORY_HOLDER";

	String MODULE_DEFINITION_SOURCE_ATTRIBUTE = WebApplicationContext.class.getName() + ".MODULE_DEFINITION_SOURCE";
	
	String WEBAPP_LOCATION_PARAM = "webappConfigLocation";
	
	String ROOT_WEB_MODULE_PARAM = "rootWebModule";

	String BOOTSTRAP_LOCATIONS_RESOURCE_PARAM = "bootstrapLocationsResource";

	String BOOTSTRAP_LOCATIONS_PROPERTY_PARAM = "bootstrapLocations";
	
	String BOOTSTRAP_MODULES_RESOURCE_PARAM = "bootstrapModulesResource";

	String PARENT_LOCATIONS = "parentLocations";
	
	String MODULE_LOCATIONS_RESOURCE_PARAM = "moduleLocationsResource";
	
	String SERVLET_MODULE_ATTRIBUTE = WebApplicationContext.class.getName() + ".SERVLET_MODULE";
	
	/** Default config location for the root context */
	String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
	
	/** Default prefix for building a config location for a namespace */
	String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
	
	/** Default suffix for building a config location for a namespace */
	String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

	String CONTEXT_LOADER_CLASS_NAME = "contextLoaderClassName";

}

