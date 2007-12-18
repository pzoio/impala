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

package org.impalaframework.spring.plugin;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Phil Zoio
 */
public class PluginBeanPostProcessorTest extends TestCase {

	private PluginBeanPostProcessor p;
	private DefaultListableBeanFactory beanFactory;
	private DefaultListableBeanFactory parentBeanFactory;
	private PluginContributionEndPoint pluginProxyFactoryBean;
	private FactoryBean factoryBean;

	public void setUp()
	{
		p = new PluginBeanPostProcessor();
		beanFactory = createMock(DefaultListableBeanFactory.class);
		parentBeanFactory = createMock(DefaultListableBeanFactory.class);
		pluginProxyFactoryBean = createMock(PluginProxyFactoryBean.class);
		factoryBean = createMock(FactoryBean.class);
		p.setBeanFactory(beanFactory);
	}
	
	public void testNull() {
		p.setBeanFactory(null);
		p.postProcessAfterInitialization(new Object(), "mybean");
		p.findFactoryBean("mybean");
	}
	
	public void testPostProcessAfterInitialization() {
		expectFactoryBean();
		Object object = new Object();
		p.setPluginSpec(new SimpleModuleDefinition("pluginName"));
		
		//this is the method we are expecting to be called
		pluginProxyFactoryBean.registerTarget("pluginName", object);
		
		replay(beanFactory);
		replay(parentBeanFactory);
		replay(pluginProxyFactoryBean);
		assertEquals(object, p.postProcessAfterInitialization(object, "mybean"));
		verify(beanFactory);
		verify(parentBeanFactory);
		verify(pluginProxyFactoryBean);
	}
	
	public void testPostProcessAfterInitializationFactoryBean() throws Exception {
		expectFactoryBean();

		//verify that if the object is a factory bean
		//then the registered object is the factoryBean.getObject()
		Object object = new Object();
		expect(factoryBean.getObject()).andReturn(object);
		p.setPluginSpec(new SimpleModuleDefinition("pluginName"));
		
		pluginProxyFactoryBean.registerTarget("pluginName", object);
		
		replay(beanFactory);
		replay(parentBeanFactory);
		replay(pluginProxyFactoryBean);
		replay(factoryBean);
		assertEquals(factoryBean, p.postProcessAfterInitialization(factoryBean, "mybean"));
		verify(beanFactory);
		verify(parentBeanFactory);
		verify(pluginProxyFactoryBean);
		verify(factoryBean);
	}
	
	
	public void testFindFactoryBean() {
		expectFactoryBean();
		
		replay(beanFactory);
		replay(parentBeanFactory);
		assertEquals(pluginProxyFactoryBean, p.findFactoryBean("mybean"));
		verify(beanFactory);
		verify(parentBeanFactory);
	}

	private void expectFactoryBean() {
		expect(beanFactory.getParentBeanFactory()).andReturn(parentBeanFactory);
		expect(parentBeanFactory.containsBean("&mybean")).andReturn(true);
		expect(parentBeanFactory.getBean("&mybean")).andReturn(pluginProxyFactoryBean);
	}

}
