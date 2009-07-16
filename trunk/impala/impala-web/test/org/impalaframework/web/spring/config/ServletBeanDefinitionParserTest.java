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

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import junit.framework.TestCase;

import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServletFactoryBean;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.HttpServletBean;

public class ServletBeanDefinitionParserTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MyServlet1.reset();
    }

    @SuppressWarnings("unchecked")
    public void testDefault() throws Exception {
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/servletbeancontext.xml"));
        
        context.refresh();
        
        assertEquals("injectedValue", MyServlet1.getMyAttribute());
        assertEquals("initValue", MyServlet2.getConfigParam());
        
        Map factoryBeans = context.getBeansOfType(ServletFactoryBean.class);
        assertEquals(3, factoryBeans.size());
        
        Map s = context.getBeansOfType(HttpServlet.class);
        assertEquals(1, s.size());
        
        Map beans = context.getBeansOfType(InternalFrameworkIntegrationServletFactoryBean.class);
        assertEquals(1, beans.size());
        
        InternalFrameworkIntegrationServletFactoryBean firstValue = (InternalFrameworkIntegrationServletFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("delegator", firstValue.getServletName());

        beans = context.getBeansOfType(ExtendedServletFactoryBean.class);
        assertEquals(1, beans.size());
        
        ExtendedServletFactoryBean extendedFactoryBean = (ExtendedServletFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("extraValue", extendedFactoryBean.getExtraAttribute());
    }
    
}

class MyServlet1 extends HttpServletBean {
    
    private static String myAttribute;

    private static final long serialVersionUID = 1L;
    
    public void setMyAttribute(String myAttribute) {
        MyServlet1.myAttribute = myAttribute;
    }
    
    public static String getMyAttribute() {
        return myAttribute;
    }
    
    public static void reset() {
        myAttribute = null;
    }
}

class MyServlet2 extends HttpServletBean {
    
    private static String configParam;

    private static final long serialVersionUID = 1L;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        configParam = config.getInitParameter("initParam");
    }
    
    public static String getConfigParam() {
        return configParam;
    }
    
    public static void reset() {
        configParam = null;
    }
}

class ExtendedServletFactoryBean extends ServletFactoryBean {
    
    private String extraAttribute;

    public String getExtraAttribute() {
        return extraAttribute;
    }

    public void setExtraAttribute(String extraAttribute) {
        this.extraAttribute = extraAttribute;
    }
}
