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

package org.impalaframework.interactive.web.bootstrap;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.config.PropertySource;
import org.impalaframework.interactive.bootstrap.TestContextLocationResolver;
import org.impalaframework.web.bootstrap.WebContextLocationResolver;

/**
 * Extends {@link WebContextLocationResolver} to add test support, as provided by {@link TestContextLocationResolver}
 * @author Phil Zoio
 */
public class WebTestContextLocationResolver extends WebContextLocationResolver {

    @Override
    public void addCustomLocations(ConfigurationSettings configSettings,
            PropertySource propertySource) {
        super.addCustomLocations(configSettings, propertySource);
        
        TestContextLocationResolver.addBootstrapConfig(configSettings, propertySource);
    }

}
