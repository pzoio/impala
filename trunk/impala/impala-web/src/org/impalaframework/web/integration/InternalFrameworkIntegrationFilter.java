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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.impalaframework.web.helper.ImpalaServletUtils;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvoker;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * Filter which is design to integrate with web frameworks other than Spring
 * MVC. Performs the same function as <code>InternalFrameworkIntegrationServlet</code>,
 * but works with <code>Filter</code> instances.
 * 
 * @see ModuleProxyServlet
 * @see InternalFrameworkIntegrationServlet
 * @author Phil Zoio
 */
public class InternalFrameworkIntegrationFilter implements javax.servlet.Filter, ApplicationContextAware {
	
	private static final long serialVersionUID = 1L;

	private WebApplicationContext applicationContext;
	
	private HttpServlet delegateServlet;
	
	/**
	 * Determine whether to set the context class loader. This almost certainly
	 * need to be true. Frameworks such as Struts which dynamically instantiate
	 * classes typically use <code>Thread.currentThread().getContextClassLoader()</code> to 
	 * retrieve the class loader with which to instantiate classes. This needs to be set correctly to the 
	 * class loader of the current module's application context to ensure that resource contained within the module
	 * (e.g. Struts action and form classes) can be found using the current thread's context class loader.
	 */
	private boolean setContextClassLoader = true;

	private ClassLoader currentClassLoader;

	private ThreadContextClassLoaderHttpServiceInvoker invoker;

	private ServletContext servletContext;

	private FilterConfig filterConfig;
	
	public InternalFrameworkIntegrationFilter() {
		super();
	}

	public void init(FilterConfig config) throws ServletException {

		//FIXME test
		
		this.filterConfig = config;
		servletContext = config.getServletContext();
		final String filterName = config.getFilterName();
		
		//FIXME ImpalaServletUtils.publishServlet(servletContext, filterName, this);
		ImpalaServletUtils.publishRootModuleContext(servletContext, filterName, applicationContext);
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		ClassLoader moduleClassLoader = applicationContext.getClassLoader();
		if (this.invoker == null || this.currentClassLoader != moduleClassLoader) {
			this.invoker = new ThreadContextClassLoaderHttpServiceInvoker(delegateServlet, setContextClassLoader, moduleClassLoader);
			this.currentClassLoader = moduleClassLoader;
		}
		
		//FIXME this.invoker.invoke(request, response);
	}

	public void destroy() {
		final ServletContext servletContext = filterConfig.getServletContext();
		final String filterName = filterConfig.getFilterName();
		
		//FIXME ImpalaServletUtils.unpublishServlet(servletContext, filterName);
		ImpalaServletUtils.unpublishRootModuleContext(servletContext, filterName);
	}	
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = ImpalaServletUtils.checkIsWebApplicationContext(filterConfig.getFilterName(), applicationContext);
	}
	
	/* ************************ injected setters ************************** */

	public void setDelegateServlet(HttpServlet delegateServlet) {
		this.delegateServlet = delegateServlet;
	}

	public void setSetContextClassLoader(boolean setContextClassLoader) {
		this.setContextClassLoader = setContextClassLoader;
	}

}
