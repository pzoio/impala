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

package org.impalaframework.service.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.impalaframework.service.ServiceRegistryReference;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link ServiceRegistryReference}. Objects of this class are immutable.
 * @author Phil Zoio
 */
public class BasicServiceRegistryReference implements ServiceRegistryReference {

    private final Object bean;
    private final String beanName;
    private final String contributingModule;
    private final Map<String, ?> attributes;
    private final ClassLoader beanClassLoader;
    private List<Class<?>> exportedTypes;

    @SuppressWarnings("unchecked")
    public BasicServiceRegistryReference(Object bean, 
            String beanName,
            String contributingModule, 
            ClassLoader classLoader) {
        this(bean, beanName, contributingModule, null, Collections.EMPTY_MAP, classLoader);
    }

    @SuppressWarnings("unchecked")
    public BasicServiceRegistryReference(Object bean, 
            String beanName,
            String contributingModule, 
            List<Class<?>> exportedTypes,
            Map<String, ?> attributes, 
            ClassLoader classLoader) {
        super();
        Assert.notNull(bean, "service instance cannot be null");
        Assert.notNull(contributingModule, "contributingModule cannot be null");
        Assert.notNull(classLoader, "classLoader cannot be null");
        this.bean = bean;
        this.beanName = beanName;
        this.contributingModule = contributingModule;
        this.exportedTypes = (exportedTypes != null ? new LinkedList<Class<?>>(exportedTypes) : Collections.EMPTY_LIST);
        this.attributes = (attributes != null ? new HashMap(attributes) : Collections.EMPTY_MAP);
        this.beanClassLoader = classLoader;
    }

    public final Object getBean() {
        return bean;
    }
    
    public List<Class<?>> getExportedTypes() {
        return exportedTypes;
    }

    public final String getBeanName() {
        return beanName;
    }

    public final String getContributingModule() {
        return contributingModule;
    }

    public final Map<String, ?> getAttributes() {
        return attributes;
    }

    public final ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

}
