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

package org.impalaframework.web.servlet.wrapper.request;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.util.ReflectionUtils;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.servlet.wrapper.request.IdentityHttpRequestWrapper;
import org.impalaframework.web.servlet.wrapper.request.MappedWrapperHttpServletRequest;
import org.impalaframework.web.servlet.wrapper.request.PartitionedRequestWrapper;
import org.impalaframework.web.servlet.wrapper.session.IdentityHttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.session.PartitionedHttpSessionWrapper;

public class HttpRequestWrapperFactoryTest extends TestCase {
    
    private RequestModuleMapping moduleMapping;
    private String applicationId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleMapping = new RequestModuleMapping("/myModule", "myModule", null);
        applicationId = "applicationId";
    }

    public void testIdentityWrapper() {
        IdentityHttpRequestWrapper factory = new IdentityHttpRequestWrapper();
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        assertSame(request, factory.getWrappedRequest(request, null, null, applicationId));
        
        assertNull(factory.getWrappedRequest(null, null, null, applicationId));
    }
    
    public void testModuleWrapper() {
        PartitionedRequestWrapper factory = new PartitionedRequestWrapper();
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        final ServletContext servletContext = EasyMock.createMock(ServletContext.class);
        
        HttpServletRequest mappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
        assertTrue(mappedRequest instanceof MappedWrapperHttpServletRequest);
        MappedWrapperHttpServletRequest mr = (MappedWrapperHttpServletRequest) mappedRequest;
        assertSame(request, mr.getRequest());
        assertTrue(ReflectionUtils.getFieldValue(mr, "httpSessionWrapper", HttpSessionWrapper.class) instanceof IdentityHttpSessionWrapper);
        
        factory.setEnableModuleSessionProtection(true);
        final HttpServletRequest wrappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
        assertTrue(wrappedRequest instanceof MappedWrapperHttpServletRequest);
        assertTrue(ReflectionUtils.getFieldValue(wrappedRequest, "httpSessionWrapper", HttpSessionWrapper.class) instanceof PartitionedHttpSessionWrapper);
    }

}
