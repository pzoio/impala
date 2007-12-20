package org.impalaframework.web.module;

import java.io.IOException;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
import org.impalaframework.web.module.ServletModuleLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ServletModuleLoaderTest extends TestCase {

	public final void testGetSpringConfigResources() throws IOException {
		ServletModuleLoader pluginLoader = new ServletModuleLoader(new PropertyModuleLocationResolver());
		Resource[] springConfigResources = pluginLoader.getSpringConfigResources(new SimpleModuleDefinition("myplugin"), ClassUtils.getDefaultClassLoader());
	
		assertEquals(1, springConfigResources.length);
		Resource resource = springConfigResources[0];
		assertTrue(resource.getURL().toString().endsWith("myplugin/spring/myplugin-context.xml"));
	}

}