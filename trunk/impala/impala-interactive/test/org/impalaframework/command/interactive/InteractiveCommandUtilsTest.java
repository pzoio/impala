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

import org.impalaframework.facade.Impala;
import org.impalaframework.resolver.LocationConstants;

import junit.framework.TestCase;

public class InteractiveCommandUtilsTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		try {
			Impala.clear();
		} catch (RuntimeException e) {
		}
	}
	
	public final void testIsRootProjectNull() {
		assertTrue(InteractiveCommandUtils.getRootProjectList().isEmpty());
	}
	
	public final void testWithRootDefinition() {
		Impala.init(new Test1());
		assertEquals(1, InteractiveCommandUtils.getRootProjectList().size());
		assertTrue(InteractiveCommandUtils.isRootProject("impala-core"));
	}
	
	public final void testIsRootProjectSysProperty() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "project1, project2");
		assertTrue(InteractiveCommandUtils.isRootProject("project1"));
		assertTrue(InteractiveCommandUtils.isRootProject("project2"));
		assertFalse(InteractiveCommandUtils.isRootProject("project3"));
	}

}
