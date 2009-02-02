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

package org.impalaframework.osgi.test;

import org.impalaframework.module.ModuleDefinitionSource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class TestActivator implements BundleActivator {

	private ServiceRegistration registerService;

	public void start(BundleContext bundleContext) throws Exception {
		registerService = bundleContext.registerService(ModuleDefinitionSource.class.getName(), new InjectableModuleDefinitionSource(bundleContext), null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		registerService.unregister();
	}

}
