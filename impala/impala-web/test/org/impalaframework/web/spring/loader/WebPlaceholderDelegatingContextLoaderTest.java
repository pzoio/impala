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

package org.impalaframework.web.spring.loader;

import junit.framework.TestCase;

import org.impalaframework.web.spring.loader.WebPlaceholderDelegatingContextLoader;
import org.springframework.context.ConfigurableApplicationContext;

public class WebPlaceholderDelegatingContextLoaderTest extends TestCase {

    public final void testLoadApplicationContext() {
        WebPlaceholderDelegatingContextLoader loader = new WebPlaceholderDelegatingContextLoader();
        ConfigurableApplicationContext context = loader.loadApplicationContext(null, null);
        assertNotNull(context);
        assertTrue(context.isActive());
    }

}
