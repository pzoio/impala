package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.operation.ReloadOperation;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

@ManagedResource(objectName = "impala:service=webPluginOperations", description = "MBean exposing reconfiguration of web application")
public class WebPluginReloader implements ServletContextAware {

	private ServletContext servletContext;

	@ManagedOperation(description = "Uses the PluginSpecBuilder which loaded the initial application context")
	public void reloadPlugins() {
		Assert.notNull(servletContext);

		ImpalaBootstrapFactory factory = (ImpalaBootstrapFactory) servletContext
				.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE);
		if (factory == null) {
			throw new IllegalStateException(
					"No instance of "
							+ ImpalaBootstrapFactory.class.getName()
							+ " found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.IMPALA_FACTORY_ATTRIBUTE");
		}

		PluginSpecBuilder builder = (PluginSpecBuilder) servletContext
				.getAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE);
		if (builder == null) {
			throw new IllegalStateException(
					"No instance of "
							+ PluginSpecBuilder.class.getName()
							+ " found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE");

		}
		
		new ReloadOperation(factory, builder).execute();
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
