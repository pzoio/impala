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

package org.impalaframework.facade;

import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.BeanFactory;

/**
 * A facade by which many of the Impala module management interfaces can be
 * accessed.
 * 
 * @author Phil Zoio
 */
public interface ModuleManagementFacade extends BeanFactory {

    ModuleLocationResolver getModuleLocationResolver();

    ModuleLoaderRegistry getModuleLoaderRegistry();

    ModificationExtractorRegistry getModificationExtractorRegistry();

    TransitionProcessorRegistry getTransitionProcessorRegistry();

    ModuleStateHolder getModuleStateHolder();

    ModuleOperationRegistry getModuleOperationRegistry();

    ModuleStateChangeNotifier getModuleStateChangeNotifier();

    TypeReaderRegistry getTypeReaderRegistry();
    
    ModuleRuntimeManager getModuleRuntimeManager();
    
    FrameworkLockHolder getFrameworkLockHolder();

    void close();

}
