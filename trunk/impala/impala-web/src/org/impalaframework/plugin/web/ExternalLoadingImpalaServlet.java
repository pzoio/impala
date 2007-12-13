package org.impalaframework.plugin.web;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ExternalLoadingImpalaServlet extends BaseImpalaServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected WebApplicationContext createWebApplicationContext() throws BeansException {

		// the superclass closes the plugins
		ImpalaBootstrapFactory factory = (ImpalaBootstrapFactory) getServletContext().getAttribute(
				WebConstants.IMPALA_FACTORY_ATTRIBUTE);

		if (factory == null) {
			throw new IllegalStateException("Unable to load " + ExternalLoadingImpalaServlet.class.getName()
					+ " as no attribute '" + WebConstants.IMPALA_FACTORY_ATTRIBUTE
					+ "' has been set up. Have you set up your Impala ContextLoader correctly?");
		}

		String servletName = getServletName();
		PluginStateManager pluginStateManager = factory.getPluginStateManager();

		ConfigurableApplicationContext plugin = pluginStateManager.getPlugin(servletName);
		if (plugin != null) {
			if (plugin instanceof WebApplicationContext) {
				return (WebApplicationContext) plugin;
			}
			else {
				throw new IllegalStateException("Plugin registered under name of servlet '" + servletName
						+ "' needs to be an instance of " + WebApplicationContext.class.getName());
			}
		}
		else {
			throw new IllegalStateException("No plugin registered under the name of servlet '" + servletName + "'");
		}
	}

}
