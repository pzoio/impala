/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.command.basic;

import junit.framework.TestCase;

import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandState;

public class ManualSelectMethodCommandTest extends TestCase {
	public void testAlternativeInputCommand() throws Exception {
		SelectMethodCommand command = getCommand();
		doTest(command);
	}

	protected boolean doTest(SelectMethodCommand command) throws ClassNotFoundException {
		// now need to capture
		CommandState commandState = new CommandState();

		CommandLineInputCapturer inputCapturer = getInputCapturer();
		commandState.setInputCapturer(inputCapturer);

		commandState.capture(command);
		boolean result = command.execute(commandState);
		
		System.out.println("Selected alternative: " + command.getMethodName());
		return result;
	}
	
	protected CommandLineInputCapturer getInputCapturer() {
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer();
		return inputCapturer;
	}
	
	protected SelectMethodCommand getCommand() {
		SelectMethodCommand command = new SelectMethodCommand(ClassFindCommandTest.class);
		return command;
	}
}
