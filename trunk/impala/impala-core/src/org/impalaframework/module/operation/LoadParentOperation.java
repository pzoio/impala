package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class LoadParentOperation implements PluginOperation {
	
	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(ShutParentOperation.class);

	private final ModuleManagementSource factory;

	private final ModuleDefinitionSource pluginSpecBuilder;

	public LoadParentOperation(final ModuleManagementSource factory, final ModuleDefinitionSource pluginSpecBuilder) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginSpecBuilder);

		this.factory = factory;
		this.pluginSpecBuilder = pluginSpecBuilder;
	}

	public boolean execute() {
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		RootModuleDefinition pluginSpec = pluginSpecBuilder.getModuleDefintion();
		RootModuleDefinition existingSpec = getExistingParentSpec(factory);
		
		ModificationCalculationType modificationCalculationType = getPluginModificationType();
		// figure out the plugins to reload
		ModuleModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(modificationCalculationType);
		ModuleTransitionSet transitions = calculator.getTransitions(existingSpec, pluginSpec);
		pluginStateManager.processTransitions(transitions);
		return true;
	}

	protected ModificationCalculationType getPluginModificationType() {
		return ModificationCalculationType.STRICT;
	}

	protected RootModuleDefinition getExistingParentSpec(ModuleManagementSource factory) {
		return null;
	}
}
