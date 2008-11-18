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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class GraphBasedClassLoaderTest extends TestCase {

	public void testClassLoader() throws Exception {
		
		final GraphClassLoaderFactory graphClassLoaderFactory = new GraphClassLoaderFactory(new TestClassResolver());
		
		
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		newDefinition(definitions, "a");
		newDefinition(definitions, "b", "a");
		newDefinition(definitions, "c");
		newDefinition(definitions, "d", "b");
		newDefinition(definitions, "e", "c,d");
		newDefinition(definitions, "f", "b,e");
		newDefinition(definitions, "g", "c,d,f");	
		
		/*
a
b depends on a
c
d depends on b
e depends on c, d
f depends on b, e
g on c, d, f
		 */
		DependencyRegistry registry = new DependencyRegistry(definitions);
		
		DependencyRegistryClassLoaderHelper helper = new DependencyRegistryClassLoaderHelper(graphClassLoaderFactory, registry);
		
		GraphBasedClassLoader eClassLoader = new GraphBasedClassLoader(helper, "module-e");
		System.out.println(eClassLoader.toString());
		
		System.out.println(eClassLoader.loadClass("E"));
		System.out.println(eClassLoader.loadClass("EImpl"));
		System.out.println(eClassLoader.loadClass("C"));
		System.out.println(eClassLoader.loadClass("B"));
		System.out.println(eClassLoader.loadClass("A"));

		failToLoad(eClassLoader, "F");
		
		printModuleDependees(registry, "module-a");
		printModuleDependees(registry, "module-b");
		printModuleDependees(registry, "module-c");
		printModuleDependees(registry, "module-d");
		printModuleDependees(registry, "module-e");
		printModuleDependees(registry, "module-f");
		printModuleDependees(registry, "module-g");
		
		System.out.println("------------------ Removing vertices for c --------------------");
		registry.remove("module-c");

		try {
		new GraphBasedClassLoader(new DependencyRegistryClassLoaderHelper(graphClassLoaderFactory, registry), "module-c");
		fail(); }
		catch (InvalidStateException e) {}
		
		//notice that any of c's dependees no longer appear now
		printModuleDependees(registry, "module-a");
		
		//now add c, depending on a
		GraphModuleDefinition newC = new SimpleGraphModuleDefinition("module-c", Arrays.asList("module-a"));
		
		//and e, with c as parent, and depending also on b
		new SimpleGraphModuleDefinition(newC, "module-e", Arrays.asList("module-b"));
		
		registry.addModule("module-a", newC);
		
		//we should see c and e in the list of dependencies
		printModuleDependees(registry, "module-a");
		
		//now we load class C
		final GraphBasedClassLoader cClassLoader = new GraphBasedClassLoader(new DependencyRegistryClassLoaderHelper(graphClassLoaderFactory, registry), "module-c");
		System.out.println(cClassLoader.loadClass("C"));
		System.out.println(cClassLoader);
		
	}

	private void failToLoad(GraphBasedClassLoader classLoader,
			final String className) {
		try {
			classLoader.loadClass(className);
			fail();
		} catch (ClassNotFoundException e) {
		}
	}

	private void printModuleDependees(DependencyRegistry registry,
			final String moduleName) {
		System.out.println("--------------- Module dependees: " + moduleName);
		final List<ModuleDefinition> dependees = registry.getOrderedModuleDependees(moduleName);
		for (ModuleDefinition moduleDefinition : dependees) {
			System.out.println(moduleDefinition.getName());
		}
		System.out.println("---------------------------------------------");
	}

	private void newDefinition(List<ModuleDefinition> list, final String name, final String dependencies) {
		final String[] split = dependencies.split(",");
		for (int i = 0; i < split.length; i++) {
			split[i] = "module-" + split[i];
		}
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name, dependencyList);
		list.add(definition);
	}
	
	private void newDefinition(List<ModuleDefinition> list, final String name) {
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name);
		list.add(definition);
	}
	
}

class TestClassResolver implements ModuleLocationResolver {

	private String rootLocation = "../impala-core/files/impala-classloader";
	
	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		File root = rootFileLocation();
		File moduleDirectory = new File(root, moduleName);
		File classDirectory = new File(moduleDirectory, "bin");
		final Resource resource = new FileSystemResource(classDirectory);
		
		Assert.isTrue(resource.exists());
		
		return Collections.singletonList(resource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		throw new UnsupportedOperationException();
	}

	public Resource getRootDirectory() {
		File file = rootFileLocation();
		return new FileSystemResource(file);
	}

	private File rootFileLocation() {
		File file = new File(rootLocation);
		return file;
	}
	
}
