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

package org.impalaframework.module.transition;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.TransitionProcessor;
import org.springframework.util.Assert;

public class ReloadTransitionProcessor implements TransitionProcessor {

	private TransitionProcessor loadTransitionProcessor;

	private TransitionProcessor unloadTransitionProcessor;

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition newRootDefinition, ModuleDefinition currentDefinition) {
		Assert.notNull(loadTransitionProcessor);
		Assert.notNull(unloadTransitionProcessor);

		boolean success = true;

		success = unloadTransitionProcessor.process(moduleStateHolder, newRootDefinition, currentDefinition);
		if (success) {
			success = loadTransitionProcessor.process(moduleStateHolder, newRootDefinition, currentDefinition);
		}

		return success;
	}

	public void setLoadTransitionProcessor(TransitionProcessor loadTransitionProcessor) {
		this.loadTransitionProcessor = loadTransitionProcessor;
	}

	public void setUnloadTransitionProcessor(TransitionProcessor unloadTransitionProcessor) {
		this.unloadTransitionProcessor = unloadTransitionProcessor;
	}

}
