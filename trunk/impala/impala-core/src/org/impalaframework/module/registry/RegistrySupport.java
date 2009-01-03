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

package org.impalaframework.module.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.util.ObjectUtils;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class RegistrySupport {

	private Map entries = new HashMap();
	
	public <T extends Object> T getEntry(String key, Class<T> type) {
		
		return getEntry(key, type, true);
	}	
	
	public <T extends Object> T getEntry(String key, Class<T> type, boolean mandatory) {
		
		Assert.notNull(key, "key cannot be null");
		Assert.notNull(type, "type cannot be null");
		
		key = key.toLowerCase();
		
		Object value = entries.get(key);
		if (mandatory && value == null) {
			throw new NoServiceException("No instance of " + type.getName()
					+ " available for key '" + type + "'. Available entries: " + entries.keySet());
		}
		
		return ObjectUtils.cast(value, type);
	}
	
	
	public void addItem(String key, Object value) {
		
		Assert.notNull(key, "key cannot be null");
		Assert.notNull(value, "value cannot be null");
		
		this.entries.put(key.toLowerCase(), value);
	}
	
	public void setEntries(Map entries) {
		
		Assert.notNull(entries, "entries cannot be null");
		
		final Set<String> keySet = entries.keySet();
		for (String key : keySet) {
			this.entries.put(key, entries.get(key));
		}
	}
	
}
