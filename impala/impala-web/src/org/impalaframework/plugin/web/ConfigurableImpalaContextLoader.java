package org.impalaframework.plugin.web;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.impalaframework.util.PropertyUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

/**
 * Allows you to specify bootstrapLocationsResource, either as a system property or as
 * a servlet context init parameter named WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM.
 * If found, then this resource is loaded as a property file and used to find the
 * parent context locations as well as the child plugin names for bootstrapping the 
 * application
 * @author Phil Zoio
  */
public class ConfigurableImpalaContextLoader extends ImpalaContextLoader {

	@Override
	protected String[] getBootstrapContextLocations(ServletContext servletContext) {
		String bootstrapLocationsResource = getLocationsResourceName(servletContext);

		if (bootstrapLocationsResource == null) {
			// then look for init parameter which contains these
			return super.getBootstrapContextLocations(servletContext);
		}
		else {
			// figure out which resource loader to use
			ResourceLoader resourceLoader = getResourceLoader();
			Resource bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);

			if (bootStrapResource == null || !bootStrapResource.exists()) {
				logger.info("Unable to load locations resource from {}. Delegating to superclass",
						bootstrapLocationsResource);
				return super.getBootstrapContextLocations(servletContext);
			}
			Properties loadProperties = PropertyUtils.loadProperties(bootStrapResource);
			String property = loadProperties.getProperty(WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM);

			if (property == null) {
				throw new IllegalStateException("Bootstrap location resource '" + bootStrapResource
						+ "' does not contain property '" + WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM + "'");
			}

			return StringUtils.tokenizeToStringArray(property, " ,");
		}
	}

	protected ResourceLoader getResourceLoader() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		return resourceLoader;
	}

	@Override
	protected String getPluginDefinitionString(ServletContext servletContext) {
		String bootstrapLocationsResource = getLocationsResourceName(servletContext);
		if (bootstrapLocationsResource == null) {
			return super.getPluginDefinitionString(servletContext);
		}
		else {
			ResourceLoader resourceLoader = getResourceLoader();
			Resource bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);

			if (bootStrapResource == null || !bootStrapResource.exists()) {
				logger.info("Unable to load locations resource from {}. Delegating to superclass",
						bootstrapLocationsResource);
				return super.getPluginDefinitionString(servletContext);
			}
			Properties loadProperties = PropertyUtils.loadProperties(bootStrapResource);
			String property = loadProperties.getProperty(WebConstants.BOOTSTRAP_PLUGIN_NAMES_PARAM);

			if (property == null) {
				//FIXME test
				throw new IllegalStateException("Bootstrap location resource '" + bootStrapResource
						+ "' does not contain property '" + WebConstants.BOOTSTRAP_PLUGIN_NAMES_PARAM + "'");
			}

			return property;
		}

	}

	String getLocationsResourceName(ServletContext servletContext) {
		// first look for System property which contains plugins definitions
		// location
		String bootstrapLocationsResource = System.getProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);

		if (bootstrapLocationsResource == null) {
			bootstrapLocationsResource = servletContext
					.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
		return bootstrapLocationsResource;
	}

}
