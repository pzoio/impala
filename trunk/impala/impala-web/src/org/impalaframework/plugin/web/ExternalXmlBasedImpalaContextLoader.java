package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.spec.ParentSpec;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ExternalXmlBasedImpalaContextLoader extends BaseImpalaContextLoader {

	@Override
	public ParentSpec getPluginSpec(ServletContext servletContext) {
		
		//FIXME allow this to reloadable using JMX
		
		String locationsResourceName = WebPluginUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM);

		if (locationsResourceName == null) {
			throw new IllegalStateException(
					"Unable to resolve locations resource name parameter '"
							+ WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM
							+ "' from either a system property or a 'context-param' entry in the web application's WEB-INF/web.xml");
		}

		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(locationsResourceName);

		if (!resource.exists()) {
			throw new IllegalStateException("Plugin spec XML resource '" + resource.getDescription()
					+ "' does not exist");
		}

		WebXmlPluginSpecBuilder pluginSpecBuilder = new WebXmlPluginSpecBuilder();
		pluginSpecBuilder.setResource(resource);
		return pluginSpecBuilder.getParentSpec();
	}

	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

}
