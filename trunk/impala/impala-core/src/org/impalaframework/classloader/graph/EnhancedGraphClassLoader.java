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

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.module.ModuleDefinition;

/**
 * Extension of {@link GraphClassLoader} which is aware of resources on file system
 * @author Phil Zoio
 */
public class EnhancedGraphClassLoader extends GraphClassLoader {
    
    private ClassRetriever retriever = null;

    public EnhancedGraphClassLoader(
            ClassLoader parentClassLoader,
            DelegateClassLoader delegateClassLoader,
            ClassRetriever moduleResourceRetriever, 
            ClassRetriever internalJarRetriever,
            ModuleDefinition definition, boolean loadParentFirst) {
        super(parentClassLoader, delegateClassLoader, moduleResourceRetriever, definition, loadParentFirst);
        retriever = internalJarRetriever;
    }

    @Override
    protected Class<?> maybeLoadExternalClass(String className) {
        if (retriever != null) {
            return attemptToLoadUsingRetriever(retriever, className);
        }
        return null;
    }
    
}
