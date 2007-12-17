package org.impalaframework.module.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.SimpleModuleDefinition;
import org.springframework.util.Assert;

public class WebRootPluginSpec extends SimpleModuleDefinition {

	private static final long serialVersionUID = 1L;

	private List<String> contextLocations;

	@Override
	public String getType() {
		return WebPluginTypes.WEB_ROOT;
	}

	public WebRootPluginSpec(ModuleDefinition moduleDefinition, String name, String[] contextLocations) {
		super(moduleDefinition, name);
		Assert.notEmpty(contextLocations);

		this.contextLocations = new ArrayList<String>();
		for (int i = 0; i < contextLocations.length; i++) {
			this.contextLocations.add(contextLocations[i]);
		}
	}
	
	public WebRootPluginSpec(ModuleDefinition moduleDefinition, String name, List<String> contextLocations) {
		super(moduleDefinition, name);
		Assert.notEmpty(contextLocations);

		this.contextLocations = new ArrayList<String>(contextLocations);
	}

	public List<String> getContextLocations() {
		return Collections.unmodifiableList(contextLocations);
	}

}
