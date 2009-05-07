package org.impalaframework.service.registry;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.service.registry.BeanRetrievingServiceRegistryTargetSource;
import org.springframework.util.ClassUtils;

public class BeanRetrievingServiceRegistryTargetSourceTest extends TestCase {

    private DelegatingServiceRegistry serviceRegistry;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new DelegatingServiceRegistry();
    }

    public void testGetUsingBean() {
        BeanRetrievingServiceRegistryTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, "mybean", new Class[]{ Object.class }, false);
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        final ServiceRegistryEntry serviceReference = serviceRegistry.addService("mybean", "moduleName", new StaticServiceBeanReference(service), ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }
    
    public void testGetUsingExportedType() {
        BeanRetrievingServiceRegistryTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, null, new Class[]{ Object.class }, true);
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        List<Class<?>> asList = Arrays.asList(new Class<?>[]{Object.class});
        final ServiceRegistryEntry serviceReference = serviceRegistry.addService("mybean", "moduleName", new StaticServiceBeanReference(service), asList, null, ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }

}
