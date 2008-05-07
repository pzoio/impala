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

package org.impalaframework.module.bootstrap;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class StandaloneModuleLocationResolverFactoryBean extends StandaloneModuleLocationResolverFactory implements
		InitializingBean, FactoryBean {

	private ModuleLocationResolver moduleLocationResolver;

	public void afterPropertiesSet() throws Exception {
		moduleLocationResolver = getClassLocationResolver();
	}

	public Object getObject() throws Exception {
		return moduleLocationResolver;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return ModuleLocationResolver.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
