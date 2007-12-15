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

package org.impalaframework.plugin.loader;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransition;
import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.monitor.PluginMonitor;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.plugin.transition.DefaultPluginStateManager;
import org.impalaframework.plugin.transition.LoadTransitionProcessor;
import org.impalaframework.plugin.transition.PluginStateUtils;
import org.impalaframework.plugin.transition.TransitionProcessorRegistry;
import org.impalaframework.plugin.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class DefaultApplicationContextLoaderTest extends TestCase {

	private DefaultApplicationContextLoader loader;

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private DefaultPluginStateManager pluginStateManager;

	private PluginModificationCalculator calculator;

	public void setUp() {
		System.setProperty("impala.parent.project", "impala-core");
		
		PropertyClassLocationResolver resolver = new PropertyClassLocationResolver();

		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver){
			@Override
			public ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent) {
				return this.getClass().getClassLoader();
			}}) ;
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));

		loader = new DefaultApplicationContextLoader(registry);
		pluginStateManager = new DefaultPluginStateManager();
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(loader);
		UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		pluginStateManager.setTransitionProcessorRegistry(transitionProcessors);
		
		calculator = new StrictPluginModificationCalculator();
	}
	
	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testResourceBasedValue() {
		PluginSpecProvider spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
		PluginSpec p2 = spec.getPluginSpec().getPlugin(plugin2);
		new SimplePluginSpec(p2, plugin3);
		PluginStateUtils.addPlugin(pluginStateManager, calculator, spec.getPluginSpec());

		ConfigurableApplicationContext parent = pluginStateManager.getParentContext();

		// the implementing FileMonitorBean3 will find the monitor.properties
		// file
		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		assertEquals(333L, bean3.lastModified((File) null));

		// this time, we will not be able to find the resource from
		// FileMonitorBean3
		assertEquals(100L, bean3.lastModified(new File("./")));
	}

	public void testLoadUnloadPlugins() {

		PluginSpecProvider spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		PluginStateUtils.addPlugin(pluginStateManager, calculator, spec.getPluginSpec());
		PluginSpec root = spec.getPluginSpec();

		ConfigurableApplicationContext parent = pluginStateManager.getParentContext();
		assertNotNull(parent);
		assertEquals(3, pluginStateManager.getPlugins().size());

		FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		// shutdown plugin and check behaviour has gone
		PluginStateUtils.removePlugin(pluginStateManager, calculator, plugin2);

		try {
			bean2.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// bean 2 still works
		assertEquals(999L, bean1.lastModified((File) null));

		PluginStateUtils.removePlugin(pluginStateManager, calculator, plugin1);

		try {
			bean1.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// now reload the plugin, and see that behaviour returns
		PluginStateUtils.addPlugin(pluginStateManager, calculator, new SimplePluginSpec(plugin2));
		bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		PluginStateUtils.addPlugin(pluginStateManager, calculator, new SimplePluginSpec(plugin1));
		bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		PluginSpec p2 = root.getPlugin(plugin2);
		PluginStateUtils.addPlugin(pluginStateManager, calculator, new SimplePluginSpec(p2, plugin3));
		assertEquals(333L, bean3.lastModified((File) null));

		final ConfigurableApplicationContext applicationPlugin3 = pluginStateManager.getPlugin(plugin3);
		applicationPlugin3.close();

		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	public void testLoadAll() {

		PluginSpecProvider spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
		final PluginSpec p2 = spec.getPluginSpec().getPlugin(plugin2);
		new SimplePluginSpec(p2, plugin3);

		PluginStateUtils.addPlugin(pluginStateManager, calculator, spec.getPluginSpec());

		ConfigurableApplicationContext parent = pluginStateManager.getParentContext();
		assertNotNull(parent);

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		bean3.lastModified((File) null);

		// check that all three plugins have loaded
		assertEquals(4, pluginStateManager.getPlugins().size());
	}

	class RecordingPluginMonitor implements PluginMonitor {

		private int started = 0;

		private int stopped = 0;

		public void addModificationListener(PluginModificationListener listener) {
		}

		public void setResourcesToMonitor(String pluginName, Resource[] resources) {
		}

		public void start() {
			started++;
		}

		public void stop() {
			stopped++;
		}

		public int getStarted() {
			return started;
		}

		public int getStopped() {
			return stopped;
		}

	}

}
