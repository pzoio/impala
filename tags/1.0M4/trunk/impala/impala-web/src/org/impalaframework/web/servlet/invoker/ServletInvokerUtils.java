/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletInvokerUtils {
	
	private static Log logger = LogFactory.getLog(ServletInvokerUtils.class);
	
	/**
	 * Used to invoke either the <code>HttpServiceInvoker.invoke</code> or <code>HttpServlet.service</code>, depending on the class of target.
	 * In both cases, the request and response are passed through.
	 * 
	 * @param target either an instance of <code>HttpServiceInvoker</code> or <code>HttpServlet</code>.
	 * @param filterChain instanceof <code>FilterChain</code>. Applies only if target is instance of <code>Filter</code>
	 */
	public static void invoke(Object target, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if (target instanceof HttpServiceInvoker) {
			HttpServiceInvoker invoker = (HttpServiceInvoker) target;
			invoker.invoke(request, response, null);
		} else if (target instanceof HttpServlet) {
			HttpServlet servlet = (HttpServlet) target;
			servlet.service(request, response);
		} else if (target instanceof Filter) {
			Filter filter = (Filter) target;
			filter.doFilter(request, response, filterChain);
		} else {
			logger.warn("invoke called with target " + (target != null ) + " which is not an instance of " + HttpServiceInvoker.class.getSimpleName() + ", " +
					HttpServlet.class.getSimpleName() + " or " + Filter.class.getName());
		}
		
	}
}