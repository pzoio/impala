package org.impalaframework.module.builder;

import java.util.Arrays;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;

public class InternalXmlModuleDefinitionSourceTest extends TestCase {

	private InternalXmlModuleDefinitionSource moduleDefinitionSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleDefinitionSource = new InternalXmlModuleDefinitionSource(new StandaloneModuleLocationResolver());
	}
	
	public void testGetModuleDefinition() {
		moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/moduledefinition.xml"));
		RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		
		assertEquals(Arrays.asList(new String[]{"parentTestContext.xml"}), moduleDefinition.getContextLocations());
		assertEquals(2, moduleDefinition.getChildDefinitions().size());
		assertEquals(Arrays.asList(new String[]{"impala-core"}), moduleDefinition.getRootProjectNames());
		
		ModuleDefinition definition1 = getDefinition(moduleDefinition, "sample-module1");
		assertEquals(ModuleTypes.APPLICATION, definition1.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module1-context.xml"}), definition1.getContextLocations());
		
		ModuleDefinition definition2 = getDefinition(moduleDefinition, "sample-module2");
		assertEquals(ModuleTypes.APPLICATION, definition2.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module2-context.xml"}), definition2.getContextLocations());
		
		ModuleDefinition definition3 = getDefinition(definition2, "sample-module3");
		assertEquals(ModuleTypes.APPLICATION, definition3.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module3-context.xml"}), definition3.getContextLocations());
		
		ModuleDefinition definition4 = getDefinition(definition2, "sample-module4");
		assertEquals(ModuleTypes.APPLICATION_WITH_BEANSETS, definition4.getType());
		assertEquals(Arrays.asList(new String[]{"sample-module4-context.xml"}), definition4.getContextLocations());
	}

	public void testNoNames() throws Exception {
		try {
			moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/nonames.xml"));
			moduleDefinitionSource.getModuleDefinition();
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Resource 'class path resource [xmlinternal/nonames.xml]' contains a non-empty 'names' element, which is illegal when using InternalModuleDefinitionSource", e.getMessage());
		}
	}
	
	private ModuleDefinition getDefinition(ModuleDefinition moduleDefinition,
			String moduleName) {
		ModuleDefinition def = moduleDefinition.getModule(moduleName);
		assertNotNull(def);
		return def;
	}

}
