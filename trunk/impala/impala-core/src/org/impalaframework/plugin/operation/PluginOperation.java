package org.impalaframework.plugin.operation;

public interface PluginOperation {
	
	//FIXME take Map of arguments, and return boolean, Map of results, for partially
	//constrained interface
	
	public boolean execute();
}
