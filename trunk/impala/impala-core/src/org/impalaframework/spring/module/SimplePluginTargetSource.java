/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.spring.module;

/**
 * Supports dynamic registration of bean "promoted" from child container
 * @author Phil Zoio
 */
public class SimplePluginTargetSource implements PluginContributionTargetSource {

	private Object target;

	public SimplePluginTargetSource() {
		super();
	}

	/* *************** TargetSource implementations ************** */
	
	public Object getTarget() throws Exception {
		return target;
	}

	public Class getTargetClass() {
		return null;
	}

	public boolean isStatic() {
		return true;
	}	
	
	public void releaseTarget(Object target) throws Exception {
		this.target = null;
	}

	/* *************** PluginContributionTargetSource implementations ************** */

	public boolean hasTarget() {
		return (this.target != null);
	}

	public void registerTarget(Object bean) {
		this.target = bean;
	}

	public void deregisterTarget(Object bean) {
		this.target = null;
	}

}
