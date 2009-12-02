/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.servlet.wrapper;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.ClassLoaderUtils;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.PropertySourceHolder;
import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
import org.impalaframework.web.bootstrap.WebBootstrapProperties;
import org.springframework.util.Assert;

/**
 * Implementation of {@link HttpSession} which attempts to protect session attribute values 
 * across module reloads through special implementation of {@link #getAttribute(String)}
 * @author Phil Zoio
 */
public class StateProtectingWrapperHttpSession extends DelegatingWrapperHttpSession {
    
    private static final Log logger = LogFactory.getLog(StateProtectingWrapperHttpSession.class);

    private final ClassLoader moduleClassLoader;

    public StateProtectingWrapperHttpSession(HttpSession realSession,
            ClassLoader moduleClassLoader) {
        super(realSession);
        Assert.notNull(moduleClassLoader);
        this.moduleClassLoader = moduleClassLoader;
    }

    /**
     * {@link HttpSession#getAttribute(String)} detects attempts to access items from session
     * using an "old" class loader. In this case, instances which can be
     * serialized can be cloned and "read-in" using the new module's class
	 * loader, and "recovered" in this way. Non-serializable class instances
	 * cannot be recovered. In this case, the session attribute is discarded and
	 * a message is logged.
	 */
	@Override
	public Object getAttribute(String name) {
		
		Object attribute = super.getAttribute(name);
		
		if (attribute == null) 
			return null;
		
		ClassLoader attributeClassLoader = attribute.getClass().getClassLoader();
		if (!ClassLoaderUtils.isVisibleFrom(attributeClassLoader, moduleClassLoader)) {
			
			if (!(attribute instanceof Serializable)) {
				logger.warn("Object in session under key [" + name + "] is not compatible with the current class loader, and cannot be recovered because it does not implement " + Serializable.class.getName() + ". Attribute will be removed from the session");
				this.removeAttribute(name);
				return null;
			}
			
			SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(moduleClassLoader));
			
			Object clonedAttribute = null;
			try {
				clonedAttribute = clone(attribute, helper);
			} catch (RuntimeException e) {
				handleReloadFailure(name);
				return null;
			}
			
			//explicitly set as the session attribute object has changed
			this.setAttribute(name, clonedAttribute);
			return clonedAttribute;
		}
		
		return attribute;
	}

	protected void handleReloadFailure(String name) {
		final boolean preserveSession = getPreserveSession();
		if (preserveSession) {
			logger.warn("Object in session under key [" + name + "] is serializable but could not be recovered through serialization based cloning. Attribute will be removed from the session");
			this.removeAttribute(name);
		}
		else {
			logger.warn("Object in session under key [" + name + "] is serializable but could not be recovered through serialization based cloning. Session will be invalidated");
			this.invalidate();
		}
	}

	boolean getPreserveSession() {
		final PropertySource propertySource = PropertySourceHolder.getInstance().getPropertySource();
		if (propertySource == null) return true;
		BooleanPropertyValue preserveSessionOnReloadFailure = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, true);
		
		final boolean preserveSession = preserveSessionOnReloadFailure.getValue();
		return preserveSession;
	}

	/* ************************** Helper methods *************************** */
	
	Object clone(Object attribute, SerializationHelper helper) {
		return helper.clone((Serializable) attribute);
	}

	ClassLoader getModuleClassLoader() {
		return moduleClassLoader;
	}

}
