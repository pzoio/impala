package org.impalaframework.plugin.bootstrap;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootstrapBeanFactoryTest extends TestCase {

	public final void testBootstrapBeanFactory() {
		BootstrapBeanFactory factory = new BootstrapBeanFactory(new ClassPathXmlApplicationContext(
				"META-INF/impala-bootstrap.xml"));

		assertNotNull(factory.getApplicationContextLoader());
		assertNotNull(factory.getClassLocationResolver());
		assertNotNull(factory.getPluginLoaderRegistry());
		assertNotNull(factory.getPluginModificationCalculatorRegistry());
		assertNotNull(factory.getPluginStateManager());
		assertNotNull(factory.getTransitionProcessorRegistry());
		assertNotNull(factory.getClassLocationResolver());
	}

}
