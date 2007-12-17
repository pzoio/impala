package org.impalaframework.module.web;

import org.impalaframework.module.loader.DelegatingContextLoader;
import org.impalaframework.module.spec.PluginSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebPlaceholderDelegatingContextLoader implements DelegatingContextLoader {

	public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent, PluginSpec plugin) {
		GenericWebApplicationContext context = new GenericWebApplicationContext();
		context.refresh();
		return context;
	}

}
