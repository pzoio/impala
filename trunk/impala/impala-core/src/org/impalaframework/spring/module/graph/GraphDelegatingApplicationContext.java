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

package org.impalaframework.spring.module.graph;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of {@link ApplicationContext} which wraps a single parent {@link ApplicationContext}, but also
 * support delegating of {@link #getBean(String)} and overloaded methods to {@link ApplicationContext}s backing
 * dependent modules.
 * 
 * @author Phil Zoio
 */
public class GraphDelegatingApplicationContext implements ApplicationContext {
	
	private final ApplicationContext parent;
	private final long startupDate;
	private String id = ObjectUtils.identityToString(this);
	private String displayName;
	private final List<ApplicationContext> nonAncestorDependentContexts;
	private boolean parentGetBean;

	public GraphDelegatingApplicationContext(ApplicationContext parent, List<ApplicationContext> nonAncestorDependentContexts, boolean parentGetBean) {
		super();
		Assert.notNull(parent, "parent ApplicationContext cannot be null");
		Assert.notNull(nonAncestorDependentContexts, "nonAncestorDependentContexts ApplicationContext cannot be null");
		this.parent = parent;
		this.startupDate = System.currentTimeMillis();
		this.nonAncestorDependentContexts = nonAncestorDependentContexts;
		this.parentGetBean = parentGetBean;
	}

	/* ***************** Methods with non-trivial separate implementation ***************** */	
	
	public String getDisplayName() {
		return displayName;
	}

	public String getId() {
		return id;
	}
	
	/* ***************** Methods which will also search nonAncestorDependentContexts  ***************** */

	public Object getBean(String name) throws BeansException {
		
		if (parentGetBean && parent.containsBean(name)) {
			return parent.getBean(name);
		}
		
		for (ApplicationContext applicationContext : nonAncestorDependentContexts) {
			
			if (containsBeanDefintion(applicationContext, name)) {
				return applicationContext.getBean(name);
			}
		}
		
		throw new NoSuchBeanDefinitionException(name);
	}
	
	@SuppressWarnings("unchecked")
	public Object getBean(String name, Class requiredType) throws BeansException {
		
		if (parentGetBean && parent.containsBean(name)) {
			return parent.getBean(name, requiredType);
		}
		
		for (ApplicationContext applicationContext : nonAncestorDependentContexts) {
			
			//FIXME add logging
			if (containsBeanDefintion(applicationContext, name)) {
				return applicationContext.getBean(name, requiredType);
			}
		}

		throw new NoSuchBeanDefinitionException(name);
	}

	public Object getBean(String name, Object[] args) throws BeansException {
		
		if (parentGetBean && parent.containsBean(name)) {
			return parent.getBean(name, args);
		}
		
		for (ApplicationContext applicationContext : nonAncestorDependentContexts) {
			
			//FIXME add logging
			if (containsBeanDefintion(applicationContext, name)) {
				return applicationContext.getBean(name, args);
			}
		}

		throw new NoSuchBeanDefinitionException(name);
	}

	private boolean containsBeanDefintion(ApplicationContext applicationContext, String name) {
		boolean containsBeanDefinition = applicationContext.containsBeanDefinition(maybeDeferenceFactoryBean(name));
		return containsBeanDefinition;
	}

	private String maybeDeferenceFactoryBean(String name) {
		return name.startsWith("&") ? name.substring(1): name;
	}
	
	/* ***************** Methods simply delegating to parent ***************** */
	
	public boolean containsBean(String name) {
		return parent.containsBean(name);
	}
	
	public ApplicationContext getParent() {
		return parent;
	}

	public long getStartupDate() {
		return startupDate;
	}

	public boolean containsBeanDefinition(String beanName) {
		return parent.containsBeanDefinition(beanName);
	}

	public int getBeanDefinitionCount() {
		return parent.getBeanDefinitionCount();
	}

	public String[] getBeanDefinitionNames() {
		return parent.getBeanDefinitionNames();
	}

	@SuppressWarnings("unchecked")
	public String[] getBeanNamesForType(Class type) {
		return parent.getBeanNamesForType(type);
	}

	@SuppressWarnings("unchecked")
	public String[] getBeanNamesForType(Class type,	boolean includeNonSingletons, boolean allowEagerInit) {
		return parent.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
	}

	@SuppressWarnings("unchecked")
	public Map getBeansOfType(Class type) throws BeansException {
		return parent.getBeansOfType(type);
	}

	@SuppressWarnings("unchecked")
	public Map getBeansOfType(Class type, boolean includeNonSingletons,	boolean allowEagerInit) throws BeansException {
		return parent.getBeansOfType(type);
	}

	public String[] getAliases(String name) {
		return parent.getAliases(name);
	}

	@SuppressWarnings("unchecked")
	public Class getType(String name) throws NoSuchBeanDefinitionException {
		return parent.getType(name);
	}

	public boolean isPrototype(String name)
			throws NoSuchBeanDefinitionException {
		return parent.isPrototype(name);
	}

	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return parent.isSingleton(name);
	}

	@SuppressWarnings("unchecked")
	public boolean isTypeMatch(String name, Class targetType)
			throws NoSuchBeanDefinitionException {
		return parent.isTypeMatch(name, targetType);
	}

	public boolean containsLocalBean(String name) {
		return parent.containsLocalBean(name);
	}

	public BeanFactory getParentBeanFactory() {
		return parent.getParentBeanFactory();
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale)	throws NoSuchMessageException {
		return parent.getMessage(resolvable, locale);
	}

	public String getMessage(String code, Object[] args, Locale locale)	throws NoSuchMessageException {
		return parent.getMessage(code, args, locale);
	}

	public String getMessage(String code, Object[] args, String defaultMessage,	Locale locale) {
		return parent.getMessage(code, args, defaultMessage, locale);
	}

	public void publishEvent(ApplicationEvent event) {
		parent.publishEvent(event);
	}

	public Resource[] getResources(String locationPattern) throws IOException {
		return parent.getResources(locationPattern);
	}

	public Resource getResource(String location) {
		return parent.getResource(location);
	}	

	public ClassLoader getClassLoader() {
		return parent.getClassLoader();
	}
	
	public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
		return parent.getAutowireCapableBeanFactory();
	}
	
	/* ********************* wired in setters ********************* */

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
