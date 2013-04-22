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

package org.impalaframework.web.resolver;

import static org.easymock.EasyMock.*;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import junit.framework.TestCase;

public class ServletContextModuleLocationResolverTest extends TestCase {

    private ServletContextModuleLocationResolver resolver;
    private ServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new ServletContextModuleLocationResolver();
        servletContext = createMock(ServletContext.class);
        resolver.setServletContext(servletContext);
        resolver.setApplicationVersion("1.0");
    }
    
    public final void testGetApplicationModuleClassLocations() {
        List<Resource> locations = resolver.getApplicationModuleClassLocations("mymodule");
        assertEquals(2, locations.size());
        assertTrue(locations.get(0) instanceof ServletContextResource);
    }
    
    public void testGetModuleSpecificJarLocations() throws Exception {
        expect(servletContext.getResource("/WEB-INF/modules/lib/mymodule")).andReturn(null);
        expect(servletContext.getRealPath("/WEB-INF/modules/lib/mymodule")).andReturn(null);
        replay(servletContext);
        List<Resource> locations = resolver.getApplicationModuleLibraryLocations("mymodule");
        assertNull(locations);
        verify(servletContext);
    }
    
    public void testGetPresentModuleSpecificJarLocations() throws Exception {
        expect(servletContext.getResource("/WEB-INF/modules/lib/mymodule")).andReturn(null);
        expect(servletContext.getRealPath("/WEB-INF/modules/lib/mymodule")).andReturn("../sample-module3/lib");
        replay(servletContext);
        List<Resource> locations = resolver.getApplicationModuleLibraryLocations("mymodule");
        assertNotNull(locations);
        assertEquals(2, locations.size());
        verify(servletContext);
    }

}
