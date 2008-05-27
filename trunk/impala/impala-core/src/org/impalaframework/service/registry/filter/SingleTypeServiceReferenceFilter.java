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

import org.impalaframework.service.registry.ServiceRegistryReference;
import org.impalaframework.service.registry.event.ServiceReferenceFilter;

public class SingleTypeServiceReferenceFilter implements ServiceReferenceFilter {

	private Class<?> type;
	
	public boolean matches(ServiceRegistryReference reference) {
		Object bean = reference.getBean();
		
		if (type == null) {
			return false;
		}
		
		if (type.isAssignableFrom(bean.getClass())) {
			return true;
		}
		
		return false;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}
