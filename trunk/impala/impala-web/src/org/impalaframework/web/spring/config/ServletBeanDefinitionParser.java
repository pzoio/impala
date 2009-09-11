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

package org.impalaframework.web.spring.config;

import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServlet;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServletFactoryBean;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.impalaframework.web.spring.servlet.InternalModuleServlet;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;


/**
 * Bean definition parser for <i>servlet</i> element from <i>web</i> namespace.
 * 
 * @author Phil Zoio
 */
public class ServletBeanDefinitionParser extends AbstractWebHandlerBeanDefinitionParser {

    private static final String DELEGATOR_SERVLET_NAME_ATTRIBUTE =  "delegatorServletName";

    private static final String SERVLET_NAME_PROPERTY = "servletName";
    private static final String SERVLET_CLASS_PROPERTY = "servletClass";
    
    private static final String DELEGATE_SERVLET_PROPERTY = "delegateServlet";
    
    public ServletBeanDefinitionParser() {
        super();
    }

    /**
     * Suppport default handler class
     */
    protected void handlerHandlerClass(Element element, BeanDefinitionBuilder builder) {
        final String attribute = element.getAttribute(getHandlerClassAttribute());
        if (!StringUtils.hasText(attribute)) {
            builder.addPropertyValue(getHandlerClassProperty(), getDefaultHandlerClass());
        }
    }
    
    protected String getHandlerClassAttribute() {
        return SERVLET_CLASS_PROPERTY;
    }

    protected Class<?> getDefaultFactoryBeanClass() {
        return ServletFactoryBean.class;
    }
    
    protected Class<?> getDefaultHandlerClass() {
        return InternalModuleServlet.class;
    }

    protected Class<?> getIntegrationHandlerClass() {
        return InternalFrameworkIntegrationServlet.class;
    }
    
    protected Class<?> getIntegrationHandlerFactoryClass() {
        return InternalFrameworkIntegrationServletFactoryBean.class;
    }
    
    protected String getDelegateHandlerProperty() {
        return DELEGATE_SERVLET_PROPERTY;
    }
    
    protected String getDelegatorHandlerNameAttribute() {
        return DELEGATOR_SERVLET_NAME_ATTRIBUTE;
    }
    
    protected String getHandlerNameProperty() {
        return SERVLET_NAME_PROPERTY;
    }

    protected String getHandlerClassProperty() {
        return SERVLET_CLASS_PROPERTY;
    }

    
}
