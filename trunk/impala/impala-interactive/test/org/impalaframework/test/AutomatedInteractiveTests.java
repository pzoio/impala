/*
 * Copyright 2006-2007 the original author or authors.
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

package org.impalaframework.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.classloader.TestClassLoaderTest;
import org.impalaframework.command.ExitCommandTest;
import org.impalaframework.command.InitContextCommandTest;
import org.impalaframework.command.InteractiveCommandUtilsTest;
import org.impalaframework.command.InteractiveTestCommandTest;
import org.impalaframework.command.LoadDefinitionCommandTest;
import org.impalaframework.command.LoadDefinitionFromClassCommandTest;
import org.impalaframework.command.LoadTestClassContextCommandTest;
import org.impalaframework.command.ReloadCommandTest;
import org.impalaframework.command.RunTestCommandTest;
import org.impalaframework.command.ShowModulesCommandTest;
import org.impalaframework.command.basic.ModuleDefinitionAwareClassFilterTest;

public class AutomatedInteractiveTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(ExitCommandTest.class);
		suite.addTestSuite(InitContextCommandTest.class);
		suite.addTestSuite(InteractiveCommandUtilsTest.class);
		suite.addTestSuite(InteractiveTestCommandTest.class);
		suite.addTestSuite(LoadDefinitionCommandTest.class);
		suite.addTestSuite(LoadDefinitionFromClassCommandTest.class);
		suite.addTestSuite(LoadTestClassContextCommandTest.class);
		suite.addTestSuite(ModuleDefinitionAwareClassFilterTest.class);
		suite.addTestSuite(ReloadCommandTest.class);
		suite.addTestSuite(RunTestCommandTest.class);
		suite.addTestSuite(ShowModulesCommandTest.class);
		suite.addTestSuite(TestClassLoaderTest.class);
		return suite;
	}
}
