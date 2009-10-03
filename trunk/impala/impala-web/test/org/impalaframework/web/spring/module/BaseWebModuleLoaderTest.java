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

import static org.easymock.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import javax.servlet.ServletContext;

import org.impalaframework.exception.RuntimeException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import junit.framework.TestCase;

public class BaseWebModuleLoaderTest extends TestCase {
    private ModuleDefinition moduleDefinition;
    private ConfigurableApplicationContext context;
    private ServletContext servletContext;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        moduleDefinition = new SimpleModuleDefinition("mymodule");
        context = new GenericWebApplicationContext();
    }
    
    public void testHandleRefresh() {
        BaseWebModuleLoader moduleLoader = new BaseWebModuleLoader(){
            @Override
            protected void doHandleRefresh(
                    ConfigurableApplicationContext context,
                    ModuleDefinition moduleDefinition) {
            }
        };
        moduleLoader.setServletContext(servletContext);
        
        servletContext.setAttribute("module_mymodule:org.springframework.web.context.WebApplicationContext.ROOT", context);  
        replay(servletContext);
        
        moduleLoader.handleRefresh(context, moduleDefinition);
        
        verify(servletContext);
    }

    public void testHandleRefreshWithException() {
        BaseWebModuleLoader moduleLoader = new BaseWebModuleLoader(){
            @Override
            protected void doHandleRefresh(
                    ConfigurableApplicationContext context,
                    ModuleDefinition moduleDefinition) {
                throw new RuntimeException();
            }
        };
        moduleLoader.setServletContext(servletContext);
        
        servletContext.setAttribute("module_mymodule:org.springframework.web.context.WebApplicationContext.ROOT", context);  
        servletContext.removeAttribute("module_mymodule:org.springframework.web.context.WebApplicationContext.ROOT");  
        replay(servletContext);
        
        try {
            moduleLoader.handleRefresh(context, moduleDefinition);
            fail();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        
        verify(servletContext);
    }

    public void testBeforeClose() throws Exception {
        BaseWebModuleLoader moduleLoader = new BaseWebModuleLoader();
        moduleLoader.setServletContext(servletContext);

        servletContext.removeAttribute("module_mymodule:org.springframework.web.context.WebApplicationContext.ROOT");  
        replay(servletContext);
        
        moduleLoader.beforeClose(context, moduleDefinition);
        
        verify(servletContext);
    }
    
}
