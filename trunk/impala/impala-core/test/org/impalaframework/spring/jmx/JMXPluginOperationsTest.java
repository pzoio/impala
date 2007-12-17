package org.impalaframework.spring.jmx;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.modification.PluginModificationCalculator;
import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.transition.PluginStateManager;

public class JMXPluginOperationsTest extends TestCase {

	private PluginModificationCalculator pluginModificationCalculator;

	private PluginStateManager pluginStateManager;

	private JMXPluginOperations operations;

	private ParentSpec parentSpec;
	
	private PluginTransitionSet pluginModificationSet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		operations = new JMXPluginOperations();
		pluginModificationCalculator = createMock(PluginModificationCalculator.class);
		pluginStateManager = createMock(PluginStateManager.class);
		parentSpec = createMock(ParentSpec.class);
		pluginModificationSet = createMock(PluginTransitionSet.class);
		operations.setPluginModificationCalculator(pluginModificationCalculator);
		operations.setPluginStateManager(pluginStateManager);
	}

	public void testReload() {

		expect(pluginStateManager.getParentSpec()).andReturn(parentSpec);
		expect(pluginStateManager.cloneParentSpec()).andReturn(parentSpec);
		expect(parentSpec.findPlugin("someplugin", true)).andReturn(parentSpec);
		expect(pluginModificationCalculator.reload(parentSpec, parentSpec, "someplugin")).andReturn(pluginModificationSet);
		pluginStateManager.processTransitions(pluginModificationSet);
		
		replayMocks();

		assertEquals("Successfully reloaded someplugin", operations.reloadPlugin("someplugin"));

		verifyMocks();
	}
	
	public void testPluginNotFound() {

		expect(pluginStateManager.getParentSpec()).andReturn(parentSpec);
		expect(pluginStateManager.cloneParentSpec()).andReturn(parentSpec);
		expect(parentSpec.findPlugin("someplugin", true)).andReturn(null);
		
		replayMocks();

		assertEquals("Could not find plugin someplugin", operations.reloadPlugin("someplugin"));

		verifyMocks();
	}
	
	public void testThrowException() {

		expect(pluginStateManager.getParentSpec()).andReturn(parentSpec);
		expect(pluginStateManager.cloneParentSpec()).andReturn(parentSpec);
		expect(parentSpec.findPlugin("someplugin", true)).andReturn(parentSpec);
		expect(pluginModificationCalculator.reload(parentSpec, parentSpec, "someplugin")).andReturn(pluginModificationSet);
		pluginStateManager.processTransitions(pluginModificationSet);
		expectLastCall().andThrow(new IllegalStateException());
		
		replayMocks();

		assertTrue(operations.reloadPlugin("someplugin").contains("IllegalStateException"));

		verifyMocks();
	}

	private void replayMocks() {
		replay(parentSpec);
		replay(pluginModificationSet);
		replay(pluginModificationCalculator);
		replay(pluginStateManager);
	}

	private void verifyMocks() {
		verify(pluginModificationSet);
		verify(pluginModificationCalculator);
		verify(pluginStateManager);
		verify(parentSpec);
	}
}
