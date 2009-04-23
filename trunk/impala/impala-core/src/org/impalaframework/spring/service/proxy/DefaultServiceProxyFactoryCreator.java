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

package org.impalaframework.spring.service.proxy;

import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.DynamicServiceRegistryTargetSource;
import org.impalaframework.spring.service.registry.StaticServiceRegistryTargetSource;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ServiceProxyFactoryCreator} which is used to create proxy for service 
 * obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public class DefaultServiceProxyFactoryCreator implements ServiceProxyFactoryCreator, ServiceRegistryAware {
    
    private static final Log logger = LogFactory.getLog(ContributionProxyFactoryBean.class);
    
    private ServiceRegistry serviceRegistry;
    
    /**
     * True if the interceptor should allow the call to proceed (with a dummy
     * value returned) if no service is present. Primarily present for testing
     * purposes. Defaults to false.
     */
    private boolean allowNoService = false;

    /**
     * Whether to set the context class loader to the class loader of the module
     * contributing the bean being invoked. This is to mitigate possibility of
     * exceptions being caused by calls to
     * <code>Thread.setContextClassLoader()</code> being propagated across
     * modules
     */
    private boolean setContextClassLoader = true;
    
    /**
     * Creates proxy factory backed by a dynamically obtained service
     */
    public ProxyFactory createDynamicProxyFactory(Class<?>[] interfaces, String registryKeyName) {
        
        Assert.notNull(this.serviceRegistry, "serviceRegistry cannot be null");
        Assert.notNull(interfaces, "interfaces cannot be null");
        Assert.notEmpty(interfaces, "interfaces cannot be empty");
        
        //this will return a non-null value if single interface which is concrete class
        ContributionEndpointTargetSource targetSource = new DynamicServiceRegistryTargetSource(registryKeyName, interfaces, this.serviceRegistry);
        ProxyFactory proxyFactory = new ProxyFactory();
        
        if (targetSource.getTargetClass() == null) {
            //not proxying by class, so proxy by interface
            addInterfaces(proxyFactory, interfaces);
        }
        
        proxyFactory.setTargetSource(targetSource);
        
        ContributionEndpointInterceptor interceptor = new ContributionEndpointInterceptor(targetSource, registryKeyName);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Creating dynamic proxy for " + registryKeyName + 
                    " with allowNoService '" + allowNoService + "' and setContextClassLoader '" + setContextClassLoader + "'");
        }
        
        interceptor.setProceedWithNoService(allowNoService);
        interceptor.setSetContextClassLoader(setContextClassLoader);
        proxyFactory.addAdvice(interceptor);
        
        return proxyFactory;
    }

    /**
     * Creates proxy factory backed by an already obtained service registry reference. Here the job of the 
     * {@link TargetSource} is not to obtain the service, just to handle aspects of proxying the service.
     */
    public ProxyFactory createStaticProxyFactory(Class<?>[] interfaces, ServiceRegistryReference reference) {
        
        Assert.notNull(this.serviceRegistry, "serviceRegistry cannot be null");
        Assert.notNull(reference, "reference cannot be null");
        
        ContributionEndpointTargetSource targetSource = new StaticServiceRegistryTargetSource(reference);
        
        ProxyFactory proxyFactory = new ProxyFactory();
        if (interfaces != null && interfaces.length > 0) {
            addInterfaces(proxyFactory, interfaces);
        } else {
            boolean isFinal = Modifier.isFinal(reference.getBean().getClass().getModifiers());
            if (isFinal) {
                throw new InvalidStateException("Cannot create proxy for service reference " + reference + " as no interfaces have been " +
                		"specified and the bean class is final, therefore cannot be proxied");
            }
        }
        
        proxyFactory.setTargetSource(targetSource);
       
        final String registryKeyName = reference.getBeanName();
        ContributionEndpointInterceptor interceptor = new ContributionEndpointInterceptor(targetSource, registryKeyName);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Creating static proxy for " + registryKeyName + 
                    " with allowNoService '" + allowNoService + "' and setContextClassLoader '" + setContextClassLoader + "'");
        }
        
        interceptor.setProceedWithNoService(allowNoService);
        interceptor.setSetContextClassLoader(setContextClassLoader);
        proxyFactory.addAdvice(interceptor);
        
        return proxyFactory;
    }

    private void addInterfaces(ProxyFactory proxyFactory, Class<?>[] interfaces) {
        
        for (int i = 0; i < interfaces.length; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding interface " + interfaces[i] + " loaded from " + interfaces[i].getClassLoader());
            }
            proxyFactory.addInterface(interfaces[i]);
        }
    }

    /**
     * Implementation of {@link ServiceRegistryAware}. Allows service registry to be automatically be injected.
     */
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Sets to true if the interceptor should allow the call to proceed (with a dummy value returned) if no service is
     * present. Primarily present for testing purposes. Defaults to false.
     */
    public void setAllowNoService(boolean allowNoService) {
        this.allowNoService = allowNoService;
    }
    
    /**
     * Sets to true if the interceptor should set the current threads context class loader to the class loader of the module
     * responsible for loading the target bean. Defaults to true.
     * 
     * @see Thread#setContextClassLoader(ClassLoader)
     */
    public void setSetContextClassLoader(boolean setContextClassLoader) {
        this.setContextClassLoader = setContextClassLoader;
    }
    
}
