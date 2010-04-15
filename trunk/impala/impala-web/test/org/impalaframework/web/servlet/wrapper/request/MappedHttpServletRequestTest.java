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

package org.impalaframework.web.servlet.wrapper.request;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.impalaframework.web.servlet.wrapper.CacheableHttpSession;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.servlet.wrapper.session.IdentityHttpSessionWrapper;

public class MappedHttpServletRequestTest extends TestCase {
    
    private HttpServletRequest request;
    private ServletContext servletContext;
    private IdentityHttpSessionWrapper httpSessionWrapper;
    private String applicationId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = createMock(HttpServletRequest.class);
        servletContext = createMock(ServletContext.class);
        httpSessionWrapper = new IdentityHttpSessionWrapper();
        applicationId = "applicationId";
    }
    
    public void testIsForwardOrIncludeIsForward() throws Exception {
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);
    
        expect(request.getAttribute("javax.servlet.forward.request_uri")).andReturn("/forward");
        replay(request);

        assertTrue(wrapper.isForwardOrInclude());
        verify(request);
    }
    
    public void testIsForwardOrIncludeIsInclude() throws Exception {
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);
        
        expect(request.getAttribute("javax.servlet.forward.request_uri")).andReturn(null);
        expect(request.getAttribute("javax.servlet.include.request_uri")).andReturn("/include");
        replay(request);

        assertTrue(wrapper.isForwardOrInclude());
        verify(request);
    }
    
    public void testIsForwardOrIncludeIsNeither() throws Exception {
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);
        
        expect(request.getAttribute("javax.servlet.forward.request_uri")).andReturn(null);
        expect(request.getAttribute("javax.servlet.include.request_uri")).andReturn(null);
        replay(request);

        assertFalse(wrapper.isForwardOrInclude());
        verify(request);
    }

    public void testWithServletPath() {

        expect(request.getContextPath()).andReturn("/app").times(2);
        expect(request.getRequestURI()).andReturn("/app/servletPath/extra/path/info");
        expectNotForwardOrInclude();
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", null, "/servletPath"), applicationId);
        assertEquals("/servletPath", wrapper.getServletPath());
        assertEquals("/extra/path/info", wrapper.getPathInfo());
        assertEquals("/app", wrapper.getContextPath());
        
        verify(request, servletContext);
    }
    
    public void testWithServletAndContextPath() {

        expect(request.getContextPath()).andReturn("/app").times(2);
        expect(request.getRequestURI()).andReturn("/app/cp/servletPath/extra/path/info");
        expectNotForwardOrInclude();
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", "/cp", "/servletPath"), applicationId);
        assertEquals("/servletPath", wrapper.getServletPath());
        assertEquals("/extra/path/info", wrapper.getPathInfo());
        assertEquals("/app/cp", wrapper.getContextPath());
        
        verify(request, servletContext);
    }
    
    public void testNoServletPathInfo() {
        
        //note no expected calls in constructor
        
        expect(request.getServletPath()).andReturn("/sp");
        expect(request.getPathInfo()).andReturn("/pi");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);
        assertEquals("/sp", wrapper.getServletPath());
        assertEquals("/pi", wrapper.getPathInfo());
        
        verify(request, servletContext);
    }
    
    public void testNoServletPathTranslated() {
        
        //note no expected calls in constructor

        expect(request.getPathInfo()).andReturn("/pi");
        expect(servletContext.getRealPath("/pi")).andReturn("rp");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);
        assertEquals("rp", wrapper.getPathTranslated());
        
        verify(request, servletContext);
    }
    
    public void testUnexpectedServletPath() {

        expectNotForwardOrInclude();
        expect(request.getContextPath()).andReturn("/cp");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", null, "/unexpectedServletPath"), applicationId);
        assertEquals("/unexpectedServletPath", wrapper.getServletPath());
        
        verify(request, servletContext);
    }
    
    public void testWithGetPathTranslated() {
        
        expectConstruct();
        expectNotForwardOrInclude();
        
        expect(servletContext.getRealPath("/extra/path/info")).andReturn("/realpath");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", null, "/servletPath"), applicationId);
        assertEquals("/realpath", wrapper.getPathTranslated());
        
        verify(request, servletContext);
    }
    
    public void testGetWrappedSessionNull() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        
        expect(request.getAttribute(MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION")).andReturn(null);
        
        replay(request, sessionWrapper);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertNull(wrapper.getCachedSession());
        
        verify(request, sessionWrapper);       
    }
    
    public void testGetWrappedSession() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final HttpSession session = createMock(HttpSession.class);
        
        expect(request.getAttribute(MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION")).andReturn(session);
        
        replay(request, sessionWrapper);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertNull(wrapper.getCachedSession());
        
        verify(request, sessionWrapper);       
    }
    
    public void testGetWrappedCacheableSessionValid() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final CacheableHttpSession session = createMock(CacheableHttpSession.class);
        
        expect(request.getAttribute(MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION")).andReturn(session);
        expect(session.isValid()).andReturn(true);
        
        replay(request, sessionWrapper, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertSame(session, wrapper.getCachedSession());
        
        verify(request, sessionWrapper, session);       
    }
    
    public void testGetWrappedCacheableSessionInvalid() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final CacheableHttpSession session = createMock(CacheableHttpSession.class);
        
        final String attribute = MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION";
        expect(request.getAttribute(attribute)).andReturn(session);
        expect(session.isValid()).andReturn(false);
        request.removeAttribute(attribute);
        
        replay(request, sessionWrapper, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertNull(wrapper.getCachedSession());
        
        verify(request, sessionWrapper, session);       
    }
    
    public void testMaybeCacheTrue() throws Exception {

        final CacheableHttpSession session = createMock(CacheableHttpSession.class);
        
        final String attribute = MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION";
        request.setAttribute(attribute, session);
        
        replay(request, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);

        wrapper.maybeCacheSession(session);
        
        verify(request, session);       
    }

    public void testMaybeCacheFalse() throws Exception {

        final HttpSession session = createMock(HttpSession.class);
        
        replay(request, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);

        wrapper.maybeCacheSession(session);
        
        verify(request, session);       
    }
    
    public void testGetPathInfoWithMatchingContextAndServletPath() throws Exception {
        
        expect(request.getContextPath()).andReturn("/cp");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, 
                new RequestModuleMapping("/sp-path", "module", null, "/sp"), applicationId);
        assertEquals("/myuri", wrapper.getPathInfo("/cp/sp/myuri"));
        
        verify(request, servletContext);
    }
    
    public void testGetPathInfoWithMatchingContextPathOnly() throws Exception {
        
        expect(request.getContextPath()).andReturn("/cp").times(2);
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, 
                new RequestModuleMapping("/sp-path", "module", null, "/nonmatching"), applicationId);
        assertEquals("/sp/myuri", wrapper.getPathInfo("/cp/sp/myuri"));
        
        verify(request, servletContext);
    }
    
    public void testGetPathInfoWithNoMatch() throws Exception {
        
        expect(request.getContextPath()).andReturn("/nonmatching").times(2);
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, 
                new RequestModuleMapping("/sp-path", "module", null, "/nonmatching"), applicationId);
        assertEquals("/cp/sp/myuri", wrapper.getPathInfo("/cp/sp/myuri"));
        
        verify(request, servletContext);
    }
    
    public void testGetPathInfoWithServletPath() throws Exception {
        
        expect(request.getContextPath()).andReturn("/cp");
        expect(request.getRequestURI()).andReturn("/cp/sp/myuri");
        expectNotForwardOrInclude();
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, 
                new RequestModuleMapping("/sp-path", "module", null, "/sp"), applicationId);
        assertEquals("/myuri", wrapper.getPathInfo());
        
        verify(request, servletContext);
    }
    
    public void testGetPathInfoWithNoServletPath() throws Exception {
        
        expect(request.getPathInfo()).andReturn("/pathinfo");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, 
                new RequestModuleMapping("/sp-path", "module", null, null), applicationId);
        assertEquals("/pathinfo", wrapper.getPathInfo());
        
        verify(request, servletContext);
    }
    
    private void expectConstruct() {
        expect(request.getContextPath()).andReturn("/app");
        expect(request.getRequestURI()).andReturn("/app/servletPath/extra/path/info");
    }

    private void expectNotForwardOrInclude() {
        //FIXME add test which include these
        expect(request.getAttribute("javax.servlet.forward.request_uri")).andStubReturn(null);
        expect(request.getAttribute("javax.servlet.include.request_uri")).andStubReturn(null);
    }

}
