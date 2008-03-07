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

package org.impalaframework.spring;

import junit.framework.TestCase;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Phil Zoio
 */
public class MissingBeanTest extends TestCase {

	public void test() {
		ProxyCreatingBeanFactory beanFactory = new ProxyCreatingBeanFactory();
		GenericApplicationContext context = new GenericApplicationContext(beanFactory);

		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
		xmlReader.loadBeanDefinitions(new ClassPathResource("missingbean/spring-context.xml"));

		context.refresh();
		ClientBean clientBean = (ClientBean) context.getBean("clientBean");

		System.out.println("\nOutput when client bean uses 'missing' collaborator:");
		clientBean.doStuff();
	}

}
