package org.impalaframework.module.loader;

import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;


/**
 * @author Phil Zoio
 */
public class SystemParentPluginLoader extends ParentPluginLoader implements PluginLoader {

	public SystemParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		return ClassUtils.getDefaultClassLoader();
	}
	
}
