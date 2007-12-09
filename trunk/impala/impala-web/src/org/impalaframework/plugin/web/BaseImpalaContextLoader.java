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

package org.impalaframework.plugin.web;

import java.util.Arrays;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.BootstrapBeanFactory;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public abstract class BaseImpalaContextLoader extends ContextLoader {

	//FIXME make sure this instantiates in unit tests
	
	final Logger logger = LoggerFactory.getLogger(BaseImpalaContextLoader.class);

	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {

		String[] locations = getBootstrapContextLocations(servletContext);
		logger.info("Loading bootstrap context from locations {}", Arrays.toString(locations));
		
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		final GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(beanFactory);
		applicationContext.setServletContext(servletContext);

		XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
		for (int i = 0; i < locations.length; i++) {
			definitionReader.loadBeanDefinitions(new ClassPathResource(locations[i]));
		}
		applicationContext.refresh();

		BootstrapBeanFactory factory = new BootstrapBeanFactory(applicationContext);

		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		
		// load the parent context, which is web-independent
		ParentSpec pluginSpec = getPluginSpec(servletContext);

		// figure out the plugins to reload
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationCalculationType.STRICT);
		PluginTransitionSet transitions = calculator.getTransitions(null, pluginSpec);
		pluginStateManager.processTransitions(transitions);

		// add factory to servlet context
		servletContext.setAttribute(WebConstants.IMPALA_FACTORY_PARAM, factory);
		WebApplicationContext parentContext = (WebApplicationContext) pluginStateManager.getParentContext();

		return parentContext;
	}

	protected String[] getBootstrapContextLocations(ServletContext servletContext) {
		String[] locations = new String[] { 
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml",
				"META-INF/impala-web-listener-bootstrap.xml" };
		return locations;
	}

	protected abstract ParentSpec getPluginSpec(ServletContext servletContext);

}
