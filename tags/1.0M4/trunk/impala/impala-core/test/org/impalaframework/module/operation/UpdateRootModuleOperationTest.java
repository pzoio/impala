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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.ModificationExtractor;

public class UpdateRootModuleOperationTest extends BaseModuleOperationTest {

	protected LockingModuleOperation getOperation() {
		UpdateRootModuleOperation operation = new UpdateRootModuleOperation();
		operation.setModificationExtractorRegistry(modificationExtractorRegistry);
		operation.setModuleStateHolder(moduleStateHolder);
		return operation;
	}

	protected ModificationExtractor getModificationExtractor() {
		return stickyModificationExtractor;
	}

	protected RootModuleDefinition getExistingDefinition() {
		return null;
	}
	
	public final void testInvalidArgs() {
		try {
			operation.execute(new ModuleOperationInput(null, null, null));
		}
		catch (IllegalArgumentException e) {
			assertEquals("moduleDefinitionSource is required as it specifies the new module definition to apply in org.impalaframework.module.operation.UpdateRootModuleOperation", e.getMessage());
		}
	}
	
	public final void testExecute() {

		ModuleDefinitionSource moduleDefinitionSource = EasyMock.createMock(ModuleDefinitionSource.class);
		
		expect(moduleDefinitionSource.getModuleDefinition()).andReturn(newDefinition);
		
		ModificationExtractor modificationExtractor = getModificationExtractor();
		expect(modificationExtractor.getTransitions(null, newDefinition)).andReturn(transitionSet);
		
		RootModuleDefinition existingDefinition = getExistingDefinition();
		expect(strictModificationExtractor.getTransitions(existingDefinition, newDefinition)).andReturn(transitionSet);

		moduleStateHolder.processTransitions(transitionSet);
		
		replayMocks();
		replay(moduleDefinitionSource);

		assertEquals(ModuleOperationResult.TRUE, operation.doExecute(new ModuleOperationInput(moduleDefinitionSource, null, null)));
		
		verifyMocks();
		verify(moduleDefinitionSource);
	}

}
