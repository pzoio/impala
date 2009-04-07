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

package org.impalaframework.module.holder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionProcessor;
import org.impalaframework.module.spi.TransitionSet;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class DefaultModuleStateHolder implements ModuleStateHolder {

	private static Log logger = LogFactory.getLog(DefaultModuleStateHolder.class);
	
	private RootModuleDefinition rootModuleDefinition;

	private TransitionProcessorRegistry transitionProcessorRegistry;
	
	private ModuleStateChangeNotifier moduleStateChangeNotifier;

	private ReentrantLock lock = new ReentrantLock();
	
	private Map<String, RuntimeModule> runtimeModules = new HashMap<String, RuntimeModule>();

	public DefaultModuleStateHolder() {
		super();
	}

	public void processTransitions(TransitionSet transitions) {
		
		try {
			Assert.notNull(transitionProcessorRegistry, TransitionProcessorRegistry.class.getSimpleName() + " cannot be null");

			Collection<? extends ModuleStateChange> changes = transitions.getModuleTransitions();

			for (ModuleStateChange change : changes) {
				String transition = change.getTransition();
				ModuleDefinition currentModuleDefinition = change.getModuleDefinition();

				TransitionProcessor transitionProcessor = transitionProcessorRegistry.getTransitionProcessor(transition);
				transitionProcessor.process(transitions.getNewRootModuleDefinition(), currentModuleDefinition);
			
				if (moduleStateChangeNotifier != null) {
					moduleStateChangeNotifier.notify(this, change);
				}
			}
		} finally {
			rootModuleDefinition = transitions.getNewRootModuleDefinition();
		}
	}

	public RuntimeModule getRootModule() {
		if (rootModuleDefinition == null) return null;
		return runtimeModules.get(rootModuleDefinition.getName());
	}

	public RuntimeModule getModule(String moduleName) {
		return runtimeModules.get(moduleName);
	}

	public RootModuleDefinition getRootModuleDefinition() {
		return rootModuleDefinition;
	}

	public RootModuleDefinition cloneRootModuleDefinition() {
		RootModuleDefinition newDefinition = ModuleDefinitionUtils.cloneAndUnfreeze(rootModuleDefinition);
		return newDefinition;
	}

	public boolean hasModule(String moduleName) {
		return (rootModuleDefinition.findChildDefinition(moduleName, true) != null);
	}

	public boolean hasRootModuleDefinition() {
		return getRootModuleDefinition() != null;
	}

	public Map<String, RuntimeModule> getRuntimeModules() {
		return Collections.unmodifiableMap(runtimeModules);
	}
	
	public void putModule(String name, RuntimeModule context) {
		runtimeModules.put(name, context);
	}

	public RuntimeModule removeModule(String moduleName) {
		return runtimeModules.remove(moduleName);
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return getRootModuleDefinition();
	}	
	
	public void lock() {
		this.lock.lock();
	}
	
	public void unlock() {
		this.lock.unlock();
	}
	
	public boolean isUnavailable() {
		//FIXME check the semantics of this - want to robustify operations on service registry and
		//also on proxies
		if (this.lock.isLocked() && !this.lock.isHeldByCurrentThread()) {
			
			if (logger.isDebugEnabled()) {
				logger.debug("Module is unavailable with hold count of " + lock.getHoldCount() + " but not held by current thread");
			}
			return false;
		}
		return true;
	}

	public boolean hasLock() {
		return this.lock.isHeldByCurrentThread();
	}

	/* ************************* protected methods ************************* */

	protected void setRootModuleDefinition(RootModuleDefinition rootModuleDefinition) {
		this.rootModuleDefinition = rootModuleDefinition;
	}

	/* ******************** injected setters ******************** */
	
	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}

	public void setModuleStateChangeNotifier(ModuleStateChangeNotifier moduleStateChangeNotifier) {
		this.moduleStateChangeNotifier = moduleStateChangeNotifier;
	}

}
