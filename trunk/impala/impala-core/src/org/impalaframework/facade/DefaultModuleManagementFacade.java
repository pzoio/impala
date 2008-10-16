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

package org.impalaframework.facade;

import java.lang.reflect.Method;
import java.util.Map;

import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleStateChangeNotifier;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class DefaultModuleManagementFacade implements BeanFactory, ModuleManagementFacade, ApplicationContextAware,
		InitializingBean {

	private ConfigurableApplicationContext applicationContext;

	private ModuleOperationRegistry moduleOperationRegistry;

	private ApplicationContextLoader applicationContextLoader;

	private ModuleLocationResolver moduleLocationResolver;

	private ModuleLoaderRegistry moduleLoaderRegistry;

	private ModificationExtractorRegistry modificationExtractorRegistry;

	private TransitionProcessorRegistry transitionProcessorRegistry;

	private ModuleStateHolder moduleStateHolder;

	private ModuleStateChangeNotifier moduleStateChangeNotifier;
	
	private Map<String, TypeReader> typeReaders;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(moduleOperationRegistry, "moduleOperationRegistry cannot be null");
		Assert.notNull(applicationContextLoader, "applicationContextLoader cannot be null");
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		Assert.notNull(moduleLoaderRegistry, "moduleLoaderRegistry cannot be null");
		Assert.notNull(modificationExtractorRegistry, "modificationExtractorRegistry cannot be null");
		Assert.notNull(transitionProcessorRegistry, "transitionProcessorRegistry cannot be null");
		Assert.notNull(moduleStateHolder, "moduleStateHolder cannot be null");
		Assert.notNull(moduleStateChangeNotifier, "moduleStateChangeNotifier cannot be null");
		Assert.notNull(typeReaders, "typeReaders cannot be null");
	}

	public DefaultModuleManagementFacade() {
		super();
	}

	public ApplicationContextLoader getApplicationContextLoader() {
		return applicationContextLoader;
	}

	public ModuleLocationResolver getModuleLocationResolver() {
		return moduleLocationResolver;
	}

	public ModificationExtractorRegistry getModificationExtractorRegistry() {
		return modificationExtractorRegistry;
	}

	public ModuleOperationRegistry getModuleOperationRegistry() {
		return moduleOperationRegistry;
	}

	public ModuleLoaderRegistry getModuleLoaderRegistry() {
		return moduleLoaderRegistry;
	}

	public TransitionProcessorRegistry getTransitionProcessorRegistry() {
		return transitionProcessorRegistry;
	}

	public ModuleStateHolder getModuleStateHolder() {
		return moduleStateHolder;
	}

	public ModuleStateChangeNotifier getModuleStateChangeNotifier() {
		return moduleStateChangeNotifier;
	}	
	
	public Map<String, TypeReader> getTypeReaders() {
		return typeReaders;
	}

	/* *************** Injection setters ************* */

	public void setApplicationContextLoader(ApplicationContextLoader applicationContextLoader) {
		this.applicationContextLoader = applicationContextLoader;
	}

	public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public void setModificationExtractorRegistry(ModificationExtractorRegistry modificationExtractorRegistry) {
		this.modificationExtractorRegistry = modificationExtractorRegistry;
	}

	public void setModuleOperationRegistry(ModuleOperationRegistry moduleOperationRegistry) {
		this.moduleOperationRegistry = moduleOperationRegistry;
	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setModuleLoaderRegistry(ModuleLoaderRegistry moduleLoaderRegistry) {
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}

	public void setModuleStateChangeNotifier(ModuleStateChangeNotifier moduleStateChangeNotifier) {
		this.moduleStateChangeNotifier = moduleStateChangeNotifier;
	}

	public void setTypeReaders(Map<String, TypeReader> typeReaders) {
		this.typeReaders = typeReaders;
	}

	/* *************** ApplicationContext method implementations ************* */

	public boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public String[] getAliases(String name) {
		return this.applicationContext.getAliases(name);
	}

	public Object getBean(String name) throws BeansException {
		return this.applicationContext.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public Object getBean(String name, Class requiredType) throws BeansException {
		return this.applicationContext.getBean(name, requiredType);
	}

	public Object getBean(String name, Object[] args) throws BeansException {
		Method findMethod = ReflectionUtils.findMethod(ApplicationContext.class, "getBean", new Class[] { String.class,
				Object[].class });
		if (findMethod != null)
			return ReflectionUtils.invokeMethod(findMethod, this.applicationContext, name, args);
		else
			throw new UnsupportedOperationException(
					"Method getBean(String name, Object[] args) not supported. Are you using Spring 2.5.5 or above?");
	}

	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.getType(name);
	}

	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isPrototype(name);
	}

	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isSingleton(name);
	}

	@SuppressWarnings("unchecked")
	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isTypeMatch(name, targetType);
	}

	public void close() {
		this.applicationContext.close();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = ObjectUtils.cast(applicationContext, ConfigurableApplicationContext.class);
	}

}
