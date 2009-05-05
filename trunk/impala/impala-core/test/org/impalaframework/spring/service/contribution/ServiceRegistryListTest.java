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

package org.impalaframework.spring.service.contribution;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.springframework.util.ClassUtils;

public class ServiceRegistryListTest extends TestCase {

    private ServiceRegistryList list;
    private ServiceRegistry serviceRegistry;
    private Class<?>[] supportedTypes;
    private LdapServiceReferenceFilter filter;
    private ClassLoader classLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        list = new ServiceRegistryList();
        
        serviceRegistry = new ServiceRegistryImpl();
        list.setServiceRegistry(serviceRegistry);
        supportedTypes = new Class[]{ List.class };
        list.setProxyTypes(supportedTypes);
        
        filter = new LdapServiceReferenceFilter("(name=*)");
        list.setFilter(filter);
        
        classLoader = ClassUtils.getDefaultClassLoader();
    }
    
    public void testWithList() throws Exception {

        serviceRegistry = createMock(ServiceRegistry.class);
        list.setServiceRegistry(serviceRegistry);
        List<String> service = new ArrayList<String>();
        ServiceRegistryEntry ref = new BasicServiceRegistryEntry(service, "mybean", "mymodule", ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryEntry> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, supportedTypes, false)).andReturn(singletonList);
        expect(serviceRegistry.addEventListener(list)).andReturn(true);
        
        replay(serviceRegistry);
        
        list.afterPropertiesSet();
        assertFalse(list.isEmpty());
        
        assertTrue(list.get(0) instanceof List);
        assertFalse(list.get(0) instanceof ArrayList);
        
        verify(serviceRegistry);
        
        list.remove(ref);
        assertTrue(list.isEmpty());
    }
    
    public void testWithNoExportTypes() throws Exception {
        List<String> service = new ArrayList<String>();
        serviceRegistry.addService("mybean", "mymodule", service, null, Collections.singletonMap("name", "value"), classLoader);
        list.afterPropertiesSet();
        
        assertFalse(list.isEmpty());
    }
    
    public void testWithExportTypes() throws Exception {
        list.setExportTypes(supportedTypes);
        
        List<String> service = new ArrayList<String>();
        serviceRegistry.addService("mybean", "mymodule", service, null, Collections.singletonMap("name", "value"), classLoader);
        list.afterPropertiesSet();
        
        assertTrue(list.isEmpty());
            
        //add another service which does not specify export types
        serviceRegistry.addService("mybean", "mymodule", service, null, Collections.singletonMap("name", "value"), classLoader);
        assertTrue(list.isEmpty());

        //add another service which does specify export types
        serviceRegistry.addService("mybean", "mymodule", service, Arrays.asList(supportedTypes), Collections.singletonMap("name", "value"), classLoader);
        assertFalse(list.isEmpty());   
    }
    
    public void testWithExportTypesBeforeInit() throws Exception {
        list.setExportTypes(supportedTypes);
        
        List<String> service = new ArrayList<String>();
        serviceRegistry.addService("mybean", "mymodule", service, Arrays.asList(supportedTypes), Collections.singletonMap("name", "value"), classLoader);
        list.afterPropertiesSet();
        
        assertFalse(list.isEmpty());
    }
    
    static class ValueClass {
        public void sayHello() {
            System.out.println("Hello");
        }
    }
    
}
