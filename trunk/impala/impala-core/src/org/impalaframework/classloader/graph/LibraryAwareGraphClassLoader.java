/*
 * Copyright 2007-2010 the original author or authors.
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

import java.net.URL;
import java.util.Enumeration;

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.module.ModuleDefinition;

/**
 * Extension of {@link GraphClassLoader} which is aware of module-specific libraries
 * @author Phil Zoio
 */
public class LibraryAwareGraphClassLoader extends GraphClassLoader {
    
    /**
     * Used to retrieve module-specific libraries. 
     * If no module-specific libraries are present, then will be null
     */
    private ClassRetriever libraryRetriever = null;

    public LibraryAwareGraphClassLoader(
            ClassLoader parentClassLoader,
            DelegateClassLoader delegateClassLoader,
            ClassRetriever moduleResourceRetriever, 
            ClassRetriever moduleLibraryRetriever,
            ModuleDefinition definition, 
            ClassLoaderOptions options) {
        super(parentClassLoader, delegateClassLoader, moduleResourceRetriever, definition, options);
        this.libraryRetriever = moduleLibraryRetriever;
    }

    @Override
    protected Class<?> maybeFindLibraryClassLocally(String className, boolean internalLoad) {
        
        Class<?> clazz = null;
        if (libraryRetriever != null) {
            clazz = attemptToLoadUsingRetriever(libraryRetriever, className, internalLoad, true);
        }
        return clazz;
    }
    
    @Override
    protected URL getLocalResource(String name) {
        URL localResource = super.getLocalResource(name);
        if (localResource == null) {
            if (libraryRetriever != null && super.getOptions().isLoadsModuleLibraryResources()) {
                return libraryRetriever.findResource(name);
            }
        }
        return localResource;
    }
    
    @Override
    protected Enumeration<URL> getLocalResources(String name) {
        return super.getLocalResources(name);
    }
    
}
