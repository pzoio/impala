package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseRootModuleOperation extends BaseModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(CloseRootModuleOperation.class);

	protected CloseRootModuleOperation(final ModuleManagementFactory factory) {
		super(factory);
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		ModuleStateHolder moduleStateHolder = getFactory().getModuleStateHolder();
		ModificationExtractor calculator = getFactory().getModificationExtractorRegistry()
				.getModificationExtractor(ModificationExtractorType.STRICT);
		RootModuleDefinition rootModuleDefinition = moduleStateHolder.getRootModuleDefinition();
		
		if (rootModuleDefinition != null) {
			logger.info("Shutting down application context");
			TransitionSet transitions = calculator.getTransitions(rootModuleDefinition, null);
			moduleStateHolder.processTransitions(transitions);
			return ModuleOperationResult.TRUE;
		}
		else {
			return ModuleOperationResult.FALSE;
		}
	}

}
