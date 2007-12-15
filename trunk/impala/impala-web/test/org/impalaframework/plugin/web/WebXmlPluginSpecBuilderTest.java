package org.impalaframework.plugin.web;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.springframework.core.io.ClassPathResource;

public class WebXmlPluginSpecBuilderTest extends TestCase {

	public final void testCreatePluginSpec() {
		WebXmlPluginSpecBuilder builder = new WebXmlPluginSpecBuilder();
		builder.setResource(new ClassPathResource("xmlspec/webspec.xml"));
		ParentSpec actual = builder.getParentSpec();
		assertEquals(4, actual.getPlugins().size());
		
		ParentSpec expected = new SimpleParentSpec(new String[] { "parentTestContext.xml" });
		assertEquals(expected, actual);
		
		PluginSpec spec1 = new SimplePluginSpec(expected, "plugin1");
		PluginSpec spec2 = new WebRootPluginSpec(expected, "servlet1", new String[]{"location1", "location2"});
		PluginSpec spec3 = new ServletPluginSpec(expected, "servlet2", new String[]{"location3", "location4"});
		PluginSpec spec4 = new WebPlaceholderPluginSpec(expected, "servlet3");
		
		assertEquals(spec1, actual.findPlugin("plugin1", true));
		assertEquals(spec2, actual.findPlugin("servlet1", true));
		assertEquals(spec3, actual.findPlugin("servlet2", true));
		assertEquals(spec4, actual.findPlugin("servlet3", true));
	}

}
