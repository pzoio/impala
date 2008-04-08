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

import org.impalaframework.module.builder.SingleStringModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class ModificationTestUtils {

	static RootModuleDefinition spec(String contextString, String definitionString) {
		String[] locations = contextString.split(",");
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(new SimpleRootModuleDefinition(new String[]{"project1"}, locations),
				definitionString);
		RootModuleDefinition rootModuleDefinition = builder.getModuleDefinition();
		return rootModuleDefinition;
	}

}
