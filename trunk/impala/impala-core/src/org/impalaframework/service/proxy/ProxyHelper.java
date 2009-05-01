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

package org.impalaframework.service.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.util.ArrayUtils;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.aop.framework.ProxyFactory;

public class ProxyHelper {

    private static Log logger = LogFactory.getLog(ProxyHelper.class);
    
    //whether to proxy entries or not. By default true
    private boolean proxyEntries = true;
    
    //the interfaces to use for proxies
    private Class<?>[] proxyInterfaces; 
    
    /**
     * Possibly wraps bean with proxy. Based on following rules:
     * <ul>
     * <li>if proxyEntries is false, then does not wrap
     * <li>if the bean does not implement any interface, then does not wrap
     * <li>if proxyInterfaces is not empty, but bean does not implement any of the specified interface, then does not wrap
     * <li>if proxyInterfaces is not empty, then wraps with proxy, exposing with interfaces which are both are in proxyInterfaces and implemented by bean
     * <li>if proxyInterfaces is null or empty, then wraps with proxy, exposing with all interfaces implemented by bean
     * </ul>
     */
    public Object maybeGetProxy(ServiceRegistryReference reference) {
        
        Object bean = reference.getBean();
        
        if (!proxyEntries) {
            return bean;
        }
        
        final List<Class<?>> interfaces = ReflectionUtils.findInterfaceList(bean);
        
        if (interfaces.size() == 0) {
            logger.warn("Bean of instance " + bean.getClass().getName() + " could not be backed by a proxy as it does not implement an interface");
        //TODO should be try to use class-based proxy here using CGLIB
        return bean;
    }
    
    final Class<?>[] proxyInterfaces = this.proxyInterfaces;
    
    final List<Class<?>> filteredInterfaces = filterInterfaces(interfaces,proxyInterfaces);
    
    if (filteredInterfaces.size() == 0) {
        logger.warn("Bean of instance " + bean.getClass().getName() + " does not implement any of the specified interfaces: " + proxyInterfaces);
        //TODO should be try to use class-based proxy here using CGLIB
            return bean;
        }
        
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(filteredInterfaces.toArray(new Class[0]));
        proxyFactory.setTarget(bean);
        final Object proxyObject = proxyFactory.getProxy();
        return proxyObject;
    }
    
    private static List<Class<?>> filterInterfaces(final List<Class<?>> interfaces, final Class<?>[] proxyInterfaces) {
        final List<Class<?>> filteredInterfaces = new ArrayList<Class<?>>();
        if (!ArrayUtils.isNullOrEmpty(proxyInterfaces)) {
            
            //only proxy if we have a match
            for (int i = 0; i < proxyInterfaces.length; i++) {
                final Class<?> proxyInterface = proxyInterfaces[i];
                if (interfaces.contains(proxyInterface)) {
                    filteredInterfaces.add(proxyInterface);
                }
            }
    
        } else {
            filteredInterfaces.addAll(interfaces);
        }
        return filteredInterfaces;
    }

    public void setProxyEntries(boolean proxyEntries) {
        this.proxyEntries = proxyEntries;
    }
    
    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }
}
