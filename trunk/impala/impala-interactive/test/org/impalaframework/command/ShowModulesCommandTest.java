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

package org.impalaframework.command;

import org.impalaframework.module.definition.SimpleRootModuleDefinition;

import junit.framework.TestCase;

public class ShowModulesCommandTest extends TestCase {

	public void testPrintModuleInfo() {
		ShowModulesCommand commands = new ShowModulesCommand();
		commands.printModuleInfo(null);
		
		final SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("rootproject", "location.xml");
		commands.printModuleInfo(simpleRootModuleDefinition);
	}

}
