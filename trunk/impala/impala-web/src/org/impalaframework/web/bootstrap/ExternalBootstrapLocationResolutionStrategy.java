package org.impalaframework.web.bootstrap;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.PropertyUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.WebModuleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

public class ExternalBootstrapLocationResolutionStrategy extends DefaultBootstrapLocationResolutionStrategy {

	final Log logger = LogFactory.getLog(ExternalBootstrapLocationResolutionStrategy.class);

	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		String bootstrapLocationsResource = WebModuleUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		
		if (bootstrapLocationsResource == null) {
			// then look for init parameter which contains these
			return super.getBootstrapContextLocations(servletContext);
		}
		else {
			// figure out which resource loader to use
			ResourceLoader resourceLoader = getResourceLoader();
			Resource bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);

			if (bootStrapResource == null || !bootStrapResource.exists()) {
				logger.info("Unable to load locations resource from " + 
						bootstrapLocationsResource + ". Delegating to superclass");
				return super.getBootstrapContextLocations(servletContext);
			}
			Properties loadProperties = PropertyUtils.loadProperties(bootStrapResource);
			String property = loadProperties.getProperty(WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM);

			if (property == null) {
				throw new ConfigurationException("Bootstrap location resource '" + bootStrapResource
						+ "' does not contain property '" + WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM + "'");
			}

			return StringUtils.tokenizeToStringArray(property, " ,");
		}
	}

	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

}
