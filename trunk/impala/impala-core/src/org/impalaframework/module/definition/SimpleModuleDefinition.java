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

package org.impalaframework.module.definition;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinition implements ModuleDefinition {

	private static final long serialVersionUID = 1L;

	private String name;

	private ModuleState state;

	private ChildModuleContainer childContainer;

	private ModuleDefinition parentDefinition;

	private List<String> contextLocations;

	public SimpleModuleDefinition(String name) {
		this(null, name, defaultContextLocations(name));
	}

	public SimpleModuleDefinition(ModuleDefinition parent, String name) {
		this(parent, name, defaultContextLocations(name));
	}

	public SimpleModuleDefinition(ModuleDefinition parent, String name, String[] contextLocations) {
		super();
		Assert.notNull(name);
		this.name = name;
		this.childContainer = new ChildModuleContainerImpl();
		this.parentDefinition = parent;

		if (contextLocations == null || contextLocations.length == 0) {
			contextLocations = defaultContextLocations(name);
		}
		this.contextLocations = Arrays.asList(contextLocations);

		if (this.parentDefinition != null)
			this.parentDefinition.add(this);
	}

	private static String[] defaultContextLocations(String name) {
		return new String[] { name + "-context.xml" };
	}

	public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
		return ModuleDefinitionUtils.findDefinition(moduleName, this, exactMatch);
	}

	public List<String> getContextLocations() {
		return contextLocations;
	}

	public String getName() {
		return name;
	}

	public ModuleDefinition getParentDefinition() {
		return parentDefinition;
	}

	public Collection<String> getModuleNames() {
		return childContainer.getModuleNames();
	}

	public ModuleDefinition getModule(String moduleName) {
		return childContainer.getModule(moduleName);
	}

	public Collection<ModuleDefinition> getChildDefinitions() {
		return childContainer.getChildDefinitions();
	}

	public boolean hasDefinition(String moduleName) {
		return getModule(moduleName) != null;
	}

	public void add(ModuleDefinition moduleDefinition) {
		childContainer.add(moduleDefinition);
	}

	public ModuleDefinition remove(String moduleName) {
		return childContainer.remove(moduleName);
	}

	public void setParentDefinition(ModuleDefinition parentDefinition) {
		this.parentDefinition = parentDefinition;
	}

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

	public String getType() {
		return ModuleTypes.APPLICATION;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contextLocations == null) ? 0 : contextLocations.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleModuleDefinition other = (SimpleModuleDefinition) obj;
		if (contextLocations == null) {
			if (other.contextLocations != null)
				return false;
		}
		else if (!contextLocations.equals(other.contextLocations))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		toString(buffer, 0);
		return buffer.toString();
	}

	public void toString(StringBuffer buffer, int spaces) {
		ModuleDefinitionUtils.addAttributes(spaces, buffer, this);
	}
}
