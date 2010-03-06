/*
  * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.web.servlet.wrapper;

import java.net.URL;

import javax.servlet.ServletContext;

/**
 * Extension of {@link ServletContext} which defines {@link #getLocalResource(String)}
 * to allow for module-local resource to be returned.
 * @author Phil Zoio
 */
public interface LocalResourceAwareServletContext extends ServletContext {
    
    /**
     * Defines method by which local resource can be retrieved from within a module
     */
    URL getLocalResource(String path);
}
