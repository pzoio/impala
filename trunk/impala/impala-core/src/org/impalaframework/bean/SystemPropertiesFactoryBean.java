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

package org.impalaframework.bean;

import java.util.Properties;

/**
 * Extension of {@link SystemPropertyFactoryBean} which first attempts to
 * fall back to a property value from a supplied set of properties, before
 * looking for a default value
 * 
 * @author Phil Zoio
 */
public class SystemPropertiesFactoryBean extends SystemPropertyFactoryBean {

	private Properties properties;

	@Override
	protected String getValueIfPropertyNotFound() {
		String value = null;
		if (properties != null) {
			value = properties.getProperty(getPropertyName());
		}
		if (value == null)
			value = super.getValueIfPropertyNotFound();
		return value;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
