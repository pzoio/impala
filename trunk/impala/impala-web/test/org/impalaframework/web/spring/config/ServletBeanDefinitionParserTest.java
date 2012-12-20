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

package org.impalaframework.web.spring.config;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import junit.framework.TestCase;

import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.util.ReflectionUtils;
import org.impalaframework.web.facade.AttributeServletContext;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServlet;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServletFactoryBean;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.impalaframework.web.spring.servlet.InternalModuleServlet;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HttpServletBean;

public class ServletBeanDefinitionParserTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MyServlet1.reset();
    }

    public void testDefault() throws Exception {
        
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/defaultservletcontext.xml"));
        
        context.refresh();
        
        Map<String,ServletFactoryBean> factoryBeans = context.getBeansOfType(ServletFactoryBean.class);
        assertEquals(1, factoryBeans.size());
        ServletFactoryBean firstValue = (ServletFactoryBean) ObjectMapUtils.getFirstValue(factoryBeans);
        assertTrue(firstValue.getObject() instanceof InternalModuleServlet);
    }
    
    public void testDispatcherServlet() throws Exception {
        
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/dispatcherservletcontext.xml"));
        
        context.refresh();
        
        Map<String,ServletFactoryBean> factoryBeans = context.getBeansOfType(ServletFactoryBean.class);
        assertEquals(1, factoryBeans.size());
        ServletFactoryBean firstValue = (ServletFactoryBean) ObjectMapUtils.getFirstValue(factoryBeans);
        final Object frameworkServlet = firstValue.getObject();
        assertTrue(frameworkServlet instanceof InternalFrameworkIntegrationServlet);
        
        InternalFrameworkIntegrationServlet integrationServlet = (InternalFrameworkIntegrationServlet) frameworkServlet;
        final DispatcherServlet dispatcherServlet = ReflectionUtils.getFieldValue(integrationServlet, "delegateServlet", DispatcherServlet.class);
        
        assertNotNull(dispatcherServlet);
    }
    
    public void testServlet() throws Exception {
        
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/servletbeancontext.xml"));
        
        context.refresh();
        
        assertEquals("injectedValue", MyServlet1.getMyAttribute());
        assertEquals("initValue", MyServlet2.getConfigParam());
        
        Map<String,ServletFactoryBean> factoryBeans = context.getBeansOfType(ServletFactoryBean.class);
        assertEquals(3, factoryBeans.size());
        
        Map<String,HttpServlet> s = context.getBeansOfType(HttpServlet.class);
        assertEquals(1, s.size());
        
        Map<String,? extends ServletFactoryBean> beans = context.getBeansOfType(InternalFrameworkIntegrationServletFactoryBean.class);
        assertEquals(1, beans.size());
        
        InternalFrameworkIntegrationServletFactoryBean firstValue = (InternalFrameworkIntegrationServletFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("delegator", firstValue.getServletName());

        beans = context.getBeansOfType(ExtendedServletFactoryBean.class);
        assertEquals(1, beans.size());
        
        ExtendedServletFactoryBean extendedFactoryBean = (ExtendedServletFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("extraValue", extendedFactoryBean.getExtraAttribute());
        
        MyServlet2 myServlet = (MyServlet2) extendedFactoryBean.getObject();
        assertEquals("myServlet2", myServlet.getServletName());
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
