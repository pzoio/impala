package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public abstract class BasePluginLoader implements PluginLoader {
	
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec plugin) {
		final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		return new XmlBeanDefinitionReader(PluginUtils.castToBeanDefinitionRegistry(beanFactory));
	}

	public void afterRefresh(ConfigurableApplicationContext context, PluginSpec plugin) {
	}
}
