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

package org.impalaframework.classloader.graph;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.ModularClassLoader;
import org.impalaframework.util.StringBufferUtils;

/**
 * Delegate which is responsible for invoking the class loaders for a particular module, in an 
 * externally specified order. Note that this class does not extend {@link ClassLoader}
 * as it does not rely on the {@link ClassLoader#defineClass} method.
 */
public class DelegateClassLoader implements ModularClassLoader {

	private static final Log logger = LogFactory.getLog(DelegateClassLoader.class);
	
	private List<GraphClassLoader> classLoaders;
	
	/**
	 * Constructor
	 * @param classLoaders the class loaders to be called in order when attempting to 
	 * through class loaders "belonging" to dependent modules.
	 */
	public DelegateClassLoader(List<GraphClassLoader> classLoaders) {
		this.classLoaders = classLoaders;
	}

	/**
	 * Loops through the wired in class loaders. For each, check whether this returns
	 * a class. If so, then return this. Otherwise loop through to the next one.
	 * Return null if looping is completed and no class is found.
	 */
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		
		for (GraphClassLoader graphClassLoader : this.classLoaders) {
			Class<?> loadClass = graphClassLoader.loadCustomClass(name, false);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to load class " + name + " from classloader " + graphClassLoader + " on behalf of delegate " + this);
			}
			
			if (loadClass != null) {
				return loadClass;
			}
		}
		
		return null;
	}
	
	public boolean hasVisibilityOf(ClassLoader classLoader) {
		for (GraphClassLoader graphClassLoader : this.classLoaders) {
			if (classLoader == graphClassLoader) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		if (classLoaders.size() > 0) {

			buffer.append("Delegate class loader: ");
			for (GraphClassLoader graphClassLoader : classLoaders) {
				buffer.append(graphClassLoader.getModuleName() + ",");
			}
			StringBufferUtils.chop(buffer, 1);
			String lineSeparator = System.getProperty("line.separator");
			buffer.append(lineSeparator);
		}
		
		return buffer.toString();
	}
}
