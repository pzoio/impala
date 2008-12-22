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

package org.impalaframework.web.spring.module;

import javax.servlet.ServletContext;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.spring.module.loader.BaseModuleLoader;
import org.impalaframework.web.spring.servlet.wrapper.ServletContextWrapper;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Implementation of {@link ModuleLoader} which extends {@link BaseModuleLoader}
 * providing additional functionality specific to the web environment.
 * Specifically, it uses the {@link ServletContextWrapper} if present, to provide 
 * a wrapped {@link ServletContext} instance as a proxy to the real {@link ServletContext} 
 * provided via the {@link ServletContextAware#setServletContext(ServletContext)} method.
 * Also, it creates an instance of {@link GenericWebApplicationContext} to use as the
 * {@link ConfigurableApplicationContext} instance.
 * @author Phil Zoio
 */
public class WebModuleLoader extends BaseModuleLoader implements ServletContextAware {

	private ServletContext servletContext;
	
	private ServletContextWrapper servletContextWrapper;

	public WebModuleLoader() {
	}

	public WebModuleLoader(ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext cannot be null");
		this.servletContext = servletContext;
	}

	public GenericWebApplicationContext newApplicationContext(ApplicationContext parent,
			ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		
		ServletContext wrappedServletContext = servletContext;
		
		if (servletContextWrapper != null) {
			wrappedServletContext = servletContextWrapper.wrapServletContext(servletContext, moduleDefinition, classLoader);
		}
		
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
		context.setParent(parent);
		context.setServletContext(wrappedServletContext);
		context.setClassLoader(classLoader);

		return context;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void setServletContextWrapper(ServletContextWrapper servletContextWrapper) {
		this.servletContextWrapper = servletContextWrapper;
	}
	
}
