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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinitionTest extends TestCase {

	public void testSimpleModuleDefinition() {

		SimpleModuleDefinition definition = new SimpleModuleDefinition("p1");

		assertEquals(1, definition.getContextLocations().size());
		assertEquals("p1-context.xml", definition.getContextLocations().get(0));
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
		assertTrue(definition.hasDefinition("c1"));
		assertTrue(definition.hasDefinition("c2"));
		assertEquals(2, definition.getChildDefinitions().size());
		assertEquals(2, definition.getChildDefinitions().size());

		assertSame(child1, definition.getModule("c1"));
		assertSame(child2, definition.getModule("c2"));

	}

	public void testEquals() {

		SimpleModuleDefinition definition1a = new SimpleModuleDefinition(null, "p1", new String[] { "loc1", "loc2" });
		SimpleModuleDefinition definition1b = new SimpleModuleDefinition(null, "p1", new String[] { "loc1", "loc2" });
		SimpleModuleDefinition definition1c = new SimpleModuleDefinition(null, "p1", new String[] { "loc1", });

		assertEquals(definition1a, definition1b);
		assertFalse(definition1a.equals(definition1c));
	}
	
	public void testAttributes() throws Exception {
		SimpleModuleDefinition definition = new SimpleModuleDefinition(null, "p1", null, null, Collections.singletonMap("name", "value"));
		final Map<String, String> attributes = definition.getAttributes();
		assertEquals(1, attributes.size());
	}

	public void testConstructors() {

		SimpleModuleDefinition definition1a = new SimpleModuleDefinition(null, "p1", new String[] { "loc1" });
		SimpleModuleDefinition definition1b = new SimpleModuleDefinition(null, "p1", new String[] {});
		SimpleModuleDefinition definition1c = new SimpleModuleDefinition(null, "p1", null);

		checkLocation(definition1a, "loc1");
		checkLocation(definition1b, "p1-context.xml");
		checkLocation(definition1c, "p1-context.xml");
	}

	private void checkLocation(SimpleModuleDefinition definition, String expected) {
		List<String> contextLocations = definition.getContextLocations();
		assertEquals(1, contextLocations.size());
		assertEquals(expected, contextLocations.get(0));
	}

}
