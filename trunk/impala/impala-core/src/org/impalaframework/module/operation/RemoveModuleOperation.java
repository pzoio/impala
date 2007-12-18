package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateManager;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RemoveModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(RemoveModuleOperation.class);

	private final ModuleManagementSource factory;

	private final String pluginToRemove;

	public RemoveModuleOperation(final ModuleManagementSource factory, final String pluginToRemove) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToRemove);
		this.factory = factory;
		this.pluginToRemove = pluginToRemove;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		ModuleStateManager moduleStateManager = factory.getPluginStateManager();
		ModuleModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationExtractorType.STRICT);
		return removePlugin(moduleStateManager, calculator, pluginToRemove);
	}
	
	public static boolean removePlugin(ModuleStateManager moduleStateManager, ModuleModificationExtractor calculator,
			String plugin) {
		
		RootModuleDefinition oldSpec = moduleStateManager.getParentSpec();
		
		if (oldSpec == null) {
			return false;
		}
		
		RootModuleDefinition newSpec = moduleStateManager.cloneParentSpec();
		ModuleDefinition pluginToRemove = newSpec.findPlugin(plugin, true);

		if (pluginToRemove != null) {
			if (pluginToRemove instanceof RootModuleDefinition) {
				//we're removing the parent context, so new pluginSpec is null
				ModuleTransitionSet transitions = calculator.getTransitions(oldSpec, null);
				moduleStateManager.processTransitions(transitions);
				return true;
			}
			else {
				ModuleDefinition parent = pluginToRemove.getParent();
				if (parent != null) {
					ModuleDefinition remove = parent.remove(plugin);
					System.out.println("Removed: " + remove.getName());
					
					pluginToRemove.setParent(null);

					ModuleTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
					moduleStateManager.processTransitions(transitions);
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
