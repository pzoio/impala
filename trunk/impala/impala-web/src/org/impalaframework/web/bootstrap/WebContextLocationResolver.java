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

package org.impalaframework.web.bootstrap;

import java.util.List;

import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;

public class WebContextLocationResolver extends SimpleContextLocationResolver {

	@Override
	public boolean addContextLocations(List<String> contextLocations, PropertySource propertySource) {
		if (!super.addContextLocations(contextLocations, propertySource)) {
		
			addJarModuleLocation(contextLocations, propertySource);
			
			addWebMultiModuleLocation(contextLocations, propertySource);
			return false;
		} else {
			return true;
		}
	}

	protected void addJarModuleLocation(List<String> contextLocations, PropertySource propertySource) {
		BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, WebBootstrapProperties.EMBEDDED_MODE, false);
		
		if (!embeddedMode.getValue()) {
			contextLocations.add("META-INF/impala-web-jar-module-bootstrap.xml");
		}
	}

	protected void addWebMultiModuleLocation(List<String> contextLocations, PropertySource propertySource) {
		BooleanPropertyValue webMultiModule = new BooleanPropertyValue(propertySource, WebBootstrapProperties.WEB_MULTI_MODULE, false);
		
		if (webMultiModule.getValue()) {
			contextLocations.add("META-INF/impala-web-moduleaware.xml");
		}
	}

	protected void addAutoReloadListener(List<String> contextLocations, PropertySource propertySource) {
		BooleanPropertyValue autoReloadModules = new BooleanPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MODULES, false);
		
		if (autoReloadModules.getValue()) {
			contextLocations.add("META-INF/impala-web-listener-bootstrap.xml");
		}
	}

	protected void addDefaultLocations(List<String> contextLocations) {
		contextLocations.add("META-INF/impala-bootstrap.xml");
		contextLocations.add("META-INF/impala-web-bootstrap.xml");
	}

}
