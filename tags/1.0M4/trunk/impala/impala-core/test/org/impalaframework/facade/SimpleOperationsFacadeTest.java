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

package org.impalaframework.facade;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.spi.ModuleStateHolder;

public class SimpleOperationsFacadeTest extends TestCase {

	public void testSimpleOperationsFacade() {
		
		ModuleManagementFacade managementFacade = createMock(ModuleManagementFacade.class);
		
		expect(managementFacade.getModuleStateHolder()).andReturn(createMock(ModuleStateHolder.class));
		replay(managementFacade);
		
		SimpleOperationsFacade facade = new SimpleOperationsFacade(managementFacade);
		assertNotNull(facade.getModuleStateHolder());
		assertNotNull(facade.getModuleManagementFacade());
		
		verify(managementFacade);
	}

}