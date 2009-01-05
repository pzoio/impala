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

package org.impalaframework.spring.module.graph;

import java.util.Arrays;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.spring.module.SpringModuleRuntime;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ApplicationContext;

public class SpringGraphModuleRuntime extends SpringModuleRuntime {
	
	private static final String GRAPH_ORDERED = "graphOrdered";
	private static final String PARENT_FIRST = "parentFirst";
	private static final String PARENT_ONLY = "parentOnly";
	private static final String NONE = "none";
	
	private String beanVisibilityType = PARENT_FIRST;

	@Override
	protected ApplicationContext getParentApplicationContext(ModuleDefinition definition) {
		
		ApplicationContext parentApplicationContext = internalGetParentApplicationContext(definition);
		
		if (parentApplicationContext == null) {
			return null;
		}

		if (beanVisibilityType.equals(NONE)) {
			return null;		
		}
		
		if (beanVisibilityType.equals(PARENT_ONLY)) {
			return parentApplicationContext;
		}
		
		if (beanVisibilityType.equals(PARENT_FIRST)) {
			GraphModuleStateHolder graphModuleStateHolder = ObjectUtils.cast(getModuleStateHolder(), GraphModuleStateHolder.class);
			return new ParentFirstBeanGraphInheritanceStrategy().getParentApplicationContext(graphModuleStateHolder, parentApplicationContext, definition);		
		}
		
		if (beanVisibilityType.equals(GRAPH_ORDERED)) {
			GraphModuleStateHolder graphModuleStateHolder = ObjectUtils.cast(getModuleStateHolder(), GraphModuleStateHolder.class);
			return new GraphOrderedBeanInheritanceStrategy().getParentApplicationContext(graphModuleStateHolder, parentApplicationContext, definition);
		}
	
		throw new ConfigurationException("Invalid value for property bean.visibility.type. Permissible values are " + Arrays.asList(NONE, PARENT_ONLY, PARENT_FIRST, GRAPH_ORDERED));
	}

	public void setBeanVisibilityType(String beanVisibilityType) {
		this.beanVisibilityType = beanVisibilityType;
	}

}
