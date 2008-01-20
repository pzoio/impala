package org.impalaframework.module.holder;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;

public class SharedModuleDefinitionSources {

	public static final String plugin1 = "impala-sample-dynamic-plugin1";

	public static final String plugin2 = "impala-sample-dynamic-plugin2";

	public static final String plugin3 = "impala-sample-dynamic-plugin3";

	public static ModuleDefinitionSource newTest1() {
		return new Test1();
	}

	public static ModuleDefinitionSource newTest1a() {
		return new Test1a();
	}

	public static ModuleDefinitionSource newTest2() {
		return new Test2();
	}

	public static class Test1 implements ModuleDefinitionSource {
		ModuleDefinitionSource definitionSource = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test1() {
		}

		public RootModuleDefinition getModuleDefinition() {
			return definitionSource.getModuleDefinition();
		}
	}

	public static class Test1a implements ModuleDefinitionSource {
		ModuleDefinitionSource definitionSource = new SimpleModuleDefinitionSource(new String[] { "parentTestContext.xml",
				"extra-context.xml" }, new String[] { plugin1, plugin2 });

		public Test1a() {
		}

		public RootModuleDefinition getModuleDefinition() {
			return definitionSource.getModuleDefinition();
		}
	}

	static class Test2 implements ModuleDefinitionSource {
		ModuleDefinitionSource definitionSource = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test2() {

			ModuleDefinition p2 = definitionSource.getModuleDefinition().getModule(plugin2);
			new SimpleModuleDefinition(p2, plugin3);
		}

		public RootModuleDefinition getModuleDefinition() {
			return definitionSource.getModuleDefinition();
		}
	}

}
