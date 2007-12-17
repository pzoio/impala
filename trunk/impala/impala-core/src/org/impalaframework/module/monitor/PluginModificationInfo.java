package org.impalaframework.module.monitor;

/**
 * @author Phil Zoio
 */
public class PluginModificationInfo {
	private String pluginName;

	public PluginModificationInfo(String pluginName) {
		super();
		this.pluginName = pluginName;
	}

	public String getPluginName() {
		return pluginName;
	}

	public String toString() {
		return this.getClass().getName() + ": " + pluginName;
	}
	
}
