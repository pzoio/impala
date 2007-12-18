package org.impalaframework.module.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleModificationExtractorRegistry;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeInfo;
import org.impalaframework.module.web.WebConstants;
import org.impalaframework.module.web.WebPluginModificationListener;

public class WebPluginModificationListenerTest extends TestCase {

	public final void testEmpty() throws Exception {

		ServletContext servletContext = createMock(ServletContext.class);

		final WebPluginModificationListener listener = new WebPluginModificationListener(servletContext);

		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();
		ModuleChangeEvent event = new ModuleChangeEvent(info);

		replay(servletContext);

		listener.pluginModified(event);

		verify(servletContext);

		info.add(new ModuleChangeInfo("p1"));
		info.add(new ModuleChangeInfo("p2"));
		info.add(new ModuleChangeInfo("p2"));

		event = new ModuleChangeEvent(info);

	}

	public final void testNonEmpty() throws Exception {


		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();

		info.add(new ModuleChangeInfo("p1"));

		ModuleChangeEvent event = new ModuleChangeEvent(info);

		ServletContext servletContext = createMock(ServletContext.class);
		ModuleManagementSource bootstrapFactory = createMock(ModuleManagementSource.class);
		ModuleStateHolder moduleStateHolder = createMock(ModuleStateHolder.class);

		final WebPluginModificationListener listener = new WebPluginModificationListener(servletContext);
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("p1");
		
		ModuleModificationExtractor modificationCalculator = createMock(ModuleModificationExtractor.class);
		ModuleModificationExtractorRegistry registry = new ModuleModificationExtractorRegistry();
		registry.addModificationCalculationType(ModificationExtractorType.STRICT, modificationCalculator);

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		ModuleTransitionSet transitionSet = new ModuleTransitionSet(transitions, simpleRootModuleDefinition);
		
		// set expectations
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(bootstrapFactory);
		expect(bootstrapFactory.getPluginStateManager()).andReturn(moduleStateHolder);
		expect(moduleStateHolder.getParentSpec()).andReturn(simpleRootModuleDefinition);
		expect(bootstrapFactory.getPluginStateManager()).andReturn(moduleStateHolder);
		expect(moduleStateHolder.cloneParentSpec()).andReturn(simpleRootModuleDefinition);
		expect(bootstrapFactory.getPluginModificationCalculatorRegistry()).andReturn(registry);
		expect(modificationCalculator.reload(simpleRootModuleDefinition, simpleRootModuleDefinition, "p1")).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);

		replay(servletContext);
		replay(bootstrapFactory);
		replay(moduleStateHolder);
		replay(modificationCalculator);

		listener.pluginModified(event);

		verify(servletContext);
		verify(bootstrapFactory);
		verify(moduleStateHolder);
		verify(modificationCalculator);

	}
}
