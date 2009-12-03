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

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.springframework.util.Assert;

/**
 * Implementation of {@link HttpSession} which attempts to protect session attribute values 
 * across module reloads through special implementation of {@link #getAttribute(String)}
 * @author Phil Zoio
 */
public class ModuleAwareHttpSession extends DelegatingWrapperHttpSession {
    
    private final WebAttributeQualifier webAttributeQualifier;
    
    private final String applicationId;
    
    private final String moduleName;
    
	public ModuleAwareHttpSession(HttpSession realSession,
            WebAttributeQualifier webAttributeQualifier, 
            String applicationId,
            String moduleName) {
        super(realSession);
        
        Assert.notNull(webAttributeQualifier, "webAttributeQualifier cannot be null");
        
        this.webAttributeQualifier = webAttributeQualifier;
        this.applicationId = applicationId;
        this.moduleName = moduleName;
    }

    @Override
	public Object getAttribute(String name) {
        Assert.notNull(name);
        final String qualifiedAttributeName = webAttributeQualifier.getQualifiedAttributeName(name, applicationId, moduleName);
        return super.getAttribute(qualifiedAttributeName);
	}
	
    @Override
    @SuppressWarnings("unchecked")
	public Enumeration<String> getAttributeNames() {
        Enumeration<String> attributeNames = super.getAttributeNames();
        
        return webAttributeQualifier.filterAttributeNames(attributeNames, applicationId, moduleName);
	}

}
