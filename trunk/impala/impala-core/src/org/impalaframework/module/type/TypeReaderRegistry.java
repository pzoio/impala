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

package org.impalaframework.module.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.spi.Registry;
import org.impalaframework.module.spi.TypeReader;
import org.springframework.util.Assert;

public class TypeReaderRegistry implements Registry<TypeReader> {

	private Map<String, TypeReader> typeReaders = new HashMap<String, TypeReader>();
	
	public TypeReader getTypeReader(String type) {

		if (type == null) return null;
		type = type.toLowerCase();
		
		TypeReader typeReader = typeReaders.get(type);
		if (typeReader == null) {
			throw new NoServiceException("No instance of " + TypeReader.class.getName()
					+ " available for type named '" + type + "'. Available types: " + typeReaders.keySet());
		}
		
		return typeReader;
	}
	
	public void addItem(String type, TypeReader typeReader) {
		Assert.notNull(type);
		this.typeReaders.put(type.toLowerCase(), typeReader);
	}

	public Map<String, TypeReader> getTypeReaders() {
		return Collections.unmodifiableMap(typeReaders);
	}

	public void setTypeReaders(Map<String, TypeReader> typeReaders) {
		Assert.notNull(typeReaders);
		final Set<String> keys = typeReaders.keySet();
		for (String key : keys) {
			addItem(key, typeReaders.get(key));
		}
	}
}
