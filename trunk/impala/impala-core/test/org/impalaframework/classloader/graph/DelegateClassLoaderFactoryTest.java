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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.holder.graph.GraphClassLoaderFactory;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;

public class DelegateClassLoaderFactoryTest extends TestCase {

	private DependencyManager dependencyManager;
	private GraphClassLoaderFactory factory;
	private GraphClassLoaderRegistry graphClassLoaderRegistry;
	private ModuleDefinition a;
	private ModuleDefinition b;
	private ModuleDefinition g;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graphClassLoaderRegistry = new GraphClassLoaderRegistry();
		factory = new GraphClassLoaderFactory();
		factory.setClassLoaderRegistry(graphClassLoaderRegistry);
		factory.setModuleLocationResolver(new TestClassResolver());
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		a = newDefinition(definitions, "a");
		b = newDefinition(definitions, "b", "a");
		newDefinition(definitions, "c");
		newDefinition(definitions, "d", "b");
		newDefinition(definitions, "e", "c,d");
		newDefinition(definitions, "f", "b,e");
		g = newDefinition(definitions, "g", "c,d,f");
		dependencyManager = new DependencyManager(definitions);
	}
	
	public void testClassLoader() throws Exception {
		
		GraphClassLoader aClassLoader = factory.newClassLoader(dependencyManager, a);
		Object aImpl = aClassLoader.loadClass("AImpl").newInstance();
		
		assertNotNull(graphClassLoaderRegistry.getClassLoader("module-a"));
		
		GraphClassLoader bClassLoader = factory.newClassLoader(dependencyManager, b);
		Object aImplFromB = bClassLoader.loadClass("AImpl").newInstance();

		assertNotNull(graphClassLoaderRegistry.getClassLoader("module-b"));
		
		//notice the same class object gets returned here
		assertSame(aImpl.getClass(), aImplFromB.getClass());
		
		Class<?> aInt = bClassLoader.loadClass("A");
		
		//show that AImpl loaded from b can be asssigned to to AImpl loaded from a
		assertTrue(aInt.isAssignableFrom(aImpl.getClass()));
		
		try {
			aClassLoader.loadClass("duffClass");
			fail();
		} catch (ClassNotFoundException e) {
		}
		
		GraphClassLoader newbClassLoader = factory.newClassLoader(dependencyManager, b);
		Object newaImplFromB = newbClassLoader.loadClass("AImpl").newInstance();
		
		//can still use new class loader
		assertTrue(aInt.isAssignableFrom(newaImplFromB.getClass()));

		Object bImpl = bClassLoader.loadClass("BImpl").newInstance();
		Object newBImpl = newbClassLoader.loadClass("BImpl").newInstance();
		
		assertSame(bImpl.getClass(), newBImpl.getClass());
		
		Map<String, Class<?>> loadedClasses = bClassLoader.getLoadedClasses();
		assertEquals(2, loadedClasses.size());
		assertTrue(loadedClasses.containsKey("BImpl"));
		assertTrue(loadedClasses.containsKey("B"));
		
	}
	
	public void testRecursiveClassLoader() throws Exception {

		//notice how can recursively create class loaders on dependent modules
		factory.newClassLoader(dependencyManager, g);
		assertNotNull(graphClassLoaderRegistry.getClassLoader("module-a"));
		assertNotNull(graphClassLoaderRegistry.getClassLoader("module-g"));
		
	}
	
	public void testAttemptAddNewClassLoader() throws Exception {

		factory.newClassLoader(dependencyManager, a);
		try {
			graphClassLoaderRegistry.addClassLoader("module-a", EasyMock.createMock(GraphClassLoader.class));
		} catch (InvalidStateException e) {
			assertEquals("Class loader registry already contains class loader for module 'module-a'", e.getMessage());
		}
	}

	private ModuleDefinition newDefinition(List<ModuleDefinition> list, final String name, final String dependencies) {
		final String[] split = dependencies.split(",");
		for (int i = 0; i < split.length; i++) {
			split[i] = "module-" + split[i];
		}
		final List<String> dependencyList = Arrays.asList(split);
		ModuleDefinition definition = new SimpleModuleDefinition(null, dependencyList.toArray(new String[0]), "module-" + name);
		list.add(definition);
		return definition;
	}
	
	private ModuleDefinition newDefinition(List<ModuleDefinition> list, final String name) {
		ModuleDefinition definition = new SimpleModuleDefinition(null, new String[0], "module-" + name);
		list.add(definition);
		return definition;
	}
	
}