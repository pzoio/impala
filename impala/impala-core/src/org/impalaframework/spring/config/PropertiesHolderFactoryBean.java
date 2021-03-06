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

package org.impalaframework.spring.config;

import java.util.Properties;

import org.impalaframework.config.PropertiesHolder;
import org.springframework.beans.factory.FactoryBean;

/**
 * {@link FactoryBean} which exposes {@link Properties} held by {@link PropertiesHolder} instance.
 * @author Phil Zoio
 */
public class PropertiesHolderFactoryBean implements FactoryBean<Properties> {

    /**
     * Returns {@link Properties} instance held by {@link Properties} holder.
     * Otherwise, returns empty {@link Properties} instance.
     */
    public Properties getObject() throws Exception {
        Properties properties = PropertiesHolder.getInstance().getProperties();
        
        if (properties != null)         
            return properties;
        
        return new Properties();
    }

    public Class<Properties> getObjectType() {
        return Properties.class;
    }

    public boolean isSingleton() {
        return true;
    }

}
