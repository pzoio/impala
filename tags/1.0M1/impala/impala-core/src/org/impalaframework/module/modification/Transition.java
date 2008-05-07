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

package org.impalaframework.module.modification;

import org.impalaframework.module.definition.ModuleState;
import org.springframework.util.Assert;

public enum Transition {

	LOADED_TO_UNLOADED(ModuleState.LOADED, ModuleState.UNLOADED), 
	UNLOADED_TO_LOADED(ModuleState.UNLOADED, ModuleState.LOADED),
	CONTEXT_LOCATIONS_ADDED(ModuleState.LOADED, ModuleState.LOADED);
	
	private Enum<ModuleState> beforeState;

	private Enum<ModuleState> afterState;

	private Transition(Enum<ModuleState> beforeState, Enum<ModuleState> afterState) {
		Assert.notNull(beforeState);
		Assert.notNull(afterState);
		this.beforeState = beforeState;
		this.afterState = afterState;
	}

	public Enum<ModuleState> getAfterState() {
		return afterState;
	}

	public Enum<ModuleState> getBeforeState() {
		return beforeState;
	}

}
