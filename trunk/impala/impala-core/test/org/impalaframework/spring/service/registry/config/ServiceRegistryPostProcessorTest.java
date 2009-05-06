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

package org.impalaframework.spring.service.registry.config;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.service.proxy.DefaultProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.ProxyFactoryCreator;
import org.impalaframework.spring.service.registry.config.ServiceRegistryPostProcessor;

public class ServiceRegistryPostProcessorTest extends TestCase {

    public final void testPostProcessBeforeInitialization() {
        ServiceRegistry registry = new ServiceRegistryImpl();
        ProxyFactoryCreator serviceProxyFactoryCreator = new DefaultProxyFactoryCreator();
        ServiceRegistryPostProcessor postProcessor = new ServiceRegistryPostProcessor(registry, serviceProxyFactoryCreator);
        TestRegistryAware testAware = new TestRegistryAware();
        postProcessor.postProcessBeforeInitialization(testAware, null);
        assertSame(registry, testAware.getServiceRegistry());
        
        assertSame(testAware, postProcessor.postProcessAfterInitialization(testAware, null));
    }
}

class TestRegistryAware implements ServiceRegistryAware {

    private ServiceRegistry serviceRegistry;

    ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }


}
