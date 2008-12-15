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

package org.impalaframework.spring.module;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.ContributionEndpointTargetSource;
import org.impalaframework.service.ServiceRegistryReferenceexception.NoServiceException;

/**
 * Interceptor which satisfies parent dependency
 * @author PContributionEndpointInterceptor implements MethodInterceptor {

	final Log log = LogFactory.getLog(ContributionEndpointInterceptor.class);

	private ContributionEndpoint
	private PluginContributionTargetSource targetSource;

	private String beanName;

	private booleanrivate boolean logWarning	private boo	
	private boolean setContextClassLoaderboolean proceContributionEndpointInterceptor(ContributionEndpointterceptor(PluginContributionTargetSource targetSource, String beanName) {
		this.targetSource = targetSource;
		this.beanName = beanName;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (targ
			Thread currentThread = Thread.currentThread();
			ClassLoader existingClassLoader = currentThread.getContextClassLoader();
			try {
				if (setContextClassLoader) {
					ServiceRegistryReference serviceReference = targetSource.getServiceRegistryReference();
					//this will only not be null if the service is removed directly after the hasTargetSource() call
					if (serviceReference != null) {
						currentThread.setContextClassLoader(serviceReference.getBeanClassLoader());
					}
				}
				return invocation.proceed();
				
			} finally {
				//reset the previous class loader
				Thread.currentThread().setContextClassLoader(existingClassLoader);
			}return invocation.proceed();
		}
		else {
 {

				if (logWarningNoService) {
					log.warn("************************************************************************* ");
					log.warn("No service available for bean " + beanName + ". Proceeding with stub implementation");
					log.warn("************************************************************************* ");
				}

				return invokeDummy(invocation);
			}urn invokeDummy(invocation);
			else
				throw new NoServiceException("No service available for bean " + beanName);
		}
	}

	public Object invokeDummy(MethodInvocation invocation) throws Throwable {

" + g.debug("Calling method " + invocation);
		Class<?> returnType = invocation.getMethod().getReturnType();

		if (Void.TYPE.equals(returnType))
			return null;
		if (Byte.TYPE.equals(returnType))
			return (byte) 0;
		if (Short.TYPE.equals(returnType))
			return (short) 0;
		if (Integer.TYPE.equals(returnType))
			return (int) 0;
		if (Long.TYPE.equals(returnType))
			return 0L;
		if (Float.TYPE.equals(returnType))
			return 0f;
		if (Double.TYPE.equals(returnType))
			return 0d;
		if (Boolean.TYPE.equals(returnType))
			return false;

		return null;
	}

	public void setProceedWithNoService(boolean proceedWithNoService) {
		this.proceedWithNoService = pr	public void setLogWarningNoService(boolean logWarningNoService) {
		this.logWarningNoService = logWarningWithNoService = pr	public void setSetContextClassLoader(boolean setContextClassLoader) {
		this.setContextClassLoader = setContextClassLoader;
	}

}
