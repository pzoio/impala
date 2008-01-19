package org.impalaframework.module.operation;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class AddModuleOperation extends BaseModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(AddModuleOperation.class);

	protected AddModuleOperation() {
		super();
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		ModuleDefinition moduleToAdd = moduleOperationInput.getModuleDefinition();
		Assert.notNull(moduleToAdd, "moduleName is required as it specifies the name of the module to add in " + this.getClass().getName());
		
		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		ModificationExtractor calculator = getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STICKY);
		
		addModule(moduleStateHolder, calculator, moduleToAdd);
		return new ModuleOperationResult(true);
	}
	
	protected void addModule(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
			ModuleDefinition moduleDefinition) {

		RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModuleDefinition parent = moduleDefinition.getParentDefinition();
		
		if (moduleDefinition instanceof RootModuleDefinition) {
			newRootDefinition = (RootModuleDefinition) moduleDefinition;
		}
		else {

			ModuleDefinition newParent = null;

			if (parent == null) {
				newParent = newRootDefinition;
			}
			else {
				String parentName = parent.getName();
				newParent = newRootDefinition.findChildDefinition(parentName, true);

				if (newParent == null) {
					throw new InvalidStateException("Unable to find parent module '" + parentName + "' in " + newRootDefinition);
				}
			}

			newParent.add(moduleDefinition);
			moduleDefinition.setParentDefinition(newParent);
		}

		TransitionSet transitions = calculator.getTransitions(oldRootDefinition, newRootDefinition);
		moduleStateHolder.processTransitions(transitions);
	}	
	
}
