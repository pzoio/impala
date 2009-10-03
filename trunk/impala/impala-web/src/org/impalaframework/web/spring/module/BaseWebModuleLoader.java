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

package org.impalaframework.web.spring.module;

import javax.servlet.ServletContext;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.spring.module.loader.BaseSpringModuleLoader;
import org.impalaframework.spring.module.loader.ModuleLoaderUtils;
import org.impalaframework.web.servlet.wrapper.ServletContextWrapper;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Base implementation of {@link ModuleLoader} which extends {@link BaseSpringModuleLoader}
 * providing additional functionality specific to the web environment.
 * Specifically, it uses the {@link ServletContextWrapper} if present, to provide 
 * a wrapped {@link ServletContext} instance as a proxy to the real {@link ServletContext} 
 * provided via the {@link ServletContextAware#setServletContext(ServletContext)} method.
 * Also, it creates an instance of {@link GenericWebApplicationContext} to use as the
 * {@link ConfigurableApplicationContext} instance.
 * 
 * @author Phil Zoio
 */
public class BaseWebModuleLoader extends BaseSpringModuleLoader implements ServletContextAware {

    private ServletContext servletContext;
    
    private ServletContextWrapper servletContextWrapper;

    public BaseWebModuleLoader() {
    }

    public BaseWebModuleLoader(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext cannot be null");
        this.servletContext = servletContext;
    }

    public final GenericWebApplicationContext newApplicationContext(ApplicationContext parent,
            ModuleDefinition moduleDefinition, ClassLoader classLoader) {
        
        ServletContext wrappedServletContext = servletContext;
        
        if (servletContextWrapper != null) {
            wrappedServletContext = servletContextWrapper.wrapServletContext(servletContext, moduleDefinition, classLoader);
        }
        
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setBeanClassLoader(classLoader);

        final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
        context.setParent(parent);
        context.setServletContext(wrappedServletContext);
        context.setClassLoader(classLoader);
        context.setDisplayName(ModuleLoaderUtils.getDisplayName(moduleDefinition, context));
        
        configureBeanFactoryAndApplicationContext(moduleDefinition,
                beanFactory, context);
        
        return context;
    }
    
    
    @Override
    public void handleRefresh(ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        
        try {
            ImpalaServletUtils.publishRootModuleContext(servletContext, moduleDefinition.getName(), context);
            doHandleRefresh(context, moduleDefinition);
        }
        catch (RuntimeException e) {
            ImpalaServletUtils.unpublishRootModuleContext(servletContext, moduleDefinition.getName());
            throw e;
        }
        catch (Throwable e) {
            ImpalaServletUtils.unpublishRootModuleContext(servletContext, moduleDefinition.getName());
            throw new ExecutionException(e.getMessage(), e);
        }
    }
    
    @Override
    public void beforeClose(ApplicationContext applicationContext, ModuleDefinition moduleDefinition) {
        ImpalaServletUtils.unpublishRootModuleContext(servletContext, moduleDefinition.getName());
    }

    protected void doHandleRefresh(ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        super.handleRefresh(context, moduleDefinition);
    }
    
    
    /**
     * Hook for subclasses to perform additional configuration on application context
     */
    protected void configureBeanFactoryAndApplicationContext(
            ModuleDefinition moduleDefinition,
            final DefaultListableBeanFactory beanFactory,
            final GenericWebApplicationContext context) {
    }
    
    protected ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setServletContextWrapper(ServletContextWrapper servletContextWrapper) {
        this.servletContextWrapper = servletContextWrapper;
    }
    
}
