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

package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base implementation of <code>URLClassLoader</code> which will
 * attempt to load from the named class locations BEFORE attempting to load
 * using the parent class location.
 * @author Phil Zoio
 * @see URLClassLoader
 */
public abstract class CustomClassLoader extends URLClassLoader {

	final Log logger = LogFactory.getLog(CustomClassLoader.class);

	public CustomClassLoader(File[] locations) {
		super(locations);
	}

	public CustomClassLoader(ClassLoader parent, File[] locations) {
		super(parent, locations);
	}
	
	public CustomClassLoader(URL[] locations) {
		super(locations);
	}

	public CustomClassLoader(ClassLoader parent, URL[] locations) {
		super(parent, locations);
	}
	
	protected abstract boolean loadCustomClassFirst();

	/**
	 * Attempts to load the class by calling the following superclass methods,
	 * in order:
	 * <ul>
	 * <li><code>getAlreadyLoadedClass()</code>, to return a cached class.</li>
	 * <li><code>loadCustomClass()</code>, to attempt to load the class from
	 * a custom location.</li>
	 * <li><code>loadParentClass()</code></li>
	 * </ul>
	 * @param name of class to load
	 * @exception the <code>ClassNotFoundException</code> if the class could
	 * not be loaded
	 */
	public Class<?> loadClass(String className) throws ClassNotFoundException {

		Class<?> toReturn = null;
		if (toReturn == null) {
			toReturn = getAlreadyLoadedClass(className);
		}
		//FIXME if parent class is loaded first, would lose the
		//dynamic reloading capability. 
		//Should have it as an option, as this would make running
		//more reliable
		
		if (loadCustomClassFirst()) {	
			if (toReturn == null) {
				toReturn = loadCustomClass(className);
			}
			if (toReturn == null) {
				toReturn = loadParentClass(className);
			}
		} else {
			if (toReturn == null) {
				toReturn = loadParentClass(className);
			}	
			if (toReturn == null) {
				toReturn = loadCustomClass(className);
			}
		}
		
		if (toReturn == null) {
			if (logger.isDebugEnabled())
				logger.debug("Class not found: " + className);
			throw new ClassNotFoundException(className);
		}

		return toReturn;
	}

}