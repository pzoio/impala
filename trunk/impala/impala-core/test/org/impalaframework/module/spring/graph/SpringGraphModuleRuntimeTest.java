package org.impalaframework.module.spring.graph;

import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.facade.GraphOperationFacade;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.spring.SpringModuleUtils;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.springframework.context.ApplicationContext;

public class SpringGraphModuleRuntimeTest extends TestCase implements ModuleDefinitionSource {

	public void setUp() {
		Impala.clear();
		System.setProperty(FacadeConstants.FACADE_CLASS_NAME, GraphOperationFacade.class.getName());
		Impala.init();
	}

	public void tearDown() {
		try {
			Impala.clear();
		}
		catch (Exception e) {
		}
		System.clearProperty(FacadeConstants.FACADE_CLASS_NAME);
	}

	public void testGraph() throws Exception {
	
		System.out.println(getModuleDefinition());
		
		Impala.init(this);
		ModuleStateHolder moduleStateHolder = Impala.getFacade().getModuleManagementFacade().getModuleStateHolder();
		
		SpringGraphModuleRuntime runtime = new SpringGraphModuleRuntime();
		runtime.setModuleStateHolder(moduleStateHolder);

		checkExpected(moduleStateHolder, runtime, "sample-module4", 0);
		checkExpected(moduleStateHolder, runtime, "sample-module6", 3);
		checkExpected(moduleStateHolder, runtime, "sample-module5", 0);
		checkExpected(moduleStateHolder, runtime, "sample-module2", 0);
		checkExpected(moduleStateHolder, runtime, "impala-core", 0);
		
	}

	private void checkExpected(ModuleStateHolder moduleStateHolder,
			SpringGraphModuleRuntime runtime, String moduleName, int expected) {
		ModuleDefinition definition = moduleStateHolder.getModuleDefinition().findChildDefinition(moduleName, true);
		
		RuntimeModule parentModule = null;
		if (definition.getParentDefinition() != null) parentModule = moduleStateHolder.getModule(definition.getParentDefinition().getName());
		
		ApplicationContext parent = SpringModuleUtils.getModuleSpringContext(parentModule);
		
		List<ApplicationContext> dependentContextsNotParents = runtime.getNonAncestorDependentContext(definition, parent, (GraphModuleStateHolder)moduleStateHolder);
		assertEquals(expected, dependentContextsNotParents.size());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new InternalModuleDefinitionSource(TypeReaderRegistryFactory.getTypeReaderRegistry(), 
				Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
				new String[] { "impala-core", "sample-module4",	"sample-module5", "sample-module6" }).getModuleDefinition();
	}
}
