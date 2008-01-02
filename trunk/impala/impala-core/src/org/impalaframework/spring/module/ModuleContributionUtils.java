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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.AbstractBeanFactory;

/**
 * <code>BeanPostProcessor</code> which attempts to register the created bean
 * with the parent's bean factory's <code>ContributionProxyFactoryBean</code>
 * 
 * @author Phil Zoio
 */
public abstract class ModuleContributionUtils {

	final Logger logger = LoggerFactory.getLogger(ModuleContributionUtils.class);

	static ContributionEndpoint findContributionEndPoint(BeanFactory beanFactory, String beanName) {
	
		ContributionEndpoint factoryBean = null;
		if (beanFactory instanceof AbstractBeanFactory) {
	
			AbstractBeanFactory abf = (AbstractBeanFactory) beanFactory;
			BeanFactory parentBeanFactory = abf.getParentBeanFactory();
	
			if (parentBeanFactory != null) {
	
				String parentFactoryBeanName = "&" + beanName;
	
				try {
	
					if (parentBeanFactory.containsBean(parentFactoryBeanName)) {
						Object o = parentBeanFactory.getBean(parentFactoryBeanName);
						if (o instanceof ContributionEndpoint) {
							factoryBean = (ContributionEndpoint) o;
						}
					}
				}
				catch (BeanIsNotAFactoryException e) {
					// This is check is only present due to a bug in an early
					// 2.0
					// release, which was fixed certainly before 2.0.6
					// ordinarily, this exception will never be thrown
				}
			}
		}
		return factoryBean;
	}

	static Object getTarget(Object bean, String beanName) {
		
		//FIXME test, rename
		
		Object target = null;
		if (bean instanceof FactoryBean) {
			FactoryBean factoryBean = (FactoryBean) bean;
			try {
				target = factoryBean.getObject();
			}
			catch (Exception e) {
				String errorMessage = "Failed getting object from factory bean " + factoryBean + ", bean name " + beanName;
				throw new BeanInstantiationException(factoryBean.getObjectType(), errorMessage, e);
			}
		}
		else {
			target = bean;
		}
		return target;
	}

}
