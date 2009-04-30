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

import org.springframework.aop.framework.ProxyFactory;

/**
 * The <code>NamedTypesProxyFactoryBean</code> works under the assumption that the service was exported against a 
 * one or more types. The property {@link #exportTypes} denotes the types which must have been used to export the service
 * to the service registry.
 * 
 * @see NamedServiceProxyFactoryBean
 * @author Phil Zoio
 */
public class TypedServiceProxyFactoryBean extends BaseServiceProxyFactoryBean {

    private static final long serialVersionUID = 1L;

    private Class<?>[] exportTypes;

    /* *************** Abstract superclass method implementation ************** */

    protected ProxyFactory createProxyFactory() {
        
        BeanRetrievingProxyFactorySource source = new BeanRetrievingProxyFactorySource(super.getServiceRegistry(), exportTypes, getBeanName(), true);
        
        ProxyFactory createDynamicProxyFactory = getProxyFactoryCreator().createProxyFactory(source, getBeanName());
        return createDynamicProxyFactory;
    }

    /* *************** dependency injection setters ************** */

    public void setExportTypes(Class<?>[] proxyTypes) {
        this.exportTypes = proxyTypes;
    }
    
}
