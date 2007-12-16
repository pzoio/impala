package org.impalaframework.plugin.loader;

import junit.framework.TestCase;

import org.impalaframework.classloader.FileSystemModuleClassLoader;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ParentPluginLoaderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private ParentPluginLoader pluginLoader;

	private PluginSpecProvider spec;

	public void setUp() {
		System.setProperty("impala.parent.project", "impala-core");
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		pluginLoader = new ParentPluginLoader(locationResolver);
		spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
	}

	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public final void testGetClassLocations() {
		final Resource[] classLocations = pluginLoader.getClassLocations(spec.getPluginSpec());
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}

	public final void testGetClassLoader() {
		final ClassLoader classLoader = pluginLoader.newClassLoader(spec.getPluginSpec(), null);
		assertTrue(classLoader instanceof FileSystemModuleClassLoader);
		assertTrue(classLoader.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));
	}

	public void testGetSpringLocations() {
		final ClassLoader classLoader = pluginLoader.newClassLoader(spec.getPluginSpec(), null);
		final Resource[] springConfigResources = pluginLoader.getSpringConfigResources(spec.getPluginSpec(),
				classLoader);

		assertEquals(1, springConfigResources.length);
		assertEquals(ClassPathResource.class, springConfigResources[0].getClass());
		assertTrue(springConfigResources[0].exists());

	}

}
