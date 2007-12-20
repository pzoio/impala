package org.impalaframework.web.servlet;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.web.module.ServletModuleDefinition;
import org.impalaframework.web.module.WebConstants;

public class WebModuleServlet extends RootWebModuleServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected ModuleDefinition newPluginSpec(String pluginName, RootModuleDefinition rootModuleDefinition) {
		ModuleDefinition moduleDefinition = rootModuleDefinition;

		String pluginNameString = getServletContext().getInitParameter(WebConstants.ROOT_WEB_PLUGIN_PARAM);

		if (pluginNameString != null) {
			ModuleDefinition rootWebPlugin = rootModuleDefinition.findPlugin(pluginNameString, true);
			if (rootWebPlugin == null) {
				throw new IllegalStateException("Unable to find root plugin '" + pluginNameString
						+ "' specified using the web.xml parameter '" + WebConstants.ROOT_WEB_PLUGIN_PARAM + "'");
			}
			moduleDefinition = rootWebPlugin;
		}

		return new ServletModuleDefinition(moduleDefinition, pluginName, getSpringConfigLocations());
	}

}
