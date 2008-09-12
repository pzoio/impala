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

package org.impalaframework.web.servlet;

import org.impalaframework.web.WebConstants;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Extension of <code>DispatcherServlet</code> for servlets which are defined
 * not in web.xml but internally within the module using the
 * <code>ServletFactoryBean</code>. At runtime, an instance can be retrieved
 * using <code>ModuleRedirectingServlet</code>
 * 
 * @author Phil Zoio
 */
public class InternalModuleServlet extends DispatcherServlet implements ApplicationContextAware {
	
	private static final long serialVersionUID = 1L;
	
	private WebApplicationContext applicationContext;
	
	public InternalModuleServlet() {
		super();
	}

	@Override
	protected WebApplicationContext initWebApplicationContext()
			throws BeansException {
		onRefresh(applicationContext);
		
		getServletContext().setAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE + getServletName(), this);
		//FIXME we should unpublish this if the application context closes
		return ImpalaServletUtils.publishWebApplicationContext(this, applicationContext);
	}

	@Override
	public void destroy() {
		getServletContext().removeAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE + getServletName());
		super.destroy();
	}

	/* ************ injected properties ************ */

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = ImpalaServletUtils.checkIsWebApplicationContext(getServletName(), applicationContext);
	}
	
}
