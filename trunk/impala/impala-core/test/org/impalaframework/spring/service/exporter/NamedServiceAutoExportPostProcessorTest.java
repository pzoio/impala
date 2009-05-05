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

package org.impalaframework.spring.service.exporter;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.NamedContributionEndpoint;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class NamedServiceAutoExportPostProcessorTest extends TestCase {

    private NamedServiceAutoExportPostProcessor p;
    private DefaultListableBeanFactory beanFactory;
    private DefaultListableBeanFactory parentBeanFactory;
    private NamedContributionEndpoint endPoint;
    private FactoryBean factoryBean;
    private ServiceRegistry serviceRegistry;
    private Class<?>[] classes;

    public void setUp()
    {
        classes = new Class[]{Object.class};
        p = new NamedServiceAutoExportPostProcessor();
        beanFactory = createMock(DefaultListableBeanFactory.class);
        parentBeanFactory = createMock(DefaultListableBeanFactory.class);
        endPoint = createMock(NamedServiceProxyFactoryBean.class);
        factoryBean = createMock(FactoryBean.class);
        serviceRegistry = new ServiceRegistryImpl();
        p.setBeanFactory(beanFactory);
        p.setServiceRegistry(serviceRegistry);
        p.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }
    
    public void testNull() {
        p.setBeanFactory(null);
        p.postProcessAfterInitialization(new Object(), "mybean");
        ModuleContributionUtils.findContributionEndPoint(beanFactory, "mybean");
    }
    
    public void testPostProcessAfterInitialization() {
        expectFactoryBean();
        Object object = new Object();
        p.setModuleDefinition(new SimpleModuleDefinition("pluginName"));
        
        expect(endPoint.getExportName()).andReturn("anotherExportName");
        
        replay(beanFactory);
        replay(parentBeanFactory);
        replay(endPoint);
        assertEquals(object, p.postProcessAfterInitialization(object, "mybean"));
        verify(beanFactory);
        verify(parentBeanFactory);
        verify(endPoint);
        
        //because anotherExportName is used, no service is exported
        assertNull(serviceRegistry.getService("mybean", classes, false));
    }
    
    public void testPostProcessWithOtherExportName() {
        expectFactoryBean();
        Object object = new Object();
        p.setModuleDefinition(new SimpleModuleDefinition("pluginName"));
        
        expect(endPoint.getExportName()).andReturn("mybean");
        
        replay(beanFactory);
        replay(parentBeanFactory);
        replay(endPoint);
        assertEquals(object, p.postProcessAfterInitialization(object, "mybean"));
        verify(beanFactory);
        verify(parentBeanFactory);
        verify(endPoint);
        
        ServiceRegistryEntry service = serviceRegistry.getService("mybean", classes, false);
        assertSame(object, service.getServiceBeanReference().getService());
    }
    
    
    public void testPostProcessAfterInitializationFactoryBean() throws Exception {
        expectFactoryBean();
        expect(factoryBean.getObject()).andReturn("value");

        //verify that if the object is a factory bean
        //then the registered object is the factoryBean.getObject()
        p.setModuleDefinition(new SimpleModuleDefinition("pluginName"));
        
        expect(endPoint.getExportName()).andReturn("mybean");
        
        replay(beanFactory);
        replay(parentBeanFactory);
        replay(endPoint);
        replay(factoryBean);
        assertEquals(factoryBean, p.postProcessAfterInitialization(factoryBean, "mybean"));

        ServiceRegistryEntry service = serviceRegistry.getService("mybean", classes, false);
        assertNotNull(service.getServiceBeanReference().getService());
        
        verify(beanFactory);
        verify(parentBeanFactory);
        verify(endPoint);
        verify(factoryBean);
    }
    
    
    public void testFindFactoryBean() {
        expectFactoryBean();
        
        replay(beanFactory);
        replay(parentBeanFactory);
        assertEquals(endPoint, ModuleContributionUtils.findContributionEndPoint(beanFactory, "mybean"));
        verify(beanFactory);
        verify(parentBeanFactory);
    }

    private void expectFactoryBean() {
        expect(beanFactory.getParentBeanFactory()).andReturn(parentBeanFactory);
        expect(parentBeanFactory.containsBean("&mybean")).andReturn(true);
        expect(parentBeanFactory.getBean("&mybean")).andReturn(endPoint);
    }

}
