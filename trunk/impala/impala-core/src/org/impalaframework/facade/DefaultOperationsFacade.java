/*
 * Copyright 2007 the original author or authors.
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

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSource;
import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.operation.AddModuleOperation;
import org.impalaframework.module.operation.CloseRootModuleOperation;
import org.impalaframework.module.operation.IncrementalUpdateRootModuleOperation;
import org.impalaframework.module.operation.ReloadNamedModuleOperation;
import org.impalaframework.module.operation.ReloadNewNamedModuleOperation;
import org.impalaframework.module.operation.ReloadRootModuleOperation;
import org.impalaframework.module.operation.RemoveModuleOperation;
import org.impalaframework.module.operation.UpdateRootModuleOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DefaultOperationsFacade implements InternalOperationsFacade {

	final Logger logger = LoggerFactory.getLogger(DefaultOperationsFacade.class);

	private ModuleStateHolder moduleStateHolder = null;

	private ModuleManagementSource factory;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	public DefaultOperationsFacade(boolean reloadableParent) {
		super();
		init(reloadableParent);
	}

	protected void init(boolean reloadableParent) {
		String[] locations = null;

		if (reloadableParent) {
			locations = new String[] { "META-INF/impala-bootstrap.xml" };
		}
		else {
			locations = new String[] { "META-INF/impala-bootstrap.xml", "META-INF/impala-interactive-bootstrap.xml" };
		}

		factory = new BeanFactoryModuleManagementSource(new ClassPathXmlApplicationContext(locations));
		moduleStateHolder = factory.getModuleStateHolder();
	}

	public void init(ModuleDefinitionSource source) {
		ReloadRootModuleOperation operation = new IncrementalUpdateRootModuleOperation(factory, source);
		operation.execute();
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */

	public boolean reload(String plugin) {
		ReloadNamedModuleOperation operation = new ReloadNamedModuleOperation(factory, plugin);
		return operation.execute();
	}

	public boolean reload(ModuleDefinitionSource source, String moduleName) {
		ReloadNewNamedModuleOperation operation = new ReloadNewNamedModuleOperation(factory, moduleName, source);
		return operation.execute();
	}

	public String reloadLike(ModuleDefinitionSource source, String moduleName) {
		String like = findLike(source, moduleName);
		if (like != null) {
			reload(source, moduleName);
		}
		return like;
	}

	public String reloadLike(String moduleName) {
		String like = findLike(getModuleStateHolder(), moduleName);
		if (like != null) {
			reload(getModuleStateHolder(), moduleName);
		}
		return like;
	}

	public void reloadAll() {
		RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
		new CloseRootModuleOperation(factory).execute();
		new UpdateRootModuleOperation(factory, new ConstructedModuleDefinitionSource(rootModuleDefinition)).execute();
	}

	public void unloadParent() {
		new CloseRootModuleOperation(factory).execute();
	}

	public boolean remove(String moduleName) {
		return new RemoveModuleOperation(factory, moduleName).execute();
	}

	public void addPlugin(final ModuleDefinition moduleDefinition) {
		new AddModuleOperation(factory, moduleDefinition).execute();
	}

	/* **************************** getters ************************** */

	public boolean hasModule(String moduleName) {
		RootModuleDefinition rootModuleDefinition = getModuleStateHolder().getRootModuleDefinition();
		return (rootModuleDefinition.findChildDefinition(moduleName, true) != null);
	}

	public String findLike(ModuleDefinitionSource source, String moduleName) {
		RootModuleDefinition rootModuleDefinition = source.getModuleDefinition();
		ModuleDefinition definition = rootModuleDefinition.findChildDefinition(moduleName, false);
		if (definition != null) {
			return definition.getName();
		}
		return null;
	}

	public ApplicationContext getRootContext() {
		ConfigurableApplicationContext context = internalGet();
		if (context == null) {
			throw new NoServiceException("No root application has been loaded");
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T getBean(String beanName, Class<T> t) {
		ApplicationContext context = getRootContext();
		return (T) context.getBean(beanName);
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T getPluginBean(String moduleName, String beanName, Class<T> t) {
		ApplicationContext context = getModuleStateHolder().getModule(moduleName);
		if (context == null) {
			throw new NoServiceException("No application context could be found for plugin " + moduleName);
		}
		return (T) context.getBean(beanName);
	}
	
	public RootModuleDefinition getRootModuleDefinition() {
		return getModuleStateHolder().getRootModuleDefinition();
	}	
	
	/* ******************* InternalOperationsFacade methods ************************** */
	
	public ApplicationContext getModule(String moduleName) {
		return getModuleStateHolder().getModule(moduleName);
	}

	/* **************************** private methods ************************** */
	
	protected ModuleStateHolder getModuleStateHolder() {
		return moduleStateHolder;
	}

	/* **************************** private methods ************************** */

	private ConfigurableApplicationContext internalGet() {
		ModuleStateHolder stateHolder = getModuleStateHolder();
		return stateHolder.getParentContext();
	}

}
