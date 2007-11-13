package org.impalaframework.plugin.spec.transition;

import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.SimplePluginSpec;

public class SharedSpecProviders {

	static final String plugin1 = "impala-sample-dynamic-plugin1";

	static final String plugin2 = "impala-sample-dynamic-plugin2";

	static final String plugin3 = "impala-sample-dynamic-plugin3";

	static PluginSpecProvider newTest1() {
		return new Test1();
	}

	static PluginSpecProvider newTest2() {
		return new Test2();
	}

	static class Test1 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test1() {
		}

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}
	}

	static class Test2 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test2() {

			PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
			new SimplePluginSpec(p2, plugin3);
		}

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}
	}

}
