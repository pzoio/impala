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

package test;

import junit.framework.TestCase;

import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.resolver.LocationConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseIntegrationTest extends TestCase implements ModuleDefinitionSource {

	private static final Log logger = LogFactory.getLog(BaseIntegrationTest.class);	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "wineorder");
		DynamicContextHolder.init(this);
		logger.info("Setting up " + this.getClass().getSimpleName());
	}
}
