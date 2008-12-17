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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.TransitionProcessor;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;

public class UnloadTransitionProcessor implements TransitionProcessor {

	private static final Log logger = LogFactory.getLog(UnloadTransitionProcessor.class);

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition newSpec,
			ModuleDefinition currentModuleDefinition) {

		logger.info("Unloading module " + currentModuleDefinition.getName());

		boolean success = true;

		RuntimeModule runtimeModule = moduleStateHolder.removeModule(currentModuleDefinition.getName());
		if (runtimeModule != null) {
			try {
				runtimeModule.close();
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle unloading of application module " + currentModuleDefinition.getName(), e);
				success = false;
			}
		}
		return success;
	}
}
