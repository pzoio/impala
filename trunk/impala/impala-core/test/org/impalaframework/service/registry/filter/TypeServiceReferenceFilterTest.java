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

package org.impalaframework.service.registry.filter;

import junit.framework.TestCase;

import org.impalaframework.service.registry.BasicServiceRegistryReference;

public class TypeServiceReferenceFilterTest extends TestCase {

	public void testMatches() {
		SingleTypeServiceReferenceFilter filter = new SingleTypeServiceReferenceFilter();
		assertFalse(filter.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		
		filter.setType(String.class);
		assertTrue(filter.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		assertFalse(filter.matches(new BasicServiceRegistryReference(new Integer(1), "beanName", "moduleName")));
	}

}
