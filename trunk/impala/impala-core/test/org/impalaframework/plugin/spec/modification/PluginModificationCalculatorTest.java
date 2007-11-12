package org.impalaframework.plugin.spec.modification;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;

public class PluginModificationCalculatorTest extends TestCase {

	private PluginModificationCalculator calculator;

	public void setUp() {
		calculator = new PluginModificationCalculator();
	}
	
	public void testGetSimpleTransitions() {
		ParentSpec parentSpec = spec("app-context.xml", "plugin1, plugin2");
		PluginTransitionSet transitionsFromOriginal = calculator.getTransitions(parentSpec, null);
		assertSame(parentSpec, transitionsFromOriginal.getNewSpec());
		
		PluginTransitionSet transitionsToNew = calculator.getTransitions(null, parentSpec);
		assertSame(parentSpec, transitionsToNew.getNewSpec());
		
		PluginTransitionSet nullTransitions = calculator.getTransitions(null, null);
		assertNull(nullTransitions.getNewSpec());
	}
	
	public void testModifiedParent() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2");
		ParentSpec parentSpec2 = spec("app-context2.xml", "plugin1, plugin2");
		
		PluginTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(6, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange change1 = iterator.next();
		PluginStateChange change2 = iterator.next();
		PluginStateChange change3 = iterator.next();
		PluginStateChange change4 = iterator.next();
		PluginStateChange change5 = iterator.next();
		PluginStateChange change6 = iterator.next();
		
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin2", change2.getPluginSpec().getName());
		assertEquals("root-plugin", change3.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change3.getTransition());
		
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change4.getTransition());
		assertEquals("root-plugin", change4.getPluginSpec().getName());
		assertEquals("plugin1", change5.getPluginSpec().getName());
		assertEquals("plugin2", change6.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change6.getTransition());
	}	

	private ParentSpec spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleParentSpec(locations),
				pluginString);
		ParentSpec parentSpec = builder.getParentSpec();
		return parentSpec;
	}

}
