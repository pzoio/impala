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

package org.impalaframework.classloader.graph;

import static org.impalaframework.classloader.graph.GraphTestUtils.assertModules;
import static org.impalaframework.classloader.graph.GraphTestUtils.newDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphRootModuleDefinition;

/**
 * Tests for various corner and error cases for {@link DependencyManager}
 * @author Phil Zoio
 */
public class DependencyManagerErrorTest extends TestCase {
	
	private SimpleGraphRootModuleDefinition rootDefinition;
	private DependencyManager manager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		rootDefinition = definitionSet1();
		manager = new DependencyManager(rootDefinition);
	}
	
	public void testOK() throws Exception {
		Collection<ModuleDefinition> allModules = manager.getAllModules();
		assertModules("b,a,root,f,e", allModules);
	}
	
	public void testModuleNotPresent() throws Exception {
		try {
			manager.addModule("duffModule", new SimpleModuleDefinition("moduleWithDuffParent"));
			fail();
		} catch (InvalidStateException e) {
			assertDuffModule(e);
		}
		
		try {
			manager.getOrderedModuleDependees("duffModule");
			fail();
		} catch (InvalidStateException e) {
			assertDuffModule(e);
		}
		
		try {
			manager.getOrderedModuleDependencies("duffModule");
			fail();
		} catch (InvalidStateException e) {
			assertDuffModule(e);
		}
		
		try {
			manager.getDirectDependees("duffModule");
			fail();
		} catch (InvalidStateException e) {
			assertDuffModule(e);
		}
	}

	public void testSortHasInvalidModule() throws Exception {
		ModuleDefinition[] toSort = new ModuleDefinition[]{rootDefinition.getModule("e"),rootDefinition.getModule("f")};
		List<ModuleDefinition> sorted = manager.sort(Arrays.asList(toSort));
		System.out.println(sorted);
		
		toSort = new ModuleDefinition[]{rootDefinition.getModule("e"), new SimpleModuleDefinition("duffModule")};
		
		try {
			manager.sort(Arrays.asList(toSort));
			fail();
		} catch (InvalidStateException e) {
			assertDuffModule(e);
		}
	}
	
	public void testAddModuleWithDuffDependency() throws Exception {
		manager.addModule("root", new SimpleGraphModuleDefinition("newmodule1", Arrays.asList("e")));

		try {
			manager.addModule("root", new SimpleGraphModuleDefinition("newmodule2", Arrays.asList("duffModule")));
		} catch (InvalidStateException e) {
			assertEquals("Unable to dependency named named 'duffModule' for module definition 'newmodule2'", e.getMessage());
		}
	}
	
	private SimpleGraphRootModuleDefinition definitionSet1() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		
		//b depends on d but has no parent
		ModuleDefinition b = newDefinition(definitions, null, "b", null);
		
		//root has siblings a, b and depends on a
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				new String[] {"root.xml"}, 
				Arrays.asList("a"), 
				Arrays.asList(a, b));
		
		//e has parent root, and depends on b
		newDefinition(definitions, root, "e", "b");
		
		//f has parent root, but no dependencies
		newDefinition(definitions, root, "f", null);
	
		return root;
	}

	private void assertDuffModule(InvalidStateException e) {
		assertEquals("No module 'duffModule' is registered with current instance of dependency manager.", e.getMessage());
	}
	
}
