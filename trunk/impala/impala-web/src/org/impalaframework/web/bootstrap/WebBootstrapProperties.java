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

package org.impalaframework.web.bootstrap;

import javax.servlet.ServletContext;

/**
 * Configuration properties which are specific to running Impala in a web environment.
 * @author Phil Zoio
 */
public interface WebBootstrapProperties {

	/**
	 * Whether modules should be automatically reloaded if the file/directory
	 * containing the modules has modified. If true, a background thread will
	 * monitor module files/folders and automatically reload modules when
	 * changes are detected. Default is <b>false</b>.
	 */
	String AUTO_RELOAD_MODULES = "auto.reload.modules";
	
	/**
	 * Whether {@link ServletContext} attributes and resources should be partitioned
	 * across modules. This makes it possible to set attributes and make resources
	 * visible within modules only through {@link ServletContext} methods.
	 */
	String PARTITIONED_SERVLET_CONTEXT = "partitioned.servlet.context";

	/**
	 * Attempt to protect session state when module is reloaded. If class loader
	 * which loaded original object in session is replaced, the object in
	 * session will no longer be visible to application code once module is
	 * reloaded. The solution is to clone the object via Java serialization,
	 * replacing the class loader of session object during the cloning process.
	 * The cloned object is then added to the session in place of the old object
	 * with the stale class loader.
	 */
	String SESSION_MODULE_PROTECTION = "session.module.protection";
	
	/**
	 * This property must be set to true if you want to run a web application
	 * embedded within the IDE, without any specific packaging required.
	 * Suitable for a fast develop/deploy/test cycle. The alternative involves
	 * creating a war file with modules placed in jars files in
	 * <i>WEB-INF/modules</i>. This is the default, and is the valid production
	 * setting.
	 */
	String EMBEDDED_MODE = "embedded.mode";

}
