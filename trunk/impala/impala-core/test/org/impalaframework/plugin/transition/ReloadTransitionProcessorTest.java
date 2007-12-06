package org.impalaframework.plugin.transition;

import static org.easymock.EasyMock.*;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;

import junit.framework.TestCase;

public class ReloadTransitionProcessorTest extends TestCase {

	private ReloadTransitionProcessor processor;

	private TransitionProcessor loadTransitionProcessor;

	private TransitionProcessor unloadTransitionProcessor;

	private PluginStateManager pluginStateManager;

	private ParentSpec p1;

	private ParentSpec p2;

	private PluginSpec plugin;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		processor = new ReloadTransitionProcessor();
		loadTransitionProcessor = createMock(TransitionProcessor.class);
		unloadTransitionProcessor = createMock(TransitionProcessor.class);
		processor.setLoadTransitionProcessor(loadTransitionProcessor);
		processor.setUnloadTransitionProcessor(unloadTransitionProcessor);
		pluginStateManager = createMock(PluginStateManager.class);

		p1 = new SimpleParentSpec("p1");
		p2 = new SimpleParentSpec("p1");
		plugin = new SimplePluginSpec(p2, "p3");
	}

	public final void testBothTrue() {

		expect(unloadTransitionProcessor.process(pluginStateManager, p1, p2, plugin)).andReturn(true);
		expect(loadTransitionProcessor.process(pluginStateManager, p1, p2, plugin)).andReturn(true);

		replayMocks();

		assertTrue(processor.process(pluginStateManager, p1, p2, plugin));

		verifyMocks();
	}

	public final void testUnloadFalse() {

		expect(unloadTransitionProcessor.process(pluginStateManager, p1, p2, plugin)).andReturn(false);

		replayMocks();

		assertFalse(processor.process(pluginStateManager, p1, p2, plugin));

		verifyMocks();
	}

	public final void testLoadFalse() {

		expect(unloadTransitionProcessor.process(pluginStateManager, p1, p2, plugin)).andReturn(true);
		expect(loadTransitionProcessor.process(pluginStateManager, p1, p2, plugin)).andReturn(false);

		replayMocks();

		assertFalse(processor.process(pluginStateManager, p1, p2, plugin));

		verifyMocks();
	}

	private void verifyMocks() {
		verify(loadTransitionProcessor);
		verify(unloadTransitionProcessor);
	}

	private void replayMocks() {
		replay(loadTransitionProcessor);
		replay(unloadTransitionProcessor);
	}

}
