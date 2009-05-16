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

package org.impalaframework.web.spring.helper;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.ModuleStateChangeListener;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.spring.module.SpringModuleUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Delegate class responsible for creating {@link WebApplicationContext} on
 * behalf of Servlet instance. Used by
 * {@link org.impalaframework.web.spring.servlet.ExternalModuleServlet} and
 * {@link org.impalaframework.web.spring.integration.ExternalFrameworkIntegrationServlet},
 * which are both servlets set up in <i>WEB-INF/web.xml</i>.
 * 
 * This class encapsulates mechanism for retrieving module from Impala's context
 * and making it available as the Spring context to represent a particular
 * Servlet instance. Supports reloading of the backing
 * <code>ApplicationContext</code> through
 * <code>ModuleStateChangeListener</code>
 * 
 * @author Phil Zoio
 */
public class FrameworkServletContextCreator  {

	private static final Log logger = LogFactory.getLog(FrameworkServletContextCreator.class);
	
	private static final long serialVersionUID = 1L;
	
	private boolean initialized;
	
	private FrameworkServlet servlet;

	public FrameworkServletContextCreator(FrameworkServlet servlet) {
		Assert.notNull(servlet);
		this.servlet = servlet;
	}

	public WebApplicationContext createWebApplicationContext() throws BeansException {

		// the superclass closes the modules
		final ServletContext servletContext = servlet.getServletContext();
		ModuleManagementFacade facade = ImpalaServletUtils.getModuleManagementFacade(servletContext);

		final String servletName = servlet.getServletName();
		ModuleStateHolder moduleStateHolder = facade.getModuleStateHolder();
		
		if (!initialized) {
			
			ModuleStateChangeNotifier moduleStateChangeNotifier = facade.getModuleStateChangeNotifier();
			moduleStateChangeNotifier.addListener(new ModuleStateChangeListener() {

				public void moduleStateChanged(ModuleStateHolder moduleStateHolder, ModuleStateChange change) {
					try {
						servlet.init();
					}
					catch (Exception e) {
						logger.error("Unable to reinitialize servlet " + servlet.getServletName(), e);
					}
				}

				public String getModuleName() {
					return servletName;
				}

				public String getTransition() {
					return Transition.UNLOADED_TO_LOADED;
				}
				
			});
			this.initialized = true;
		}
		
		ApplicationContext context = SpringModuleUtils.getModuleSpringContext(moduleStateHolder, servletName);
		if (context != null) {
			if (context instanceof WebApplicationContext) {
				return (WebApplicationContext) context;
			}
			else {
				throw new ConfigurationException("Module registered under name of servlet '" + servletName
						+ "' needs to be an instance of " + WebApplicationContext.class.getName());
			}
		}
		else {
			throw new ConfigurationException("No module registered under the name of servlet '" + servletName + "'");
		}
	}

}
