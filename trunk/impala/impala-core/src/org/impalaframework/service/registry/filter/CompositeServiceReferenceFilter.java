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

import java.util.Collection;

import org.impalaframework.service.registry.ServiceRegistryReference;
import org.impalaframework.service.registry.event.ServiceReferenceFilter;

public class CompositeServiceReferenceFilter implements ServiceReferenceFilter {

	private Collection<ServiceReferenceFilter> filters;
	private boolean matchAny;
	
	public boolean matches(ServiceRegistryReference reference) {
		
		if (filters == null || filters.isEmpty()) {
			return false;
		}
		
		for (ServiceReferenceFilter type : filters) {	
			if (type.matches(reference)) {
				if (matchAny)
					return true;
			} else {
				if (!matchAny) {
					return false;
				}
			}
		}
		return true;
	}

	public void setFilters(Collection<ServiceReferenceFilter> types) {
		this.filters = types;
	}

	public void setMatchAny(boolean matchAny) {
		this.matchAny = matchAny;
	}
	
}
