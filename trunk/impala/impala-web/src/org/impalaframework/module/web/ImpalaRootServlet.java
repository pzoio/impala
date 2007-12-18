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

package org.impalaframework.module.web;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.monitor.ModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ImpalaRootServlet extends BaseImpalaServlet implements ModuleChangeListener {

	final Logger logger = LoggerFactory.getLogger(ImpalaRootServlet.class);

	private static final long serialVersionUID = 1L;

	private boolean initialized;

	public ImpalaRootServlet() {
		super();
	}

	// lifted straight from XmlWebApplicationContext
	protected String[] getDefaultConfigLocations() {
		String nameSpace = getNamespace();
		if (nameSpace != null) {
			return new String[] { WebConstants.DEFAULT_CONFIG_LOCATION_PREFIX + nameSpace
					+ WebConstants.DEFAULT_CONFIG_LOCATION_SUFFIX };
		}
		else {
			return new String[] { WebConstants.DEFAULT_CONFIG_LOCATION };
		}
	}

	protected WebApplicationContext createWebApplicationContext() throws BeansException {

		ModuleManagementSource factory = (ModuleManagementSource) getServletContext().getAttribute(
				WebConstants.IMPALA_FACTORY_ATTRIBUTE);

		if (factory == null) {
			throw new RuntimeException(ModuleManagementSource.class.getSimpleName()
					+ " not set. Have you set up your Impala context loader properly? "
					+ "You need to set up a Spring context loader which will set up the parameter '"
					+ WebConstants.IMPALA_FACTORY_ATTRIBUTE + "'");
		}


		String pluginName = getServletName();
		if (!initialized) {

			ModuleStateHolder moduleStateHolder = factory.getPluginStateManager();
			RootModuleDefinition existing = moduleStateHolder.getParentSpec();
			RootModuleDefinition newSpec = moduleStateHolder.cloneParentSpec();
			newPluginSpec(pluginName, newSpec);

			//FIXME this should be deprecated!
			ModuleModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry()
					.getPluginModificationCalculator(ModificationExtractorType.STRICT);
			ModuleTransitionSet transitions = calculator.getTransitions(existing, newSpec);

			moduleStateHolder.processTransitions(transitions);

		}

		ApplicationContext context = factory.getPluginStateManager().getPlugins().get(pluginName);

		if (factory.containsBean("scheduledPluginMonitor") && !initialized) {
			logger.info("Registering " + getServletName() + " for plugin modifications");
			ModuleChangeMonitor moduleChangeMonitor = (ModuleChangeMonitor) factory.getBean("scheduledPluginMonitor");
			moduleChangeMonitor.addModificationListener(this);
		}

		this.initialized = true;

		return (WebApplicationContext) context;
	}

	protected ModuleDefinition newPluginSpec(String pluginName, RootModuleDefinition rootModuleDefinition) {
		return new WebRootPluginSpec(rootModuleDefinition, pluginName, getSpringConfigLocations());
	}

	protected String[] getSpringConfigLocations() {
		String[] locations = null;
		if (getContextConfigLocation() != null) {
			locations = StringUtils.tokenizeToStringArray(getContextConfigLocation(),
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
		}
		else {
			locations = getDefaultConfigLocations();
		}
		return locations;
	}

}
