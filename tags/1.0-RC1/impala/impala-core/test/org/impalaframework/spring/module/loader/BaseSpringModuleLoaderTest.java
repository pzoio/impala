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

package org.impalaframework.spring.module.loader;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.loader.ModuleTestUtils;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.impalaframework.spring.module.loader.ApplicationModuleLoader;
import org.impalaframework.spring.module.loader.BaseSpringModuleLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class BaseSpringModuleLoaderTest extends TestCase {
    
    public void testNewBeanDefinitionReader() throws Exception {
        
        BaseSpringModuleLoader loader = new ApplicationModuleLoader();
        loader.setModuleLocationResolver(new StandaloneModuleLocationResolver());
        GenericApplicationContext context = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(context, new SimpleModuleDefinition("pluginName"));
        assertSame(context.getBeanFactory(), reader.getBeanFactory());
    }

    public void testNewApplicationContext() throws Exception {
        BaseSpringModuleLoader loader = new BaseSpringModuleLoader() {

            public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
                return null;
            }

            public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
                return null;
            }
        };
        
        GenericApplicationContext parentContext = new GenericApplicationContext();
        SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition("project1", "context.xml");
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        GenericApplicationContext context = loader.newApplicationContext(parentContext, rootDefinition, classLoader);

        ModuleTestUtils.checkHasPostProcessor(false, context, ModuleDefinitionPostProcessor.class);
    }
}
