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

package org.impalaframework.module.definition;

import org.impalaframework.module.definition.ChildModuleContainer;
import org.impalaframework.module.definition.ChildModuleContainerImpl;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class ChildModuleContainerTest extends TestCase {

	public void testChildSpecContainer() {
		final ModuleDefinition[] strings = new ModuleDefinition[] { new SimpleModuleDefinition("p1"), new SimpleModuleDefinition("p2") };
		ChildModuleContainer spec = new ChildModuleContainerImpl(strings);

		assertTrue(spec.hasPlugin("p1"));
		assertTrue(spec.hasPlugin("p2"));
		assertFalse(spec.hasPlugin("p3"));

		assertEquals(2, spec.getModuleNames().size());
		
		//fail("To implement add() and remove() tests");
	}

}
