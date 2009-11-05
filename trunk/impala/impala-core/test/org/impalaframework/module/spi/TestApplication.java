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

package org.impalaframework.module.spi;

import org.impalaframework.service.ServiceRegistry;

/**
 * {@link ApplicationManager} which takes the {@link Application} to use in the constructor
 * @author Phil Zoio
 */
public class TestApplication implements Application {

    private final ClassLoaderRegistry classLoaderRegistry;
    
    private final ModuleStateHolder moduleStateHolder;
    
    private final ServiceRegistry serviceRegistry;

    public TestApplication(
            ClassLoaderRegistry classLoaderRegistry,
            ModuleStateHolder moduleStateHolder, 
            ServiceRegistry serviceRegistry) {
        
        super();
        
        this.classLoaderRegistry = classLoaderRegistry;
        this.moduleStateHolder = moduleStateHolder;
        this.serviceRegistry = serviceRegistry;
    }

    public ClassLoaderRegistry getClassLoaderRegistry() {
        return classLoaderRegistry;
    }

    public ModuleStateHolder getModuleStateHolder() {
        return moduleStateHolder;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void close() {
    }
    
}
