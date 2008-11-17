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

package org.impalaframework.module;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.util.Assert;

public final class ModuleStateChange {
	
	private final Transition transition;

	private final ModuleDefinition moduleDefinition;

	public ModuleStateChange(Transition transition, ModuleDefinition moduleDefinition) {
		super();
		Assert.notNull(transition);
		Assert.notNull(moduleDefinition);
		this.transition = transition;
		this.moduleDefinition = moduleDefinition;
	}

	public ModuleDefinition getModuleDefinition() {
		return moduleDefinition;
	}

	public Transition getTransition() {
		return transition;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append(getTransition()).append(" - ").append(getModuleDefinition().getName()).toString();
	}

}
