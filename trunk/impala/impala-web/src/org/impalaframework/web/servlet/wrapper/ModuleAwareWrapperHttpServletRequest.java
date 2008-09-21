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

package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * Module-aware implementation of <code>HttpServletRequest</code>.
 * Implementation of <code>getSession()</code>, returns
 * <code>ModuleAwareWrapperHttpSession</code> instance. Otherwise, simply
 * handled by <code>HttpServletWrapper</code> superclass.
 * 
 * @author Phil Zoio
 */
public class ModuleAwareWrapperHttpServletRequest extends
		HttpServletRequestWrapper {

	private static final Log logger = LogFactory.getLog(ModuleAwareWrapperHttpServletRequest.class);

	private final String moduleName;
	private final ServletContext servletContext;

	public ModuleAwareWrapperHttpServletRequest(HttpServletRequest request, String moduleName,
			ServletContext servletContext) {
		super(request);
		Assert.notNull(request);
		Assert.notNull(moduleName);
		Assert.notNull(servletContext);
		this.moduleName = moduleName;
		this.servletContext = servletContext;
	}

	@Override
	public HttpSession getSession() {
		//FIXME test
		
		HttpSession session = super.getSession();
		return wrapSession(session);
	}

	@Override
	public HttpSession getSession(boolean create) {
		//FIXME test
		
		HttpSession session = super.getSession(create);
		return wrapSession(session);
	}
	
	/* ****************** Helper methods ****************** */


	private HttpSession wrapSession(HttpSession session) {
		if (session == null) {
			return null;
		}
		ModuleManagementFactory moduleManagementFactory = ImpalaServletUtils.getModuleManagementFactory(servletContext);
		if (moduleManagementFactory != null) {
			ConfigurableApplicationContext currentModuleContext = moduleManagementFactory.getModuleStateHolder().getModule(moduleName);
			
			if (currentModuleContext != null) {
				return new ModuleAwareWrapperHttpSession(session, currentModuleContext.getClassLoader());
			} else {
				logger.warn("No module application context associated with module: " + moduleName + ". Using unwrapped session");
				return session;
			}
		}
		return session;
	}	

}
