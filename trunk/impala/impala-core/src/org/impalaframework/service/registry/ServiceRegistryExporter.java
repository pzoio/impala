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

package org.impalaframework.service.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.service.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Bean which can be used to export beans into the service registry. This class
 * gives full control over how the bean is added to the service registry. Use
 * this class when you want to export individual beans either with attributes or
 * tags. You can also set the name which clients can use to find the 
 * bean in the registry.
 * 
 * An instance of this bean is only able to add a single entry to the service registry, 
 * which it does when {@link InitializingBean#afterPropertiesSet()} is called.
 * It is also responsible for removing the bean from the service registry, which it does 
 * when {@link DisposableBean#destroy()} is called.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryExporter implements ServiceRegistryAware, BeanFactoryAware, InitializingBean, DisposableBean, ModuleDefinitionAware, BeanClassLoaderAware {

	private String beanName;
	
	private String exportName;
	
	private List<String> tags;
	
	private Map<String, String> attributes;
	
	private ModuleDefinition moduleDefinition;
	
	private ServiceRegistry serviceRegistry;

	private BeanFactory beanFactory;
	
	private Object service;

	private ClassLoader beanClassLoader;
	
	/**
	 * {@link InitializingBean} implementation. Retrieves bean by name from bean factory. Then exports it using the 
	 * supplied export name, attributes and tags, if these are provided. By default, simply uses the bean name.
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(beanName, "beanName cannot be null");
		Assert.notNull(serviceRegistry);
		Assert.notNull(beanFactory);
		Assert.notNull(moduleDefinition);
		
		if (exportName == null) {
			exportName = beanName;
		}
		
		service = beanFactory.getBean(beanName);
		serviceRegistry.addService(exportName, moduleDefinition.getName(), service, tags, attributes, beanClassLoader);
	}

	/**
	 * {@link ServiceRegistryAware} implementation. Simply sets {@link ServiceRegistry}
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * {@link BeanFactoryAware} implementation. Simply sets {@link BeanFactory}
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}	

	/**
	 * {@link ModuleDefinitionAware} implementation. Simply sets {@link ModuleDefinition}
	 */
	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}
	
	/**
	 * {@link DisposableBean} implementation. Removes the entry previously added to the service registry
	 */
	public void destroy() throws Exception {
		serviceRegistry.remove(service);
	}

	/**
	 * {@link BeanClassLoaderAware} implementation. Provided so that service registry reference is class loader aware.
	 */
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}
	
	/* ********************* properties wired in, probably in context definition XML ************** */

	/**
	 * Sets the name of the bean to be exported
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * Sets the name from which the bean will be accessible in the service registry
	 */
	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	/**
	 * Sets tags which can be used to annotate a service registry entry, as a {@link List}
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Sets tags which can be used to annotate a service registry entry, as an array
	 */
	public void setTagsArray(String[] tags) {
		Assert.notNull(tags);
		this.tags = new ArrayList<String>(Arrays.asList(tags));
	}

	/**
	 * Sets attributes for the service registry entry
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
