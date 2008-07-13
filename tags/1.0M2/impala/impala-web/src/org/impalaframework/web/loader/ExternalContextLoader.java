package org.impalaframework.web.loader;

import javax.servlet.ServletContext;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

public class ExternalContextLoader extends ContextLoader {
	
	static String EXTERNAL_CONFIG_LOCATIONS_PARAM = "external.config.locations";

	//TODO create 2.0 compliant implementation
	protected void customizeContext(ServletContext servletContext,
			ConfigurableWebApplicationContext applicationContext) {
		String[] configLocations = applicationContext.getConfigLocations();

		String[] expandedLocations = getExpandedLocations(configLocations);

		applicationContext.setConfigLocations(expandedLocations);
		super.customizeContext(servletContext, applicationContext);
	}

	protected String[] getExpandedLocations(String[] configLocations) {
		String[] expandedLocations = configLocations;
		
		String externalLocations = System.getProperty(EXTERNAL_CONFIG_LOCATIONS_PARAM);
		if (externalLocations != null) {
			String[] externalLocationsArray = externalLocations.split(",");
			for (int i = 0; i < externalLocationsArray.length; i++) {
				externalLocationsArray[i] = externalLocationsArray[i].trim();
			}
			expandedLocations = new String[configLocations.length + externalLocationsArray.length];
			System.arraycopy(configLocations, 0, expandedLocations, 0, configLocations.length);
			System.arraycopy(externalLocationsArray, 0, expandedLocations, configLocations.length, externalLocationsArray.length);
		}
		return expandedLocations;
	}


}
