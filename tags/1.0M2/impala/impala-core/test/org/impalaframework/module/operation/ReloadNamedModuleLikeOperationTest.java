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

package org.impalaframework.module.operation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class ReloadNamedModuleLikeOperationTest extends BaseModuleOperationTest {

	private ModuleOperationRegistry moduleOperationRegistry;
	
	@Override
	protected void setUp() throws Exception {
		moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		super.setUp();
	}

	protected ModuleOperation getOperation() {
		ReloadModuleNamedLikeOperation operation = new ReloadModuleNamedLikeOperation();
		operation.setModificationExtractorRegistry(modificationExtractorRegistry);
		operation.setModuleStateHolder(moduleStateHolder);
		operation.setModuleOperationRegistry(moduleOperationRegistry);
		return operation;
	}
	
	public final void testInvalidArgs() {
		try {
			operation.execute(new ModuleOperationInput(null, null, null));
		}
		catch (IllegalArgumentException e) {
			assertEquals("moduleName is required as it specifies the name used to match the module to reload in org.impalaframework.module.operation.ReloadModuleNamedLikeOperation", e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public final void testExecuteFound() {
		
		ModuleOperation moduleOperation = createMock(ModuleOperation.class);
		
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		
		expect(newDefinition.findChildDefinition("mymodule", false)).andReturn(newDefinition);

		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadNamedModuleOperation)).andReturn(moduleOperation);
		
		expect(newDefinition.getName()).andReturn("mymodule2");
		expect(moduleOperation.execute(new ModuleOperationInput(null, null, "mymodule2"))).andReturn(ModuleOperationResult.TRUE);
		
		replayMocks();
		replay(moduleOperationRegistry);
		replay(moduleOperation);

		ModuleOperationResult result = operation.execute(new ModuleOperationInput(null, null, "mymodule"));
		assertEquals(true, result.isSuccess());
		assertEquals("mymodule2", result.getOutputParameters().get("moduleName"));
		
		verifyMocks();
		verify(moduleOperationRegistry);
		verify(moduleOperation);
	}
	
	@SuppressWarnings("unchecked")
	public final void testExecuteNotFound() {
		
		ModuleOperationRegistry moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		ModuleOperation moduleOperation = createMock(ModuleOperation.class);
		
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		
		expect(newDefinition.findChildDefinition("mymodule", false)).andReturn(null);
		
		replayMocks();
		replay(moduleOperationRegistry);
		replay(moduleOperation);

		ModuleOperationResult result = operation.execute(new ModuleOperationInput(null, null, "mymodule"));
		assertEquals(false, result.isSuccess());
		
		verifyMocks();
		verify(moduleOperationRegistry);
		verify(moduleOperation);
	}

}
