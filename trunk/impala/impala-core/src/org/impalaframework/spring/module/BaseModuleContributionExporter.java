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

package org.impalaframework.spring.module;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.module.definition.ModuleDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <code>BeanPostProcessor</code> which attempts to register the created bean
 * with the parent's bean factory's <code>ContributionProxyFactoryBean</code>
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleContributionExporter implements ModuleDefinitionAware, BeanFactoryAware,
		InitializingBean, DisposableBean {

	final Logger logger = LoggerFactory.getLogger(BaseModuleContributionExporter.class);

	private BeanFactory beanFactory;

	private ModuleDefinition moduleDefinition;

	private Map<Object, ContributionEndpoint> contributionMap = new IdentityHashMap<Object, ContributionEndpoint>();

	protected void processContributions(Collection<String> contributions) {
		for (String beanName : contributions) {

			Object bean = beanFactory.getBean(beanName);

			ContributionEndpoint endPoint = getContributionEndPoint(beanName, bean);

			if (endPoint != null) {
				String moduleName = moduleDefinition.getName();
				logger.info("Registering bean " + beanName + " from module " + moduleName);
				endPoint.registerTarget(moduleName, bean);
				contributionMap.put(bean, endPoint);
			}
		}
	}

	public void destroy() throws Exception {

		Set<Object> contributionKeys = contributionMap.keySet();
		for (Object bean : contributionKeys) {
			ContributionEndpoint contributionEndpoint = contributionMap.get(bean);
			contributionEndpoint.deregisterTarget(bean);
		}

	}

	protected ContributionEndpoint getContributionEndPoint(String beanName, Object bean) {
		ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(beanFactory, beanName);
		return endPoint;
	}

	protected BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

}
