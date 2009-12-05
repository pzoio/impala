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

package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;

/**
 * Implementation of <code>HttpRequestWrapperFactory</code> which returns instance of <code>ModuleAwareWrapperHttpServletRequest</code>.
 * 
 * @see ModuleAwareWrapperHttpServletRequest
 * @see HttpRequestWrapperFactory
 * @author Phil Zoio
 */
public class ModuleAwareRequestWrapperFactory implements HttpRequestWrapperFactory {
    
    private boolean enableModuleSessionProtection;
    
    private boolean enablePartitionedServletContext;
    
    private WebAttributeQualifier webAttributeQualifier;
    
    public HttpServletRequest getWrappedRequest(HttpServletRequest request, ServletContext servletContext, RequestModuleMapping moduleMapping, String applicationId) {
        
        final HttpSessionWrapper sessionWrapper;
        if (enableModuleSessionProtection || enablePartitionedServletContext) {
            ModuleAwareHttpSessionWrapper httpSessionWrapper = new ModuleAwareHttpSessionWrapper();
            httpSessionWrapper.setServletContext(servletContext);
            httpSessionWrapper.setWebAttributeQualifier(webAttributeQualifier);
            httpSessionWrapper.setEnableModuleSessionProtection(enableModuleSessionProtection);
            httpSessionWrapper.setEnablePartitionedServletContext(enablePartitionedServletContext);
            sessionWrapper = httpSessionWrapper;
        } else {

            IdentityHttpSessionWrapper httpSessionWrapper = new IdentityHttpSessionWrapper();
            sessionWrapper = httpSessionWrapper;
        }
        
        return new MappedWrapperHttpServletRequest(servletContext, request, sessionWrapper, moduleMapping, applicationId);
    }

    public void setEnableModuleSessionProtection(boolean enableModuleSessionProtection) {
        this.enableModuleSessionProtection = enableModuleSessionProtection;
    }
    
    public void setEnablePartitionedServletContext(boolean enablePartitionedServletContext) {
        this.enablePartitionedServletContext = enablePartitionedServletContext;
    }
    
    public void setWebAttributeQualifier(WebAttributeQualifier webAttributeQualifier) {
        this.webAttributeQualifier = webAttributeQualifier;
    }
}
