package org.impalaframework.plugin.spec.transition;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public interface TransitionProcessor {
	public void process(DefaultPluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec, PluginSpec plugin);
}
