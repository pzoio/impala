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

package org.impalaframework.spring.module.loader;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.impalaframework.spring.service.proxy.ProxyFactoryCreator;
import org.impalaframework.spring.service.registry.config.ServiceRegistryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Phil Zoio
 */
public class DefaultApplicationContextLoader extends BaseApplicationContextLoader {

    private ServiceRegistry serviceRegistry;
    
    private ProxyFactoryCreator serviceProxyFactoryCreator;

    public DefaultApplicationContextLoader() {
    }
    
    protected void addBeanPostProcessors(ModuleDefinition definition, ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry, serviceProxyFactoryCreator));
        beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(definition));
    }
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
    
    public void setServiceProxyFactoryCreator(ProxyFactoryCreator serviceProxyFactoryCreator) {
        this.serviceProxyFactoryCreator = serviceProxyFactoryCreator;
    }
    
}
