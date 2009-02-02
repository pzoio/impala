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

package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.createMock;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.web.context.WebApplicationContext;

public class InternalFrameworkIntegrationServletFactoryBeanTest extends
		TestCase {
	
	private InternalFrameworkIntegrationServletFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = new InternalFrameworkIntegrationServletFactoryBean();
		factoryBean.setModuleDefinition(new SimpleModuleDefinition("mymodule"));
		factoryBean.setServletClass(InternalFrameworkIntegrationServlet.class);
		ServletContext servletContext = createMock(ServletContext.class);
		WebApplicationContext applicationContext = createMock(WebApplicationContext.class);
		factoryBean.setServletContext(servletContext);
		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setDelegateServlet(new ModuleProxyServlet());
	}

	public void testWrongType() throws Exception {	
		
		factoryBean.setServletClass(ModuleProxyServlet.class);
		
		try {
			factoryBean.afterPropertiesSet();
		} catch (ConfigurationException e) {
			assertTrue(e.getMessage().contains("must be an instanceof " + InternalFrameworkIntegrationServlet.class.getName()));
		}
	}

	public void testAfterPropertiesSet() throws Exception {	
		
		assertEquals(InternalFrameworkIntegrationServlet.class, factoryBean.getObjectType());
		assertTrue(factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		
		assertTrue(factoryBean.getObject() instanceof InternalFrameworkIntegrationServlet);
	}

}
