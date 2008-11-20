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

package org.impalaframework.web.module;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.resource.ResourceLoader;
import org.impalaframework.web.resource.ServletContextResourceLoader;

public class WebRootModuleLoader extends BaseWebModuleLoader {

	public WebRootModuleLoader() {
		super();
	}
	
	public WebRootModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
		//FIXME issue 25: wire this in
		Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader();
		servletContextResourceLoader.setServletContext(getServletContext());
		resourceLoaders.add(servletContextResourceLoader);
		return resourceLoaders;
	}

}
