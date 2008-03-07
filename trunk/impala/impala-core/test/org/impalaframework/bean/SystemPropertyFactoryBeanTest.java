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

import junit.framework.TestCase;

public class SystemPropertyFactoryBeanTest extends TestCase {

	private SystemPropertyFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = new SystemPropertyFactoryBean();
		System.clearProperty("myProperty");
	}
	
	public final void testNoSysPropertySet() throws Exception {
		factoryBean.setPropertyName("myProperty");
		
		factoryBean.afterPropertiesSet();
		assertNull(factoryBean.getObject());
		
		factoryBean.setDefaultValue("myValue");
		
		factoryBean.afterPropertiesSet();
		assertEquals("myValue", factoryBean.getObject());
	}
	
	public final void testWithSysPropertySet() throws Exception {
		factoryBean.setPropertyName("myProperty");
		System.setProperty("myProperty", "systemPropertyValue");
		
		factoryBean.afterPropertiesSet();
		assertEquals("systemPropertyValue", factoryBean.getObject());
	}

}
