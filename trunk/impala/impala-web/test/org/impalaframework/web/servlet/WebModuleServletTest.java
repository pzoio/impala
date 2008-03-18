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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.ServletModuleDefinition;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.impalaframework.web.servlet.WebModuleServlet;

public class WebModuleServletTest extends TestCase {

	private ServletContext servletContext;

	private ServletConfig servletConfig;

	private String servletName;

	private WebModuleServlet servlet;
	
	private String projectName = "p1";

	@Override
	@SuppressWarnings("serial")
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		servletConfig = createMock(ServletConfig.class);
		servletName = "servletName";
		servlet = new WebModuleServlet() {
			public ServletConfig getServletConfig() {
				return servletConfig;
			}
		};
	}

	public final void testNewModuleDefinition() {

		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition(projectName, "context.xml");
		new WebRootModuleDefinition(simpleRootModuleDefinition, "web-root", new String[] { "web-context.xml" });

		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getInitParameter("rootWebModule")).andReturn("web-root");
		expect(servletConfig.getServletName()).andReturn(servletName);

		replayMocks();

		ModuleDefinition newModuleDefinition = servlet.newModuleDefinition("plugin1", simpleRootModuleDefinition);
		assertEquals(ServletModuleDefinition.class.getName(), newModuleDefinition.getClass().getName());

		verifyMocks();
	}
	
	public final void testMissingModule() {

		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition(projectName, "context.xml");

		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getInitParameter("rootWebModule")).andReturn("web-root");

		replayMocks();

		try {
			servlet.newModuleDefinition("plugin1", simpleRootModuleDefinition);
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to find root module 'web-root' specified using the web.xml parameter 'rootWebModule'", e.getMessage());
		}

		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletConfig);
		verify(servletContext);
	}

	private void replayMocks() {
		replay(servletConfig);
		replay(servletContext);
	}

}