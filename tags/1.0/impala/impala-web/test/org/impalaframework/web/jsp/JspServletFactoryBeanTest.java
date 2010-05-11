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

package org.impalaframework.web.jsp;

import static org.easymock.classextension.EasyMock.createMock;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

import org.apache.jasper.servlet.JspServlet;
import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.holder.graph.GraphClassLoaderFactory;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;
import org.impalaframework.module.source.InternalModuleDefinitionSource;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServlet;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class JspServletFactoryBeanTest extends TestCase {

    private JspServletFactoryBean factoryBean;
    private AttributeServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factoryBean = new JspServletFactoryBean();
        factoryBean.setServletName("jspServlet");
        servletContext = new AttributeServletContext();
        factoryBean.setServletContext(servletContext);
        factoryBean.setApplicationContext(new GenericWebApplicationContext());
        Thread.currentThread().setContextClassLoader(null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Thread.currentThread().setContextClassLoader(null);
    }

    public void testWithURLClassLoader() throws Exception {
        final URLClassLoader urlClassLoader = newUrlClassLoader();
        Thread.currentThread().setContextClassLoader(
                urlClassLoader);

        doTest();
    }

    public void testWithWiredInClassLoader() throws Exception {
        factoryBean.setBeanClassLoader(new URLClassLoader(new URL[] { new URL("file:myfile") }));
    }

    public void testWithNoClassLoader() throws Exception {
        Thread.currentThread().setContextClassLoader(null);

        try {
            doTest();
        }
        catch (ConfigurationException e) {
            assertEquals(
                    "Cannot support JSP as the application is unable to create a JSPClassLoader instance for the current module",
                    e.getMessage());
        }
    }
    
    public void testWithGraphClassLoader() throws Exception {
        GraphClassLoader rootClassLoader = newGraphClassLoader();

        factoryBean.setBeanClassLoader(rootClassLoader);
        
        doTest();
        
        final Object attribute = servletContext.getAttribute(JspConstants.JSP_SERVLET);
        assertTrue(attribute instanceof InternalFrameworkIntegrationServlet);
    }
    
    public void testMaybeCreateUrlClassLoader() throws Exception {
        
        final GraphClassLoader gcl = newGraphClassLoader();
        factoryBean.setBeanClassLoader(gcl);
        JasperClassLoader urlClassLoader = factoryBean.maybeCreateURLClassLoader();
        assertTrue(urlClassLoader.getURLs().length > 0);
        
        final URLClassLoader ucl = newUrlClassLoader();
        factoryBean.setBeanClassLoader(ucl);
        urlClassLoader = factoryBean.maybeCreateURLClassLoader();
        assertTrue(urlClassLoader.getURLs().length > 0);
        
        factoryBean.setBeanClassLoader(createMock(ClassLoader.class));
        assertNull(factoryBean.maybeCreateURLClassLoader());
    }

    private URLClassLoader newUrlClassLoader() throws MalformedURLException {
        final URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new URL("file:myfile") });
        return urlClassLoader;
    }

    private GraphClassLoader newGraphClassLoader() {
        TypeReaderRegistry typeReaderRegistry = TypeReaderRegistryFactory.getTypeReaderRegistry();
        ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
        InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"impala-core", "sample-module4", "sample-module6"});

        RootModuleDefinition rootDefinition = source.getModuleDefinition();
        
        DependencyManager dependencyManager = new DependencyManager(rootDefinition);
        GraphClassLoaderFactory factory = new GraphClassLoaderFactory();
        factory.setModuleLocationResolver(resolver);
        
        GraphClassLoader rootClassLoader = factory.newClassLoader(new GraphClassLoaderRegistry(), dependencyManager, rootDefinition);
        return rootClassLoader;
    }

    private void doTest() throws Exception {
        factoryBean.afterPropertiesSet();
        assertTrue(factoryBean.getObject() instanceof JspServlet);
    }

}
