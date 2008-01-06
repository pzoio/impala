/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.web.loader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.impalaframework.web.WebConstants;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class ImpalaContextLoaderListener extends ContextLoaderListener {
	
	private ContextLoader contextLoader;
	
	private Class<? extends ContextLoader> defaultContextLoaderClass = WebXmlBasedContextLoader.class;

	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		contextLoader = createContextLoader(servletContext);
		contextLoader.initWebApplicationContext(servletContext);
	}

	@SuppressWarnings("unchecked")
	protected ContextLoader createContextLoader(ServletContext servletContext) {
		String contextLoaderClassName = servletContext.getInitParameter(WebConstants.CONTEXT_LOADER_CLASS_NAME);
		
		Class<? extends ContextLoader> contextLoaderClass = defaultContextLoaderClass;
		
		if (contextLoaderClassName != null) {
			try {
				contextLoaderClass = ClassUtils.forName(contextLoaderClassName);
			}
			catch (Throwable e) {
				throw new IllegalStateException("Unable to instantiate context loader class " + contextLoaderClassName);
			}
		}
		
		ContextLoader contextLoader = null;
		try {
			contextLoader = contextLoaderClass.newInstance();
		}
		catch (Exception e) {
			throw new IllegalStateException("Error instantiating context loader class " + contextLoaderClassName + ": " + e.getMessage(), e);
		}
		
		return contextLoader;
	}

	public ContextLoader getContextLoader() {
		return contextLoader;
	}

	public void contextDestroyed(ServletContextEvent event) {
		if (contextLoader != null) {
			contextLoader.closeWebApplicationContext(event.getServletContext());
		}
	}

}
