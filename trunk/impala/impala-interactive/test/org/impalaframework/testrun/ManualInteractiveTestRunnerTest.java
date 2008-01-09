package org.impalaframework.testrun;

import junit.framework.TestCase;

public class ManualInteractiveTestRunnerTest extends TestCase {
	
	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public final void testExecute() {
		new InteractiveTestRunner().start(Test1.class);
	}

	
}
