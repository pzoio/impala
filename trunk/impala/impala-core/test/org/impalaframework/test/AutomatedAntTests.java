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

package org.impalaframework.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.bean.NamedFactoryBeanTest;
import org.impalaframework.bean.OptionalPropertiesFactoryBeanTest;
import org.impalaframework.bean.SystemPropertiesFactoryBeanTest;
import org.impalaframework.bean.SystemPropertyFactoryBeanTest;
import org.impalaframework.classloader.ModuleClassLoaderFactoryTest;
import org.impalaframework.classloader.ParentClassLoaderFactoryTest;
import org.impalaframework.config.IntPropertyValueTest;
import org.impalaframework.config.StringPropertyValueTest;
import org.impalaframework.config.ExternalDynamicPropertySourceTest;
import org.impalaframework.config.StaticPropertiesPropertySourceTest;
import org.impalaframework.facade.StandaloneOperationsFacadeTest;
import org.impalaframework.file.handler.DefaultClassFilterTest;
import org.impalaframework.file.monitor.FileMonitorImplTest;
import org.impalaframework.module.beanset.BeanSetMapReaderTest;
import org.impalaframework.module.beanset.BeanSetPropertiesReaderTest;
import org.impalaframework.module.beanset.ImportingBeanSetTest;
import org.impalaframework.module.beanset.SimpleBeansetAwareModuleDefinitionTest;
import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSourceTest;
import org.impalaframework.module.bootstrap.BootstrapContextTest;
import org.impalaframework.module.bootstrap.ModuleLoaderRegistryFactoryBeanTest;
import org.impalaframework.module.builder.IncrementalModuleDefinitionSourceTest;
import org.impalaframework.module.builder.InternalModuleBuilderTest;
import org.impalaframework.module.builder.InternalModuleDefinitionSourceExtraTest;
import org.impalaframework.module.builder.InternalModuleDefinitionSourceTest;
import org.impalaframework.module.builder.InternalXmlModuleDefinitionSourceTest;
import org.impalaframework.module.builder.SingleStringModuleDefinitionSourceTest;
import org.impalaframework.module.builder.XmlModuleDefinitionDocumentLoaderTest;
import org.impalaframework.module.builder.XmlModuleDefinitionSourceTest;
import org.impalaframework.module.definition.ChildModuleContainerTest;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSourceTest;
import org.impalaframework.module.definition.ModuleDefinitionToStringTest;
import org.impalaframework.module.definition.ModuleDefinitionUtilsTest;
import org.impalaframework.module.definition.RootModuleDefinitionUtilsTest;
import org.impalaframework.module.definition.SimpleModuleDefinitionTest;
import org.impalaframework.module.definition.SimpleRootModuleDefinitionTest;
import org.impalaframework.module.definition.SimpleSpringContextTest;
import org.impalaframework.module.holder.ModuleStateChangeNotifierTest;
import org.impalaframework.module.holder.ModuleStateHolderMockTest;
import org.impalaframework.module.holder.ModuleStateHolderTest;
import org.impalaframework.module.loader.ApplicationModuleLoaderTest;
import org.impalaframework.module.loader.BaseModuleLoaderTest;
import org.impalaframework.module.loader.ManualReloadingRootModuleLoaderTest;
import org.impalaframework.module.loader.ModuleLoaderRegistryTest;
import org.impalaframework.module.loader.ModuleUtilsTest;
import org.impalaframework.module.loader.RootModuleLoaderTest;
import org.impalaframework.module.modification.ModificationExtractorRegistryTest;
import org.impalaframework.module.modification.ModificationExtractorTest;
import org.impalaframework.module.modification.StickyModificationExtractorTest;
import org.impalaframework.module.monitor.BaseModuleChangeListenerTest;
import org.impalaframework.module.monitor.ScheduledModuleChangeMonitorBeanTest;
import org.impalaframework.module.monitor.ScheduledModuleChangeMonitorTest;
import org.impalaframework.module.operation.AddModuleOperationTest;
import org.impalaframework.module.operation.CloseRootModuleOperationTest;
import org.impalaframework.module.operation.ReloadNamedModuleLikeOperationTest;
import org.impalaframework.module.operation.ReloadNamedModuleOperationTest;
import org.impalaframework.module.operation.RemoveModuleOperationTest;
import org.impalaframework.module.operation.SimpleModuleOperationRegistryTest;
import org.impalaframework.module.operation.UpdateRootModuleOperationTest;
import org.impalaframework.module.resource.ApplicationModuleLocationsResourceLoaderTest;
import org.impalaframework.module.transition.AddLocationsTransitionProcessorTest;
import org.impalaframework.module.transition.ReloadTransitionProcessorTest;
import org.impalaframework.module.transition.TransitionProcessorRegistryTest;
import org.impalaframework.module.type.ApplicationWithBeansetsModuleTypeReaderTest;
import org.impalaframework.module.type.RootModuleTypeReaderTest;
import org.impalaframework.module.type.TypeReaderRegistryFactoryBeanTest;
import org.impalaframework.resolver.AbstractModuleLocationResolverTest;
import org.impalaframework.resolver.SimpleJarModuleLocationResolverTest;
import org.impalaframework.resolver.SimpleModuleLocationResolverTest;
import org.impalaframework.resolver.StandaloneModuleLocationResolverTest;
import org.impalaframework.service.registry.ServiceArrayRegistryExporterTest;
import org.impalaframework.service.registry.ServiceRegistryExporterTest;
import org.impalaframework.service.registry.ServiceRegistryImplTest;
import org.impalaframework.service.registry.ServiceRegistryPostProcessorTest;
import org.impalaframework.service.registry.contribution.ContributionMapTest;
import org.impalaframework.spring.MissingBeanTest;
import org.impalaframework.spring.SystemPropertyBasedPlaceholderConfigurerTest;
import org.impalaframework.spring.module.AutoRegisteringModuleContributionExporterTest;
import org.impalaframework.spring.module.ContributionEndpointInterceptorTest;
import org.impalaframework.spring.module.ContributionProxyFactoryBeanTest;
import org.impalaframework.spring.module.ModuleContributionExportersTest;
import org.impalaframework.spring.module.ModuleContributionPostProcessorTest;
import org.impalaframework.spring.module.ModuleContributionUtilsTest;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessorTest;
import org.impalaframework.spring.module.ParentWithChildContextTest;
import org.impalaframework.spring.module.SimpleContributionEndpointTargetSourceTest;
import org.impalaframework.spring.module.SimpleParentContextTest;
import org.impalaframework.spring.resource.ClassPathResourceLoaderTest;
import org.impalaframework.spring.resource.CompositeResourceLoaderTest;
import org.impalaframework.spring.resource.DirectoryResourceTest;
import org.impalaframework.util.FileUtilsTest;
import org.impalaframework.util.InstantiationUtilsTest;
import org.impalaframework.util.MemoryUtilsTest;
import org.impalaframework.util.ObjectMapUtilsTest;
import org.impalaframework.util.PathUtilsTest;
import org.impalaframework.util.PropertyUtilsTest;
import org.impalaframework.util.ReflectionUtilsTest;
import org.impalaframework.util.ResourceUtilsTest;
import org.impalaframework.util.URLUtilsTest;

public class AutomatedAntTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AbstractModuleLocationResolverTest.class);
		suite.addTestSuite(AddLocationsTransitionProcessorTest.class);
		suite.addTestSuite(AddModuleOperationTest.class);
		suite.addTestSuite(ApplicationModuleLoaderTest.class);
		suite.addTestSuite(ApplicationModuleLocationsResourceLoaderTest.class);
		suite.addTestSuite(ApplicationModuleLoaderTest.class);
		suite.addTestSuite(ApplicationWithBeansetsModuleTypeReaderTest.class);
		suite.addTestSuite(AutoRegisteringModuleContributionExporterTest.class);
		suite.addTestSuite(BaseModuleLoaderTest.class);	
		suite.addTestSuite(BaseModuleChangeListenerTest.class);	
		suite.addTestSuite(BeanFactoryModuleManagementSourceTest.class);
		suite.addTestSuite(BeanSetMapReaderTest.class);	
		suite.addTestSuite(BeanSetPropertiesReaderTest.class);
		suite.addTestSuite(BootstrapContextTest.class);
		suite.addTestSuite(ChildModuleContainerTest.class);
		suite.addTestSuite(ClassPathResourceLoaderTest.class);	
		suite.addTestSuite(CloseRootModuleOperationTest.class);
		suite.addTestSuite(CompositeResourceLoaderTest.class);
		suite.addTestSuite(ConstructedModuleDefinitionSourceTest.class);
		suite.addTestSuite(ContributionEndpointInterceptorTest.class);
		suite.addTestSuite(ContributionProxyFactoryBeanTest.class);
		suite.addTestSuite(ContributionMapTest.class);
		suite.addTestSuite(DefaultClassFilterTest.class);
		suite.addTestSuite(DirectoryResourceTest.class);
		suite.addTestSuite(ExternalDynamicPropertySourceTest.class);
		suite.addTestSuite(FileMonitorImplTest.class);
		suite.addTestSuite(FileUtilsTest.class);
		suite.addTestSuite(ImportingBeanSetTest.class);
		suite.addTestSuite(IntPropertyValueTest.class);
		suite.addTestSuite(IncrementalModuleDefinitionSourceTest.class);
		suite.addTestSuite(InternalModuleBuilderTest.class);
		suite.addTestSuite(InternalModuleDefinitionSourceTest.class);
		suite.addTestSuite(InternalModuleDefinitionSourceExtraTest.class);
		suite.addTestSuite(InternalXmlModuleDefinitionSourceTest.class);
		suite.addTestSuite(InstantiationUtilsTest.class);
		suite.addTestSuite(ManualReloadingRootModuleLoaderTest.class);
		suite.addTestSuite(MemoryUtilsTest.class);
		suite.addTestSuite(ModificationExtractorTest.class);
		suite.addTestSuite(ModificationExtractorRegistryTest.class);
		suite.addTestSuite(ModuleClassLoaderFactoryTest.class);
		suite.addTestSuite(ModuleContributionExportersTest.class);
		suite.addTestSuite(ModuleContributionUtilsTest.class);
		suite.addTestSuite(ModuleContributionPostProcessorTest.class);
		suite.addTestSuite(ModuleDefinitionPostProcessorTest.class);
		suite.addTestSuite(ModuleDefinitionToStringTest.class);
		suite.addTestSuite(ModuleLoaderRegistryTest.class);
		suite.addTestSuite(ModuleLoaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(ModuleDefinitionToStringTest.class);
		suite.addTestSuite(ModuleDefinitionUtilsTest.class);
		suite.addTestSuite(ModuleStateChangeNotifierTest.class);
		suite.addTestSuite(ModuleStateHolderTest.class);
		suite.addTestSuite(ModuleStateHolderMockTest.class);
		suite.addTestSuite(ModuleUtilsTest.class);
		suite.addTestSuite(MissingBeanTest.class);
		suite.addTestSuite(NamedFactoryBeanTest.class);
		suite.addTestSuite(ObjectMapUtilsTest.class);
		suite.addTestSuite(OptionalPropertiesFactoryBeanTest.class);
		suite.addTestSuite(ParentWithChildContextTest.class);
		suite.addTestSuite(ParentClassLoaderFactoryTest.class);
		suite.addTestSuite(PathUtilsTest.class);
		suite.addTestSuite(PropertyUtilsTest.class);
		suite.addTestSuite(ReflectionUtilsTest.class);
		suite.addTestSuite(ReloadNamedModuleOperationTest.class);
		suite.addTestSuite(ReloadNamedModuleLikeOperationTest.class);
		suite.addTestSuite(ReloadTransitionProcessorTest.class);
		suite.addTestSuite(RemoveModuleOperationTest.class);
		suite.addTestSuite(ResourceUtilsTest.class);
		suite.addTestSuite(RootModuleLoaderTest.class);
		suite.addTestSuite(RootModuleDefinitionUtilsTest.class);
		suite.addTestSuite(RootModuleTypeReaderTest.class);
		suite.addTestSuite(ScheduledModuleChangeMonitorBeanTest.class);
		suite.addTestSuite(ScheduledModuleChangeMonitorTest.class);
		suite.addTestSuite(ServiceArrayRegistryExporterTest.class);
		suite.addTestSuite(ServiceRegistryImplTest.class);
		suite.addTestSuite(ServiceRegistryExporterTest.class);
		suite.addTestSuite(ServiceRegistryPostProcessorTest.class);
		suite.addTestSuite(SimpleBeansetAwareModuleDefinitionTest.class);
		suite.addTestSuite(SimpleContributionEndpointTargetSourceTest.class);
		suite.addTestSuite(SimpleModuleDefinitionTest.class);
		suite.addTestSuite(SimpleParentContextTest.class);
		suite.addTestSuite(SimpleRootModuleDefinitionTest.class);
		suite.addTestSuite(SimpleSpringContextTest.class);
		suite.addTestSuite(SimpleJarModuleLocationResolverTest.class);
		suite.addTestSuite(SimpleModuleLocationResolverTest.class);
		suite.addTestSuite(SimpleModuleOperationRegistryTest.class);
		suite.addTestSuite(SingleStringModuleDefinitionSourceTest.class);
		suite.addTestSuite(StandaloneModuleLocationResolverTest.class);
		suite.addTestSuite(StandaloneOperationsFacadeTest.class);
		suite.addTestSuite(StaticPropertiesPropertySourceTest.class);
		suite.addTestSuite(StickyModificationExtractorTest.class);
		suite.addTestSuite(StringPropertyValueTest.class);
		suite.addTestSuite(SystemPropertyBasedPlaceholderConfigurerTest.class);
		suite.addTestSuite(SystemPropertyFactoryBeanTest.class);
		suite.addTestSuite(SystemPropertiesFactoryBeanTest.class);
		suite.addTestSuite(TransitionProcessorRegistryTest.class);
		suite.addTestSuite(TypeReaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(UpdateRootModuleOperationTest.class);
		suite.addTestSuite(URLUtilsTest.class);
		suite.addTestSuite(XmlModuleDefinitionDocumentLoaderTest.class);
		suite.addTestSuite(XmlModuleDefinitionSourceTest.class);

		return suite;
	}
}
