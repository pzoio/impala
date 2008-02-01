/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.facade;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.util.InstantiationUtils;
import org.springframework.context.ApplicationContext;

/**
 * Provides a facade for loading modules, reloading modules, adding module
 * definitions, removing modules and shutting down modules. Also contains
 * methods which can be used to inspect module state. Finally, provides access
 * to Spring beans, both those defined at the root module level as well as those
 * defined in lower level modules.
 * @author Phil Zoio
 */
public class DynamicContextHolder {

	private static InternalOperationsFacade facade;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	/**
	 * This method is responsible for initializing the module loading facade.
	 * The default behaviour is to create an instance of
	 * <code>InteractiveOperationsFacade</code>. This default can be overridden
	 * by setting the system property FacadeConstants.facade.class.name. The
	 * class name must be an instance of <code>InternalOperationsFacade</code>.
	 */
	public static void init() {
		String facadeClassName = System.getProperty(FacadeConstants.FACADE_CLASS_NAME);

		if (facadeClassName == null) {
			facadeClassName = InteractiveOperationsFacade.class.getName();
		}

		if (facade == null) {
			facade = InstantiationUtils.instantiate(facadeClassName);
		}
	}

	/**
	 * Creates a module hierarchy using the provided
	 * <code>ModuleDefinitionSource</code>.
	 * @param source
	 */
	public static void init(ModuleDefinitionSource source) {
		init();
		getFacade().init(source);
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */

	/**
	 * Reloads the named module. No changes are assumed to have taken place to
	 * the module definition itself - the <code>ModuleDefinition</code> loaded
	 * using the <code>init(ModuleDefinitionSource)</code> is used.
	 * @see #init(ModuleDefinitionSource)
	 * @param the name of the module to reload
	 */
	public static boolean reload(String moduleName) {
		return getFacade().reload(moduleName);
	}

	/**
	 * This is a convenience method which is used mainly by the interactive
	 * client during testing. Renames a module whose name at least partially
	 * matches the moduleName provided. The match is based on a substring
	 * comparison. For example, <i>myModule</i> will be matched by the string
	 * <i>my</i>. Note that if there is more than one matching module, the
	 * first match found will be loaded
	 * @param moduleName the module name, or a substring thereof
	 * @return the name of the actual module reloaded, or <code>null</code> if
	 * none is found
	 */
	public static String reloadLike(String moduleName) {
		return getFacade().reloadLike(moduleName);
	}

	/**
	 * Reloads the entire module hierarchy. No changes are assumed to have taken
	 * place to the module definition itself - the <code>ModuleDefinition</code>
	 * loaded using the <code>init(ModuleDefinitionSource)</code> is used for
	 * the reload operation.
	 * @see #init(ModuleDefinitionSource)
	 */
	public static void reloadRootModule() {
		getFacade().reloadRootModule();
	}

	/**
	 * Unloads the entire module hierarchy. This will result in any
	 * <code>ApplicationContexts</code> associated with any modules to be
	 * closed. Note that this operation also discards the
	 * <code>ModuleDefinition</code> originally loaded using loaded using the
	 * <code>init(ModuleDefinitionSource)</code>
	 */
	public static void unloadRootModule() {
		getFacade().unloadRootModule();
	}

	/**
	 * Removes the named module from the module hierarchy. Will also close the
	 * <code>ApplicationContext</code> from the module hierarchy. The named
	 * module will no longer be contained in the modified module definition. In
	 * other words, <code>{@link #hasModule(String)}</code> will return false
	 * for this <code>moduleName</code>.
	 * @param moduleName the namme of the module to remove.
	 * @return true if the module was removed, false if it wasn't there in the
	 * first place.
	 * @see #hasModule(String)
	 */
	public static boolean remove(String moduleName) {
		return getFacade().remove(moduleName);
	}

	/**
	 * Adds the supplied module to the module hierarchy. Note that the supplied
	 * <code>moduleDefinition</code> needs to have a reference to its parent,
	 * to determine what point in the hierarchy the module should be added. This
	 * operation results in the supplied module definition added to the
	 * appropriate point in the module definition hierarchy. It also results in
	 * the instantiation of an <code>ApplicationContext</code> representing
	 * the module.
	 * @param moduleDefinition the <code>ModuleDefinition</code> to be added
	 */
	public static void addModule(final ModuleDefinition moduleDefinition) {
		getFacade().addModule(moduleDefinition);
	}

	/* **************************** read-only methods ************************** */

	/**
	 * Determines whether the module hierarchy contains a module of the given
	 * name
	 * @param moduleName the name of the module to match
	 */
	public static boolean hasModule(String moduleName) {
		return getFacade().hasModule(moduleName);
	}

	/**
	 * Determines whether the module hierarchy contains a module matching the
	 * supplied <code>moduleName</code>. Matching is done on the basis of a
	 * substring. For example the string <i>my</i> will match the module
	 * <i>myModule</i>.
	 * @param moduleName the string of the module to match
	 * @return the name of the actual module matched, or null if none can be
	 * found
	 */
	public static String findLike(String moduleName) {
		return getFacade().findLike(moduleName);
	}

	/**
	 * Returns the <code>ApplicationContext</code> representing the root
	 * module in the module hierarchy. Will not return <code>null</code>, but
	 * will instead throw a <code>NoServiceException</code> if the root module
	 * <code>ApplicationContext</code> has not been loaded.
	 * @return the <code>ApplicationContext</code> representing the root
	 * module in the hierarchy
	 * @throws <code>NoServiceException</code> if no root module has been
	 * loaded.
	 */
	public static ApplicationContext get() {
		return getFacade().getRootContext();
	}

	/**
	 * Gets the named bean from the root module <code>ApplicationContext</code>.
	 * @param <T> the type of the named bean to return
	 * @param beanName the name of the bean
	 * @param type the class of the named bean to return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanName, Class<T> type) {
		return getFacade().getBean(beanName, type);
	}

	/**
	 * Returns a named bean from a module which typically would not be the root
	 * module. Note that this method should be used with care, as the type of
	 * the bean may not be visible to the class loader running the test.
	 * @param <T> the type of the named bean to return
	 * @param moduleName the namme of the module from which to return the bean
	 * @param beanName the bean name to return
	 * @param type the class of the named bean to return
	 * @return a bean instance returned from the named module
	 */
	// FIXME should this be untyped
	// FIXME make class loader of test project capable of seeing the returned
	// type
	@SuppressWarnings("unchecked")
	public static <T extends Object> T getPluginBean(String moduleName, String beanName, Class<T> type) {
		return getFacade().getPluginBean(moduleName, beanName, type);
	}

	/**
	 * Returns the currently held <code>RootModuleDefinition</code>. This
	 * will have been loaded from the <code>ModuleDefinitionSource</code>
	 * provided to the {@link #init(ModuleDefinitionSource)} method.
	 * @return the <code>RootModuleDefinition</code> currently held by the
	 * module management system.
	 */
	public static RootModuleDefinition getRootModuleDefinition() {
		return getFacade().getRootModuleDefinition();
	}

	/* ****************** package level methods ***************** */

	static ApplicationContext getModule(String moduleName) {
		return getFacade().getModule(moduleName);
	}

	static InternalOperationsFacade getFacade() {
		if (facade == null) {
			throw new NoServiceException("The application has not been initialised. Has "
					+ DynamicContextHolder.class.getSimpleName() + ".init("
					+ ModuleDefinitionSource.class.getSimpleName() + ") been called?");
		}
		return facade;
	}

	public static void clear() {
		if (facade != null) {
			facade.unloadRootModule();
		}
		facade = null;
	}

}
