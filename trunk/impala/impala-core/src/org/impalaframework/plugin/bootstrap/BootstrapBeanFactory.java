package org.impalaframework.plugin.bootstrap;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.impalaframework.plugin.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

public class BootstrapBeanFactory implements BeanFactory, ImpalaBootstrapFactory {

	private final ApplicationContext applicationContext;

	public BootstrapBeanFactory(final ApplicationContext applicationContext) {
		super();
		Assert.notNull(applicationContext);
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContextLoader getApplicationContextLoader() {
		return (ApplicationContextLoader) getBean("applicationContextLoader", ApplicationContextLoader.class);
	}

	public ClassLocationResolver getClassLocationResolver() {
		return (ClassLocationResolver) getBean("classLocationResolver", ClassLocationResolver.class);
	}

	public PluginLoaderRegistry getPluginLoaderRegistry() {
		return (PluginLoaderRegistry) getBean("pluginLoaderRegistry", PluginLoaderRegistry.class);
	}

	public PluginModificationCalculator getPluginModificationCalculator() {
		return (PluginModificationCalculator) getBean("pluginModificationCalculator",
				StrictPluginModificationCalculator.class);
	}

	public PluginStateManager getPluginStateManager() {
		return (PluginStateManager) getBean("pluginStateManager", PluginStateManager.class);
	}

	//FIXME add PluginModificationCalculatorRegistry
	//FIXME extract interface
	public PluginModificationCalculator getStickyPluginModificationCalculator() {
		return (PluginModificationCalculator) getBean("stickPluginModificationCalculator",
				StrictPluginModificationCalculator.class);
	}

	public TransitionProcessorRegistry getTransitionProcessorRegistry() {
		return (TransitionProcessorRegistry) getBean("transitionProcessorRegistry", TransitionProcessorRegistry.class);
	}

	public boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public String[] getAliases(String name) {
		return this.applicationContext.getAliases(name);
	}

	public Object getBean(String name) throws BeansException {
		return this.applicationContext.getBean(name);
	}

	public Object getBean(String name, Class requiredType) throws BeansException {
		return this.applicationContext.getBean(name, requiredType);
	}

	public Class getType(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.getType(name);
	}

	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isPrototype(name);
	}

	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isSingleton(name);
	}

	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
		return this.applicationContext.isTypeMatch(name, targetType);
	}

}
