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

//this class is used by the ImpalaTestRunner to load test classes
//when the ImpalaTestRunner has been started from one of the child projects
public class ModuleTestClassLoader extends URLClassLoader {

	public ModuleTestClassLoader(File[] locations, String testClassName) {
		super(locations);
	}

	public ModuleTestClassLoader(ClassLoader parent, File[] locations, String testClassName) {
		super(parent, locations);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {

		Class<?> toReturn = loadCustomClass(className);

		if (toReturn == null) {
			toReturn = loadParentClass(className);
		}

		if (toReturn == null) {
			toReturn = getAlreadyLoadedClass(className);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loaded " + " class " + className + ": " + toReturn.getClassLoader());
		}

		if (toReturn == null) {
			if (logger.isDebugEnabled())
				logger.debug("Class not found: " + className);
			throw new ClassNotFoundException(className);
		}

		return toReturn;
		
	}

}