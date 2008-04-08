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

package org.impalaframework.module.definition;

import junit.framework.TestCase;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

/**
 * @author Phil Zoio
 */
public class SimpleSpringContextTest extends TestCase {

	public void testHasModule() {
		SimpleModuleDefinitionSource definition = new SimpleModuleDefinitionSource(new String[]{"impala-core"}, new String[] { "l0", "l1", "l2" }, new String[] { "p1", "p2" });
		
		assertNotNull(definition);
		final RootModuleDefinition root = definition.getModuleDefinition();
		assertEquals(3, root.getContextLocations().size());
		
		assertTrue(root.hasDefinition("p1"));
		assertTrue(root.hasDefinition("p2"));
		assertFalse(root.hasDefinition("p3"));
		
		assertEquals(2, root.getModuleNames().size());
	}

}
