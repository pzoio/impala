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

package org.impalaframework.resolver;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.exception.InvalidStateException;

import junit.framework.TestCase;

public class CascadingModuleLocationResolverTest extends TestCase {
	
	private CascadingModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new CascadingModuleLocationResolver();
		resolver.setWorkspaceRoot("../impala-core/files/impala-classloader");
		
		FileModuleResourceFinder fileFinder = new FileModuleResourceFinder();
		fileFinder.setClassDirectory("bin");
		JarModuleResourceFinder jarFinder = new JarModuleResourceFinder();
		
		List<ModuleResourceFinder> resourceFinders = new ArrayList<ModuleResourceFinder>();
		resourceFinders.add(fileFinder);
		resourceFinders.add(jarFinder);
		
		resolver.setModuleResourceFinders(resourceFinders);
		resolver.setApplicationVersion("1.0");
		
		resolver.init();
	}
	
	public void testGetApplicationModuleClassLocations() {
		assertFalse(resolver.getApplicationModuleClassLocations("module-a").isEmpty());
		assertFalse(resolver.getApplicationModuleClassLocations("module-i").isEmpty());
		assertFalse(resolver.getApplicationModuleClassLocations("module-h").isEmpty());
		
		try {
			resolver.getApplicationModuleClassLocations("module-k").isEmpty();
			fail();
		} catch (InvalidStateException e) {
			System.out.println(e.getMessage());
		}
	}

}
