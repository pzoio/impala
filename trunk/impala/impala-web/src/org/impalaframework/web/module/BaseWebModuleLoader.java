package org.impalaframework.web.module;

import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.BaseModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class BaseWebModuleLoader extends BaseModuleLoader implements ServletContextAware {

	private ServletContext servletContext;

	private ModuleLocationResolver moduleLocationResolver;

	public BaseWebModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public BaseWebModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext cannot be null");
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
		this.servletContext = servletContext;
	}

	public GenericWebApplicationContext newApplicationContext(ApplicationContext parent,
			ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
		context.setParent(parent);
		context.setServletContext(servletContext);
		context.setClassLoader(classLoader);

		return context;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		// FIXME test
		Resource[] moduleClassLocations = getClassLocations(moduleDefinition);
		ClassLoader classLoader = null;
		if (parent != null) {
			classLoader = parent.getClassLoader();
		}
		else {
			classLoader = ClassUtils.getDefaultClassLoader();
		}
		return new ModuleClassLoader(classLoader, ResourceUtils.getFiles(moduleClassLocations));
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<Resource> moduleClassLocations = moduleLocationResolver
				.getApplicationModuleClassLocations(moduleDefinition.getName());
		return ResourceUtils.toArray(moduleClassLocations);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	protected ModuleLocationResolver getClassLocationResolver() {
		return moduleLocationResolver;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

}
