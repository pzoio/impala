package org.impalaframework.module.transition;

import static org.impalaframework.module.transition.SharedSpecProviders.newTest1;
import static org.impalaframework.module.transition.SharedSpecProviders.newTest2;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.loader.ApplicationPluginLoader;
import org.impalaframework.module.loader.DefaultApplicationContextLoader;
import org.impalaframework.module.loader.ParentPluginLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.modification.PluginModificationCalculator;
import org.impalaframework.module.modification.PluginTransition;
import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.modification.StrictPluginModificationCalculator;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleTypes;
import org.impalaframework.module.transition.DefaultPluginStateManager;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ConfigurableApplicationContext;

public class PluginStateManagerTest extends TestCase {

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testProcessTransitions() {
		
		DefaultPluginStateManager tm = new DefaultPluginStateManager();
		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(ModuleTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationPluginLoader(resolver));
		DefaultApplicationContextLoader contextLoader = new DefaultApplicationContextLoader(registry);
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(contextLoader);
		UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		tm.setTransitionProcessorRegistry(transitionProcessors);		
		
		RootModuleDefinition test1Spec = newTest1().getPluginSpec();
		PluginModificationCalculator calculator = new StrictPluginModificationCalculator();
		PluginTransitionSet transitions = calculator.getTransitions(null, test1Spec);
		tm.processTransitions(transitions);

		ConfigurableApplicationContext context = tm.getParentContext();
		service((FileMonitor) context.getBean("bean1"));
		noService((FileMonitor) context.getBean("bean3"));

		RootModuleDefinition test2Spec = newTest2().getPluginSpec();
		transitions = calculator.getTransitions(test1Spec, test2Spec);
		tm.processTransitions(transitions);

		context = tm.getParentContext();
		service((FileMonitor) context.getBean("bean1"));
		//now we got bean3
		service((FileMonitor) context.getBean("bean3"));

	}

	private void noService(FileMonitor f) {
		try {
			f.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	private void service(FileMonitor f) {
		f.lastModified((File) null);
	}
	
}
