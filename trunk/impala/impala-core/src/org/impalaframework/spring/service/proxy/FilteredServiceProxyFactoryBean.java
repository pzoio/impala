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
import java.util.List;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.contribution.BaseServiceRegistryList;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.BaseServiceRegistryTargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

/**
 * The <code>FilteredServiceProxyFactoryBean</code> is used to retrieve a single service reference where the 
 * reference is obtained from the service registry using a RFC 1960-based service filter.
 * 
 * The implementation is backed by a {@link ServiceRegistryList} instance.
 * 
 * @see BasicServiceRegistryReference
 * @author Phil Zoio
 */
public class FilteredServiceProxyFactoryBean extends BaseServiceProxyFactoryBean implements DisposableBean {

    private static final long serialVersionUID = 1L;

    private Class<?>[] proxyTypes;
    
    private Class<?>[] exportTypes;

    private String filterExpression;

    private ServiceRegistryList list;
    
    /* *************** Abstract superclass method implementation ************** */

    protected ProxyFactory createProxyFactory() {
        
        Assert.notNull(filterExpression, "filterExpression cannot be null. If you have no filter exporession to provide, consider using " + TypedServiceProxyFactoryBean.class.getName());
        
        final Class<?>[] proxyTypesToUse = getProxyTypesToUse();
        
        list = new ServiceRegistryList();
        list.setServiceRegistry(getServiceRegistry());
        list.setFilterExpression(filterExpression);
        list.setExportTypes(exportTypes);
        list.init();
        
        ListBackedProxySource source = new ListBackedProxySource(list, proxyTypesToUse);
        
        ProxyFactory createDynamicProxyFactory = getProxyFactoryCreator().createProxyFactory(source, getBeanName());
        return createDynamicProxyFactory;
    }

    Class<?>[] getProxyTypesToUse() {
        final Class<?>[] proxyTypesToUse;
        if (proxyTypes == null || proxyTypes.length == 0) {
            Assert.isTrue(exportTypes != null && exportTypes.length > 0, "exportTypes and proxyTypes cannot both be empty");
            proxyTypesToUse = exportTypes;
        } else {
            proxyTypesToUse = proxyTypes;
        }
        return proxyTypesToUse;
    }

    /* *************** Package level methods ************** */
    
    ServiceRegistryList getList() {
        return list;
    }
    
    /* *************** DisposableBean implementation ************** */
    
    public void destroy() throws Exception {
        list.destroy();
    }

    /* *************** dependency injection setters ************** */

    /**
     * Sets proxy types for exposed bean. Proxy for service exposed using the types provided.
     * If single type is provided, and this type is a non-final concrete class, then 
     * class-based proxy using CGLIB is used.
     * 
     * Can only be null or empty if {@link #exportTypes} is not null or empty.
     */
    public void setProxyTypes(Class<?>[] proxyTypes) {
        this.proxyTypes = proxyTypes;
    }
    
    /**
     * Sets types under which bean must have been exported to service registry to be picked up.
     * If proxyTypes not set, then this is also used for the proxyTypes.
     * 
     * Can be null.
     */
    public void setExportTypes(Class<?>[] exportTypes) {
        this.exportTypes = exportTypes;
    }

    /**
     * The filter expression used to limit the types. Cannot be null. If you don't want to use a filter
     * use, {@link TypedServiceProxyFactoryBean} instead
     * @param filterExpression a RFC 1960 compliant filter expression.
     */
    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

class ListBackedProxySource extends BaseProxyFactorySource {

    private ServiceRegistryList list;

    private Class<?>[] proxyTypes;

    public ListBackedProxySource(ServiceRegistryList list,
            Class<?>[] proxyTypes) {
        super();
        this.list = list;
        this.proxyTypes = proxyTypes;
    }

    public void init() {
        
        final Class<?> targetClass;
        
        if (proxyTypes.length == 1) {
            if (!proxyTypes[0].isInterface()) {
                boolean isFinal = Modifier.isFinal(proxyTypes[0].getModifiers());
                if (isFinal) {
                    throw new InvalidStateException("Cannot create proxy for bean " + getBeanName() + " as no interfaces have been " +
                            " specified and the bean class is final, therefore cannot be proxied");
                }
                targetClass = proxyTypes[0];
            } else {
                targetClass = null;
            }
        } else {
            targetClass = null;
        }
        
        ContributionEndpointTargetSource targetSource = new ListBackedRegistryTargetSource(this.list, targetClass);
        ProxyFactory proxyFactory = new ProxyFactory();
        
        if (targetSource.getTargetClass() == null) {
            //not proxying by class, so proxy by interface
            ProxyFactorySourceUtils.addInterfaces(proxyFactory, proxyTypes);
        }
        
        afterInit(proxyFactory, targetSource);
    }
}

}

class ServiceRegistryList extends BaseServiceRegistryList {

    @Override
    protected Object maybeGetProxy(ServiceRegistryReference ref) {
        return ref.getBean();
    }

    @Override
    public List<ServiceRegistryReference> getServices() {
        return super.getServices();
    }

}

class ListBackedRegistryTargetSource extends BaseServiceRegistryTargetSource {

    private ServiceRegistryList list;
    
    /**
     * Note that targetClass will only be set to non-null if it is a non-final concrete class
     */
    private Class<?> targetClass;

    public ListBackedRegistryTargetSource(ServiceRegistryList serviceRegistryList, Class<?> targetClass) {
        super();
        this.list = serviceRegistryList;
        this.targetClass = targetClass;
    }
    
    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    public ServiceRegistryReference getServiceRegistryReference() {
        List<ServiceRegistryReference> contributions = this.list.getServices();
        if (contributions.size() > 0) {
            return contributions.get(0);
        }
        return null;
    }

}