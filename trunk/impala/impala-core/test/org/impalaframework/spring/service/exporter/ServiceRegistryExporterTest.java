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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ClassUtils;

public class ServiceRegistryExporterTest extends TestCase {

    private ServiceRegistryExporter exporter;
    private ServiceRegistryImpl registry;
    private BeanFactory beanFactory;
    
    private String service = "myservice";
    private Class<?>[] classes;
    private List<Class<?>> exportTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        classes = new Class[]{String.class};
        exporter = new ServiceRegistryExporter();
        registry = new ServiceRegistryImpl();
        beanFactory = createMock(BeanFactory.class);
        exporter.setBeanFactory(beanFactory);
        exporter.setModuleDefinition(new SimpleModuleDefinition("module1"));
        exporter.setServiceRegistry(registry);
        exportTypes = new ArrayList<Class<?>>();
        exportTypes.add(String.class);
        exportTypes.add(Object.class);
        exporter.setExportTypes(exportTypes);
        exporter.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }
    
    public void testGetBean() throws Exception {
        exporter.setBeanName("myBean");
        
        expect(beanFactory.getBean("myBean")).andReturn(service);
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        assertSame(service, registry.getService("myBean", classes).getBean());
        
        exporter.destroy();
        assertNull(registry.getService("myBean", classes));
    }
    
    public void testInvalidType() throws Exception {
        exporter.setBeanName("myBean");
        exportTypes.add(Integer.class);
        try {
            expect(beanFactory.getBean("myBean")).andReturn(service);
            replay(beanFactory);
            exporter.afterPropertiesSet();
            fail();
        } catch (InvalidStateException e) {
        }
    }
    
    public void testGetBeanAlternative() throws Exception {
        exporter.setBeanName("myBean");
        exporter.setExportName("exportName");
        
        expect(beanFactory.getBean("myBean")).andReturn(service);
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        ServiceRegistryReference serviceReference = registry.getService("exportName", classes);
        assertSame(service, serviceReference.getBean());
        
        exporter.destroy();
        assertNull(registry.getService("exportName", classes));
    }
    
    public void testTagsAndAttribute() throws Exception {
        exporter.setBeanName("myBean");
        Map<String, String> attributes = Collections.singletonMap("attribute1", "value1");
        exporter.setAttributes(attributes);
        
        expect(beanFactory.getBean("myBean")).andReturn(service);
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        ServiceRegistryReference s = registry.getService("myBean", classes);
        assertSame(service, s.getBean());
        assertEquals(attributes, s.getAttributes());
        
        exporter.destroy();
        assertNull(registry.getService("myBean", classes));
    }
    
}
