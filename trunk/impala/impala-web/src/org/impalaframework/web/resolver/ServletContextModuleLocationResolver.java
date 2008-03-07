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

package org.impalaframework.web.resolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

public class ServletContextModuleLocationResolver implements ModuleLocationResolver, ServletContextAware, InitializingBean {

	private String[] rootProjectsArray;

	private String applicationVersion;

	private String relativeModuleRootLocation = "/WEB-INF/modules";

	private ServletContext servletContext;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(rootProjectsArray, "rootProjects cannot be null");
		Assert.notNull(relativeModuleRootLocation);
	}

	public Resource getRootDirectory() {
		return new ServletContextResource(servletContext, relativeModuleRootLocation);
	}
	
	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		String applicationVersionString = StringUtils.hasText(applicationVersion) ? "-" + applicationVersion : "";
		String fullResourceName = relativeModuleRootLocation + "/" + moduleName + applicationVersionString + ".jar";
		Resource servletContextResource = new ServletContextResource(servletContext, fullResourceName);
		return Collections.singletonList(servletContextResource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		throw new UnsupportedOperationException();
	}

	public List<String> getRootProjects() {
		Assert.notNull(rootProjectsArray);
		return Arrays.asList(rootProjectsArray);
	}

	public void setRootProjectsArray(String[] rootProjectsArray) {
		this.rootProjectsArray = rootProjectsArray;
	}

	public void setRootProjectsString(String rootProjects) {
		//FIXME tests
		this.rootProjectsArray = StringUtils.tokenizeToStringArray(rootProjects, " ,");
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public void setRelativeModuleRootLocation(String relativeModuleRootLocation) {
		this.relativeModuleRootLocation = relativeModuleRootLocation;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
