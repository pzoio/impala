/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.module.factory;

import junit.framework.TestCase;

import org.impalaframework.module.application.ImpalaApplication;
import org.impalaframework.module.spi.Application;

public class SimpleApplicationFactoryTest extends TestCase {

    public void testAfterPropertiesSet() throws Exception {
        SimpleApplicationFactory factory = new SimpleApplicationFactory();
        factory.setClassLoaderRegistryFactory(new SimpleClassLoaderRegistryFactory());
        factory.setServiceRegistryFactory(new SimpleServiceRegistryFactory());
        factory.setModuleStateHolderFactory(new SimpleModuleStateHolderFactory());   

        Application application = factory.newApplication(null);
        assertTrue(application instanceof ImpalaApplication);
        
        assertNotNull(application.getClassLoaderRegistry());
        assertNotNull(application.getModuleStateHolder());
        assertNotNull(application.getClassLoaderRegistry());
    }

}
