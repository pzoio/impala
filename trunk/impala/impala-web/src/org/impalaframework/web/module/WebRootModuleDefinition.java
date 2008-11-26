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

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;

public class WebRootModuleDefinition extends SimpleModuleDefinition {

	private static final long serialVersionUID = 1L;

	public WebRootModuleDefinition(ModuleDefinition moduleDefinition, 
			String name, 
			String[] contextLocations) {
		super(moduleDefinition, name, contextLocations);
	}
	
	public WebRootModuleDefinition(ModuleDefinition parent,
			String[] dependencies, 
			String name, 
			String[] contextLocations) {
		//FIXME test
		super(parent, dependencies, name, contextLocations);
	}

	@Override
	public String getType() {
		return WebModuleTypes.WEB_ROOT;
	}

}
