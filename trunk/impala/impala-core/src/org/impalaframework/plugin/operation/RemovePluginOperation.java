package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RemovePluginOperation implements PluginOperation {

	final Logger logger = LoggerFactory.getLogger(RemovePluginOperation.class);

	private final ImpalaBootstrapFactory factory;

	private final String pluginToRemove;

	public RemovePluginOperation(final ImpalaBootstrapFactory factory, final String pluginToRemove) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToRemove);
		this.factory = factory;
		this.pluginToRemove = pluginToRemove;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationCalculationType.STICKY);
		removePlugin(pluginStateManager, calculator, pluginToRemove);
		return true;
	}
	
	public static boolean removePlugin(PluginStateManager pluginStateManager, PluginModificationCalculator calculator,
			String plugin) {

		//FIXME test

		ParentSpec oldSpec = pluginStateManager.getParentSpec();
		
		if (oldSpec == null) {
			return false;
		}
		
		ParentSpec newSpec = pluginStateManager.cloneParentSpec();
		PluginSpec pluginToRemove = newSpec.findPlugin(plugin, true);

		if (pluginToRemove != null) {
			if (pluginToRemove instanceof ParentSpec) {
				//we're removing the parent context, so new pluginSpec is null
				PluginTransitionSet transitions = calculator.getTransitions(oldSpec, null);
				pluginStateManager.processTransitions(transitions);
				return true;
			}
			else {
				PluginSpec parent = pluginToRemove.getParent();
				if (parent != null) {
					parent.remove(plugin);
					pluginToRemove.setParent(null);

					PluginTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
					pluginStateManager.processTransitions(transitions);
					return true;
				}
				else {
					throw new IllegalStateException("Plugin to remove does not have a parent plugin. "
							+ "This is unexpected state and may indicate a bug");
				}
			}
		}
		return false;
	}

	
}
