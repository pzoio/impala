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

package org.impalaframework.module.runtime;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleRuntime;
import org.impalaframework.module.ModuleRuntimeManager;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.transition.LoadTransitionProcessor;

/**
 * Implementation of {@link ModuleRuntimeManager}. Responsible for delegating
 * call to create {@link RuntimeModule} to {@link ModuleRuntime}, and making
 * this available to the {@link ModuleStateHolder}.
 * 
 * @author Phil Zoio
 */
public class DefaultModuleRuntimeManager implements ModuleRuntimeManager {

	private static final Log logger = LogFactory.getLog(LoadTransitionProcessor.class);
	
	private Map<String, ModuleRuntime> moduleRuntimes;
	
	private ModuleStateHolder moduleStateHolder;
	
	public boolean initModule(ModuleDefinition currentDefinition) {
		
		boolean success = true;

		ModuleRuntime moduleRuntime = moduleRuntimes.get("spring");
		final String moduleName = currentDefinition.getName();
		logger.info("Loading definition " + moduleName);
		
		if (moduleStateHolder.getModule(moduleName) == null) {

			try {
				RuntimeModule runtimeModule = moduleRuntime.loadRuntimeModule(currentDefinition);
				moduleStateHolder.putModule(moduleName, runtimeModule);
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle loading of application module " + moduleName, e);
				success = false;
			}

		}
		else {
			logger.warn("Attempted to load module " + moduleName
					+ " which was already loaded. Suggest calling unload first.");
			success = false;
		}

		return success;
	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setModuleRuntimes(Map<String, ModuleRuntime> moduleRuntimes) {
		this.moduleRuntimes = moduleRuntimes;
	}

}
