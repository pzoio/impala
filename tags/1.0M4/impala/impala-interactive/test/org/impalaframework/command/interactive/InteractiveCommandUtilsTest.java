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

package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.facade.Impala;

public class InteractiveCommandUtilsTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Impala.clear();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Impala.clear();
	}
	
	public final void testIsRootProjectNull() {
		assertNull(InteractiveCommandUtils.getRootProject());
	}
	
	public final void testWithRootDefinition() {
		Impala.init(new Test1());
		assertEquals("impala-core", InteractiveCommandUtils.getRootProject());
		assertEquals(true, InteractiveCommandUtils.isRootProject("impala-core"));
	}

}
