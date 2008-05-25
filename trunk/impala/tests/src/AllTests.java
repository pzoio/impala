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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.test.AutomatedBuildTests;
import org.impalaframework.test.AutomatedCoreTests;
import org.impalaframework.test.AutomatedInteractiveTests;
import org.impalaframework.test.AutomatedJmxTests;
import org.impalaframework.test.AutomatedLauncherTests;
import org.impalaframework.web.test.AutomatedWebTests;


public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AutomatedCoreTests.suite());
		suite.addTest(AutomatedJmxTests.suite());
		suite.addTest(AutomatedBuildTests.suite());
		suite.addTest(AutomatedInteractiveTests.suite());
		suite.addTest(AutomatedWebTests.suite());
		suite.addTest(AutomatedLauncherTests.suite());
		return suite;
	}
}
