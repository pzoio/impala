package org.impalaframework.plugin.bootstrap;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.impalaframework.plugin.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.BeanFactory;

public interface ImpalaBootstrapFactory extends BeanFactory {
	
	ClassLocationResolver getClassLocationResolver();
	PluginLoaderRegistry getPluginLoaderRegistry();
	ApplicationContextLoader getApplicationContextLoader();
	PluginModificationCalculatorRegistry getPluginModificationCalculatorRegistry();
	//PluginModificationCalculator getPluginModificationCalculator();
	//PluginModificationCalculator getStickyPluginModificationCalculator();
	TransitionProcessorRegistry getTransitionProcessorRegistry();
	PluginStateManager getPluginStateManager();
	void close();
	
}
