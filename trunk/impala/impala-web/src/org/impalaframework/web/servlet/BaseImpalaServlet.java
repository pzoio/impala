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

package org.impalaframework.web.servlet;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public abstract class BaseImpalaServlet extends DispatcherServlet {

	private static final Log logger = LogFactory.getLog(BaseImpalaServlet.class);

	private static final long serialVersionUID = 1L;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	private final Lock r = rwl.readLock();

	private final Lock w = rwl.writeLock();

	public BaseImpalaServlet() {
		super();
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		r.lock();
		try {
			super.doService(request, response);
		}
		finally {
			r.unlock();
		}
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		w.lock();
		try {
			WebApplicationContext wac = createWebApplicationContext();

			onRefresh(wac);

			if (isPublishContext()) {
				// Publish the context as a servlet context attribute.
				String attrName = getServletContextAttributeName();
				getServletContext().setAttribute(attrName, wac);
				if (logger.isDebugEnabled()) {
					logger.debug("Published WebApplicationContext of servlet '" + getServletName()
							+ "' as ServletContext attribute with name [" + attrName + "]");
				}
			}

			return wac;
		}
		finally {
			w.unlock();
		}
	}

	protected abstract WebApplicationContext createWebApplicationContext() throws BeansException;

}
