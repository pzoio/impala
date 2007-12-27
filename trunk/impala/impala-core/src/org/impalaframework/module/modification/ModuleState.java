package org.impalaframework.module.modification;

public enum ModuleState {

	LOADED("LOADED"), UNLOADED("UNLOADED"), STALE("STALE");

	private String name;

	private ModuleState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
