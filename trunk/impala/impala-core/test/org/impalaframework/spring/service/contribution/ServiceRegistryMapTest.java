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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.springframework.util.ClassUtils;

public class ServiceRegistryMapTest extends TestCase {

    private ServiceRegistryMap map;
    private ServiceRegistry serviceRegistry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        map = new ServiceRegistryMap();
        serviceRegistry = createMock(ServiceRegistry.class);
        map.setServiceRegistry(serviceRegistry);
        map.setProxyInterfaces(new Class[]{ List.class });
    }
    
    public void testNoMapKey() throws Exception {
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        map.setFilter(filter);
        
        ServiceRegistryReference ref = new BasicServiceRegistryReference("service", "mybean", "mymodule", ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryReference> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, null)).andReturn(singletonList);
        replay(serviceRegistry);
        
        map.afterPropertiesSet();
        assertTrue(map.isEmpty());
        
        verify(serviceRegistry);
    }
    
    public void testWithMapKey() throws Exception {
        List<String> service = new ArrayList<String>();
        
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        map.setFilter(filter);
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("mapkey", "key");
        ServiceRegistryReference ref = new BasicServiceRegistryReference(service, "mybean", "mymodule", null, attributes, ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryReference> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, null)).andReturn(singletonList);
        
        replay(serviceRegistry);
        
        map.afterPropertiesSet();
        assertFalse(map.isEmpty());
        
        //map service is List but not same instance
        assertTrue(map.get("key") instanceof List);
        assertFalse(map.get("key") == service);
        
        verify(serviceRegistry);
    }
}
