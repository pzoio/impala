package org.impalaframework.module.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractorRegistry;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.modification.StrictModuleModificationExtractor;
import org.impalaframework.module.web.BaseImpalaContextLoader;
import org.impalaframework.module.web.WebConstants;

public class BaseImpalaContextLoaderTest extends TestCase {

	private ServletContext servletContext;
	private ModuleManagementSource factory;
	private ModuleStateHolder moduleStateHolder;
	private ModuleModificationExtractorRegistry calculatorRegistry;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		factory = createMock(ModuleManagementSource.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);
		
		calculatorRegistry = new ModuleModificationExtractorRegistry();
		calculatorRegistry.addModificationCalculationType(ModificationExtractorType.STRICT, new StrictModuleModificationExtractor());
	}

	public final void testClose() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getPluginStateManager()).andReturn(moduleStateHolder);
		expect(factory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("parentSpec");
		expect(moduleStateHolder.getParentSpec()).andReturn(simpleRootModuleDefinition);
		moduleStateHolder.processTransitions(isA(ModuleTransitionSet.class));
		factory.close();

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}
	
	public final void testCloseParentNull() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getPluginStateManager()).andReturn(moduleStateHolder);
		expect(factory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		expect(moduleStateHolder.getParentSpec()).andReturn(null);
		servletContext.log("Closing Spring root WebApplicationContext");
		factory.close();

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}
	
	public final void testFactoryNull() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}

	private BaseImpalaContextLoader newContextLoader() {
		BaseImpalaContextLoader contextLoader = new BaseImpalaContextLoader() {
			@Override
			public ModuleDefinitionSource getPluginSpecBuilder(ServletContext servletContext) {
				return null;
			}
		};
		return contextLoader;
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(factory);
		verify(moduleStateHolder);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(factory);
		replay(moduleStateHolder);
	}
	

}
