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

package org.impalaframework.jmx.spring;

import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.util.ExceptionUtils;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

@ManagedResource(objectName = "impala:service=moduleManagementOperations", description = "MBean exposing configuration operations Impala application")
public class ModuleManagementOperations {

	private ModuleOperationRegistry moduleOperationRegistry;

	public void init() {
		Assert.notNull(moduleOperationRegistry);
	}

	@ManagedOperation(description = "Operation to reload a module")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "Module name", description = "Name of module to reload") })
	public String reloadModule(String moduleName) {

		ModuleOperation operation = moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation);
		
		try {
			ModuleOperationResult execute = operation.execute(new ModuleOperationInput(null, null, moduleName));
			if (execute.isSuccess()) {
				return "Successfully reloaded " + execute.getOutputParameters().get("moduleName");
			} else {
				return "Could not find module " + moduleName;
			}
		}
		catch (Throwable e) {
			return ExceptionUtils.getStackTrace(e);
		}
	}

	public void setModuleOperationRegistry(ModuleOperationRegistry moduleOperationRegistry) {
		this.moduleOperationRegistry = moduleOperationRegistry;
	}

}