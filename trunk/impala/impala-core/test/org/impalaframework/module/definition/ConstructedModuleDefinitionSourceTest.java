package org.impalaframework.module.definition;

import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

import junit.framework.TestCase;

public class ConstructedModuleDefinitionSourceTest extends TestCase {

	public final void testConstructedModuleDefinitionSource() {
		ConstructedModuleDefinitionSource provider = new ConstructedModuleDefinitionSource(null);
		assertNull(provider.getModuleDefinition());

		SimpleRootModuleDefinition spec = new SimpleRootModuleDefinition("p");
		provider = new ConstructedModuleDefinitionSource(spec);
		assertSame(spec, provider.getModuleDefinition());
	}

}
