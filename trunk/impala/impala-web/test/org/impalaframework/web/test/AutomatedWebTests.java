/*
 * Copyright 2006-2007 the original author or authors.
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

package org.impalaframework.web.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.web.bootstrap.ServletContextLocationsRetrieverTest;
import org.impalaframework.web.bootstrap.WebContextLocationResolverTest;
import org.impalaframework.web.config.ServletContextPropertySourceTest;
import org.impalaframework.web.integration.CompositeRequestModuleMapperTest;
import org.impalaframework.web.integration.IntegrationFilterConfigTest;
import org.impalaframework.web.integration.ModuleIntegrationUtilsTest;
import org.impalaframework.web.integration.ModuleProxyFilterTest;
import org.impalaframework.web.integration.ModuleProxyServletTest;
import org.impalaframework.web.integration.ModuleProxyUtilsTest;
import org.impalaframework.web.integration.PrefixTreeHolderTest;
import org.impalaframework.web.integration.UrlPrefixRequestModuleMapperTest;
import org.impalaframework.web.listener.ServletContextListenerFactoryBeanTest;
import org.impalaframework.web.module.jmx.WebModuleReloaderTest;
import org.impalaframework.web.module.listener.WebModuleChangeListenerTest;
import org.impalaframework.web.module.listener.WebScheduledModuleChangeMonitorTest;
import org.impalaframework.web.module.monitor.TempFileModuleRuntimeMonitorTest;
import org.impalaframework.web.module.path.ServletPathRequestModuleMapperTest;
import org.impalaframework.web.resolver.ServletContextModuleLocationResolverTest;
import org.impalaframework.web.resource.ServletContextResourceLoaderTest;
import org.impalaframework.web.servlet.invocation.InvocationChainTest;
import org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvokerBuilderTest;
import org.impalaframework.web.servlet.invoker.ReadWriteLockInvokerTest;
import org.impalaframework.web.servlet.invoker.ServletInvokerUtilsTest;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvokerTest;
import org.impalaframework.web.servlet.wrapper.HttpRequestWrapperFactoryTest;
import org.impalaframework.web.servlet.wrapper.MappedWrapperHttpServletRequestTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareServletContextWrapperTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpServletRequestTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpSessionTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperServletContextTest;
import org.impalaframework.web.spring.bean.FrameworkServletContextCreatorTest;
import org.impalaframework.web.spring.bean.SystemPropertyServletContextParamFactoryBeanTest;
import org.impalaframework.web.spring.config.FilterBeanDefinitionParserTest;
import org.impalaframework.web.spring.config.ServletBeanDefinitionParserTest;
import org.impalaframework.web.spring.config.WebMappingBeanDefinitionParserTest;
import org.impalaframework.web.spring.helper.ImpalaServletUtilsTest;
import org.impalaframework.web.spring.helper.WebServletUtilsTest;
import org.impalaframework.web.spring.integration.ExternalFrameworkIntegrationServletTest;
import org.impalaframework.web.spring.integration.FilterFactoryBeanTest;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilterTest;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServletFactoryBeanTest;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServletTest;
import org.impalaframework.web.spring.integration.ModuleUrlPrefixContributorTest;
import org.impalaframework.web.spring.integration.ServletFactoryBeanTest;
import org.impalaframework.web.spring.loader.BaseImpalaContextLoaderTest;
import org.impalaframework.web.spring.loader.ContextLoaderIntegrationTest;
import org.impalaframework.web.spring.loader.ExternalContextLoaderTest;
import org.impalaframework.web.spring.loader.ExternalModuleContextLoaderTest;
import org.impalaframework.web.spring.loader.ImpalaContextLoaderListenerTest;
import org.impalaframework.web.spring.loader.WebPlaceholderDelegatingContextLoaderTest;
import org.impalaframework.web.spring.module.RootWebModuleLoaderTest;
import org.impalaframework.web.spring.module.WebModuleLoaderTest;
import org.impalaframework.web.spring.module.WebPlaceholderModuleDefinitionTest;
import org.impalaframework.web.spring.module.WebRootModuleLoaderTest;
import org.impalaframework.web.spring.servlet.ExternalModuleServletTest;

public class AutomatedWebTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(BaseImpalaContextLoaderTest.class);
        suite.addTestSuite(CompositeRequestModuleMapperTest.class);
        suite.addTestSuite(ContextLoaderIntegrationTest.class);
        suite.addTestSuite(ExternalContextLoaderTest.class);
        suite.addTestSuite(ExternalFrameworkIntegrationServletTest.class);
        suite.addTestSuite(ExternalModuleServletTest.class);
        suite.addTestSuite(ExternalModuleContextLoaderTest.class);
        suite.addTestSuite(FilterBeanDefinitionParserTest.class);
        suite.addTestSuite(FilterFactoryBeanTest.class);
        suite.addTestSuite(FrameworkServletContextCreatorTest.class);
        suite.addTestSuite(HttpRequestWrapperFactoryTest.class);
        suite.addTestSuite(ImpalaContextLoaderListenerTest.class);
        suite.addTestSuite(ImpalaServletUtilsTest.class);
        suite.addTestSuite(IntegrationFilterConfigTest.class);
        suite.addTestSuite(InternalFrameworkIntegrationFilterTest.class);
        suite.addTestSuite(InternalFrameworkIntegrationServletTest.class);
        suite.addTestSuite(InternalFrameworkIntegrationServletFactoryBeanTest.class);
        suite.addTestSuite(InvocationChainTest.class);
        suite.addTestSuite(ModuleHttpServiceInvokerBuilderTest.class);
        suite.addTestSuite(MappedWrapperHttpServletRequestTest.class);
        suite.addTestSuite(ModuleAwareWrapperHttpServletRequestTest.class);
        suite.addTestSuite(ModuleAwareWrapperHttpSessionTest.class);
        suite.addTestSuite(ModuleAwareServletContextWrapperTest.class);
        suite.addTestSuite(ModuleAwareWrapperServletContextTest.class);
        suite.addTestSuite(ModuleIntegrationUtilsTest.class);
        suite.addTestSuite(ModuleProxyFilterTest.class);
        suite.addTestSuite(ModuleProxyServletTest.class);
        suite.addTestSuite(ModuleProxyUtilsTest.class);
        suite.addTestSuite(ModuleUrlPrefixContributorTest.class);
        suite.addTestSuite(PrefixTreeHolderTest.class);
        suite.addTestSuite(ReadWriteLockInvokerTest.class);
        suite.addTestSuite(RootWebModuleLoaderTest.class);
        suite.addTestSuite(ServletBeanDefinitionParserTest.class);
        suite.addTestSuite(ServletContextListenerFactoryBeanTest.class);
        suite.addTestSuite(ServletContextPropertySourceTest.class);
        suite.addTestSuite(ServletContextLocationsRetrieverTest.class);
        suite.addTestSuite(ServletFactoryBeanTest.class);
        suite.addTestSuite(ServletContextModuleLocationResolverTest.class);
        suite.addTestSuite(ServletContextResourceLoaderTest.class);
        suite.addTestSuite(ServletInvokerUtilsTest.class);
        suite.addTestSuite(ServletPathRequestModuleMapperTest.class);
        suite.addTestSuite(SystemPropertyServletContextParamFactoryBeanTest.class);
        suite.addTestSuite(TempFileModuleRuntimeMonitorTest.class);
        suite.addTestSuite(ThreadContextClassLoaderHttpServiceInvokerTest.class);
        suite.addTestSuite(UrlPrefixRequestModuleMapperTest.class);
        suite.addTestSuite(WebContextLocationResolverTest.class);
        suite.addTestSuite(WebMappingBeanDefinitionParserTest.class);
        suite.addTestSuite(WebPlaceholderDelegatingContextLoaderTest.class);
        suite.addTestSuite(WebPlaceholderModuleDefinitionTest.class);
        suite.addTestSuite(WebRootModuleLoaderTest.class);
        suite.addTestSuite(WebModuleChangeListenerTest.class);
        suite.addTestSuite(WebModuleLoaderTest.class);
        suite.addTestSuite(WebModuleReloaderTest.class);
        suite.addTestSuite(WebScheduledModuleChangeMonitorTest.class);
        suite.addTestSuite(WebServletUtilsTest.class);
        return suite;
    }
}