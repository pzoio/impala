package org.impalaframework.module.transition;

import org.impalaframework.module.builder.SimplePluginSpecBuilder;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.spec.SimpleModuleDefinition;

public class SharedSpecProviders {

	static final String plugin1 = "impala-sample-dynamic-plugin1";

	static final String plugin2 = "impala-sample-dynamic-plugin2";

	static final String plugin3 = "impala-sample-dynamic-plugin3";

	static ModuleDefinitionSource newTest1() {
		return new Test1();
	}

	static ModuleDefinitionSource newTest1a() {
		return new Test1a();
	}

	static ModuleDefinitionSource newTest2() {
		return new Test2();
	}

	static class Test1 implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test1() {
		}

		public RootModuleDefinition getPluginSpec() {
			return spec.getPluginSpec();
		}
	}

	static class Test1a implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimplePluginSpecBuilder(new String[] { "parentTestContext.xml",
				"extra-context.xml" }, new String[] { plugin1, plugin2 });

		public Test1a() {
		}

		public RootModuleDefinition getPluginSpec() {
			return spec.getPluginSpec();
		}
	}

	static class Test2 implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test2() {

			ModuleDefinition p2 = spec.getPluginSpec().getPlugin(plugin2);
			new SimpleModuleDefinition(p2, plugin3);
		}

		public RootModuleDefinition getPluginSpec() {
			return spec.getPluginSpec();
		}
	}

}
