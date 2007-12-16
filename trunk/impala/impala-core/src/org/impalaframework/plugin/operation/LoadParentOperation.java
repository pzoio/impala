package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class LoadParentOperation implements PluginOperation {
	
	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(ShutParentOperation.class);

	private final ImpalaBootstrapFactory factory;

	private final PluginSpecProvider pluginSpecBuilder;

	public LoadParentOperation(final ImpalaBootstrapFactory factory, final PluginSpecProvider pluginSpecBuilder) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginSpecBuilder);

		this.factory = factory;
		this.pluginSpecBuilder = pluginSpecBuilder;
	}

	public boolean execute() {
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		ParentSpec pluginSpec = pluginSpecBuilder.getPluginSpec();
		ParentSpec existingSpec = getExistingParentSpec(factory);
		
		// figure out the plugins to reload
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STRICT);
		PluginTransitionSet transitions = calculator.getTransitions(existingSpec, pluginSpec);
		pluginStateManager.processTransitions(transitions);
		return true;
	}

	protected ParentSpec getExistingParentSpec(ImpalaBootstrapFactory factory) {
		return null;
	}
}
