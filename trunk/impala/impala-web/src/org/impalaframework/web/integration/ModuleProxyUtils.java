package org.impalaframework.web.integration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.impalaframework.util.InstantiationUtils;
import org.impalaframework.util.ObjectUtils;
import org.springframework.util.ClassUtils;

/**
 * Class with static methods shared by <code>ModuleProxyServlet</code> and <code>ModuleProxyFilter</code>.
 * @author Phil Zoio
 */
public class ModuleProxyUtils {

	public static void maybeLogRequest(HttpServletRequest request, Log logger) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Request context path: " + request.getContextPath());
			logger.debug("Request local address: " + request.getLocalAddr());
			logger.debug("Request local name: " + request.getLocalName());
			logger.debug("Request path info: " + request.getPathInfo());
			logger.debug("Request path translated: " + request.getPathTranslated());
			logger.debug("Request query string: " + request.getQueryString());
			logger.debug("Request servlet path: " + request.getServletPath());
			logger.debug("Request request URI: " + request.getRequestURI());
			logger.debug("Request request URL: " + request.getRequestURL());
			logger.debug("Request session ID: " + request.getRequestedSessionId());
		}
	}
	
	public static String getModuleName(String servletPath, String modulePrefix) {
		
		String tempModuleName = (servletPath.startsWith("/") ? servletPath.substring(1) : servletPath);
		int firstSlash = tempModuleName.indexOf('/');
		if (firstSlash < 0) {
			return null;
		}
		
		String moduleName = tempModuleName.substring(0, firstSlash);
		if (modulePrefix != null) {
			moduleName = modulePrefix + moduleName;
		}
		return moduleName;
	}

	public static RequestModuleMapper newRequestModuleMapper(final String requestModuleMapperClass) {
		if (requestModuleMapperClass != null) {
			//remember, ClassUtils will use the thread context class loader if it is available
			final Object object = InstantiationUtils.instantiate(requestModuleMapperClass, ClassUtils.getDefaultClassLoader());
			return ObjectUtils.cast(object, RequestModuleMapper.class);
		} else {
			return new ServletPathRequestModuleMapper();
		}
	}
}
