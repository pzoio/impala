package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

public class DefaultBootstrapLocationResolutionStrategy {
	protected String[] getBootstrapContextLocations(ServletContext servletContext) {
		//FIXME test
		String[] locations = new String[] { 
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml",
				"META-INF/impala-web-listener-bootstrap.xml" };
		return locations;
	}
}
