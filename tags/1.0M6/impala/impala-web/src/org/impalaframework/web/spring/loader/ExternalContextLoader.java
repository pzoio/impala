package org.impalaframework.web.spring.loader;

import javax.servlet.ServletContext;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Extension of <code>ContextLoader</code> which as well as using the context locations set up in the <i>web.xml</i> in the 
 * standard way, also allows additional context locations to specified using the <i>external.config.locations</i>
 * System property. Use -Dexternal.config.locations=<comma separated list of context locations>.
 * 
 * Note that this class requires Spring 2.5
 * 
 * @author Phil Zoio
 */
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
