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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeInfo;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.web.WebConstants;

public class WebModuleChangeListenerTest extends TestCase {

	public final void testEmpty() throws Exception {

		ServletContext servletContext = createMock(ServletContext.class);

		final WebModuleChangeListener listener = new WebModuleChangeListener();
		listener.setServletContext(servletContext);

		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();
		ModuleChangeEvent event = new ModuleChangeEvent(info);

		replay(servletContext);

		listener.moduleContentsModified(event);

		verify(servletContext);

		info.add(new ModuleChangeInfo("p1"));
		info.add(new ModuleChangeInfo("p2"));
		info.add(new ModuleChangeInfo("p2"));

		event = new ModuleChangeEvent(info);

	}	
	
	public final void testEventsNonEmpty() throws Exception {

		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();

		info.add(new ModuleChangeInfo("p1"));

		ModuleChangeEvent event = new ModuleChangeEvent(info);
		ServletContext servletContext = createMock(ServletContext.class);
		final WebModuleChangeListener listener = new WebModuleChangeListener();
		listener.setServletContext(servletContext);
		ModuleManagementFactory bootstrapFactory = createMock(ModuleManagementFactory.class);
		ModuleOperationRegistry moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		ModuleOperation moduleOperation = createMock(ModuleOperation.class);

		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(bootstrapFactory);
		expect(bootstrapFactory.getModuleOperationRegistry()).andReturn(moduleOperationRegistry);
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadNamedModuleOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(isA(ModuleOperationInput.class))).andReturn(ModuleOperationResult.TRUE);
		
		replay(servletContext);
		replay(bootstrapFactory);
		replay(moduleOperationRegistry);
		replay(moduleOperation);
		
		listener.moduleContentsModified(event);

		verify(servletContext);
		verify(bootstrapFactory);
		verify(moduleOperationRegistry);
		verify(moduleOperation);

	}
}
