package org.impalaframework.module.web;

import java.util.Set;

import javax.servlet.ServletContext;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.monitor.BasePluginModificationListener;
import org.impalaframework.module.monitor.PluginModificationEvent;
import org.impalaframework.module.monitor.PluginModificationListener;
import org.impalaframework.module.operation.ReloadNamedPluginOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

public class WebPluginModificationListener extends BasePluginModificationListener implements
		PluginModificationListener, ServletContextAware {

	final Logger logger = LoggerFactory.getLogger(WebPluginModificationListener.class);

	private ServletContext servletContext;

	private WebPluginModificationListener() {
		super();
	}

	public WebPluginModificationListener(ServletContext servletContext) {
		super();
		Assert.notNull(servletContext, "servletContext cannot be null");
		this.servletContext = servletContext;
	}

	public void pluginModified(PluginModificationEvent event) {
		Set<String> modified = getModifiedPlugins(event);

		if (!modified.isEmpty()) {
			ModuleManagementSource factory = (ModuleManagementSource) servletContext
					.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE);

			for (String pluginName : modified) {

				logger.info("Processing modified plugin {}", pluginName);

				ReloadNamedPluginOperation operation = new ReloadNamedPluginOperation(factory, pluginName);
				operation.execute();
			}
		}
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
