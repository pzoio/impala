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

package org.impalaframework.web.integration;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.spring.module.ModuleDefinitionAware;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * <code>FactoryBean</code> which can be used to instantiate an <code>HttpServlet</code> instance.
 * 
 * <i>Init Parameters</i> are passed in via a <code>Map</code>.
 * @author Phil Zoio
 */
public class ServletFactoryBean implements FactoryBean, ServletContextAware, InitializingBean, DisposableBean, ModuleDefinitionAware, ApplicationContextAware {

	private ServletContext servletContext;
	private Map<String,String> initParameters;
	private HttpServlet servlet;
	private String servletName;
	private Class<?> servletClass;
	private ApplicationContext applicationContext;
	private ModuleDefinition moduleDefintion;

	public Object getObject() throws Exception {
		return servlet;
	}

	public Class<?> getObjectType() {
		return HttpServlet.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/* ***************** InitializingBean implementation **************** */
	
	public final void afterPropertiesSet() throws Exception {
		Assert.notNull(servletClass, "servletClass cannot be null");
		
		if (servletName == null) {
			servletName = moduleDefintion.getName();
		}
		
		servlet = (HttpServlet) BeanUtils.instantiateClass(servletClass);
		Map<String, String> emptyMap = Collections.emptyMap();
		Map<String,String> parameterMap = (initParameters != null ? initParameters : emptyMap);
		IntegrationServletConfig config = newServletConfig(parameterMap);
		
		if (servlet instanceof ApplicationContextAware) {
			ApplicationContextAware awa = (ApplicationContextAware) servlet;
			awa.setApplicationContext(applicationContext);
		}
		
		initServletProperties(servlet);		
		servlet.init(config);
	}

	private IntegrationServletConfig newServletConfig(Map<String, String> parameterMap) {
		IntegrationServletConfig config = new IntegrationServletConfig(parameterMap, this.servletContext, this.servletName);
		return config;
	}

	/**
	 * Hook for subclasses to customise servlet properties
	 */
	protected void initServletProperties(HttpServlet servlet) {
	}
	
	/* ***************** Protected getters **************** */

	protected ServletContext getServletContext() {
		return servletContext;
	}
	
	protected String getServletName() {
		return servletName;
	}
	
	/* ***************** DisposableBean implementation **************** */

	public void destroy() throws Exception {
		servlet.destroy();
	}	
	
	/* ***************** Protected getters **************** */

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected ModuleDefinition getModuleDefintion() {
		return moduleDefintion;
	}
	
	/* ***************** injection setters **************** */

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}	

	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}

	public void setServlet(HttpServlet servlet) {
		this.servlet = servlet;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public void setServletClass(Class<?> servletClass) {
		this.servletClass = servletClass;
	}

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefintion = moduleDefinition;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
