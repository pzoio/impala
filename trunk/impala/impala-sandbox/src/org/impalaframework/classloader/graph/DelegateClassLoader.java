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

//FIXME comment and test
/**
 * Delegate which is responsible for invoking the class loaders for a particular module, in an 
 * externally specified order.
 */
public class DelegateClassLoader extends ClassLoader {

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
	@Override
	public Class<?> loadClass(String name)
			throws ClassNotFoundException {
		
		for (GraphClassLoader graphClassLoader : this.classLoaders) {
			Class<?> loadClass = graphClassLoader.loadClass(name, false);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to load class " + name + " from classloader " + graphClassLoader + " on behalf of delegate " + this);
			}
			
			if (loadClass != null) {
				return loadClass;
			}
		}
		
		return null;
	}
}
