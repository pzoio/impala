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

package org.impalaframework.service.registry.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.impalaframework.service.reference.ServiceReferenceSorter;
import org.impalaframework.service.registry.exporttype.ExportTypeDeriver;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of {@link ServiceRegistry}, which holds services which can be shared across modules.
 * @author Phil Zoio
 */
public class ServiceRegistryImpl implements ServiceRegistry {

    private static Log logger = LogFactory.getLog(ServiceRegistryImpl.class);
    
    static ServiceReferenceFilter IDENTIFY_FILTER = new IdentityServiceReferenceFilter();
    
    private ClassChecker classChecker = new ClassChecker();
    private ServiceReferenceSorter serviceReferenceSorter = new ServiceReferenceSorter();
    private ExportTypeDeriver exportTypeDeriver = new EmptyExportTypeDeriver();

    private Map<String, List<ServiceRegistryReference>> beanNameToService = new ConcurrentHashMap<String, List<ServiceRegistryReference>>();
    private Map<String, List<ServiceRegistryReference>> moduleNameToServices = new ConcurrentHashMap<String, List<ServiceRegistryReference>>();
    private Map<String, List<ServiceRegistryReference>> classNameToServices = new ConcurrentHashMap<String, List<ServiceRegistryReference>>();
    private Set<ServiceRegistryReference> services = new CopyOnWriteArraySet<ServiceRegistryReference>();
    
    /**
     * For each registry entry, holds a {@link MapTargetInfo} which points to the keys by which the {@link ServiceRegistryReference}
     * instance is held as a value.
     */
    private Map<ServiceRegistryReference, MapTargetInfo> beanTargetInfo = new IdentityHashMap<ServiceRegistryReference, MapTargetInfo>();

    // use CopyOnWriteArrayList to support non-blocking thread-safe iteration
    private List<ServiceRegistryEventListener> listeners = new CopyOnWriteArrayList<ServiceRegistryEventListener>();

    private Object registryLock = new Object();
    private Object listenersLock = new Object();

    /* ************ registry service modification methods * ************** */

    public ServiceRegistryReference addService(
            String beanName, 
            String moduleName, 
            Object service,
            ClassLoader classLoader) {
        
        return addService(beanName, moduleName, service, null, null, classLoader);
    }

    public ServiceRegistryReference addService(
            String beanName, 
            String moduleName, 
            Object service,
            List<Class<?>> classes, 
            Map<String, ?> attributes, 
            ClassLoader classLoader) {
        
        //Note: null checks performed by BasicServiceRegistryReference constructor
        BasicServiceRegistryReference serviceReference = null;
        synchronized (registryLock) {
            
            boolean checkClasses = true;
            
            if (classes == null) {
                classes = deriveExportTypes(service, beanName, classes);
                checkClasses = false;
            }
            
            serviceReference = new BasicServiceRegistryReference(
                    service, 
                    beanName,
                    moduleName, 
                    classes, 
                    attributes, classLoader);
            
            if (checkClasses) {
                checkClasses(serviceReference);
            }
            
            if (classes.isEmpty() && beanName == null) {
                throw new InvalidStateException("Attempted to register bean from module '" + moduleName + "'" +
                        " with no bean name and no export types available.");
            }
            
            //deal with the case of overriding and existing bean
            if (beanName != null) {
                addReferenceToMap(beanNameToService, beanName, serviceReference, true);
            }
            
            addReferenceToMap(moduleNameToServices, moduleName, serviceReference, false);
            
            for (Class<?> exportType : classes) {
                addReferenceToMap(classNameToServices, exportType.getName(), serviceReference, true);
            }
            
            MapTargetInfo targetInfo = new MapTargetInfo(classes, beanName, moduleName);
            beanTargetInfo.put(serviceReference, targetInfo);
            
            services.add(serviceReference);
        }
        
        if (logger.isDebugEnabled())
            logger.debug("Added service bean '" + beanName
                    + "' contributed from module '" + moduleName
                    + "' to service registry, with attributes " + attributes);

        ServiceAddedEvent event = new ServiceAddedEvent(serviceReference);
        invokeListeners(event);
        
        Assert.notNull(serviceReference, "Programming error: addService completing without returning non-null service referece");
        
        return serviceReference;
    }

    public void evictModuleServices(String moduleName) {

        Assert.notNull(moduleName, "moduleName cannot be null");
        
        final List<ServiceRegistryReference> list;
        
        synchronized (registryLock) {
            final List<ServiceRegistryReference> tempList = moduleNameToServices.get(moduleName);
            
            if (tempList != null) {
                list = new LinkedList<ServiceRegistryReference>(tempList);
            } else {
                list = null;
            }
        }
        
        //we have new list so can use it outside synchronised block
        if (list != null) {
            for (ServiceRegistryReference serviceRegistryReference : list) {
                remove(serviceRegistryReference);
            }
        }
    }

    public boolean remove(ServiceRegistryReference serviceReference) {
        
        Assert.notNull(serviceReference, "serviceReference cannot be null");
        boolean removed = false;
        
        synchronized (registryLock) {
            
            final MapTargetInfo targetInfo = beanTargetInfo.remove(serviceReference);
            if (targetInfo != null) {
                String beanName = targetInfo.getBeanName();
                if (beanName != null) {
                    final List<ServiceRegistryReference> list = beanNameToService.get(beanName);
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                String moduleName = targetInfo.getModuleName();
                if (moduleName != null) {
                    final List<ServiceRegistryReference> list = moduleNameToServices.get(moduleName);
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                final List<Class<?>> exportTypes = targetInfo.getExportTypes();
                for (Class<?> exportType : exportTypes) {
                    final List<ServiceRegistryReference> list = classNameToServices.get(exportType.getName());
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                removed = services.remove(serviceReference);
            }
        }

        if (serviceReference != null) {
            
            if (removed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed from service reference '" + serviceReference
                        + "' contributed from module '"
                        + serviceReference.getContributingModule() + "'");
            }
            
            ServiceRemovedEvent event = new ServiceRemovedEvent(serviceReference);
            
            invokeListeners(event);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No service '" + serviceReference
                            + "' present to remove from service registry");
                }
            }
        }
        return removed;
    }
    
    /* ************ registry service accessor methods * ************** */

    /**
     * Returns named service, which has to implement all of implemenation types specified
     */
    public ServiceRegistryReference getService(String beanName, Class<?>[] supportedTypes) {
        
        List<ServiceRegistryReference> references = getServicesInternal(beanName, supportedTypes);
        
        if (references == null) {
            return null;
        }
        
        for (int i = 0; i < references.size(); i++) {
            final ServiceRegistryReference ref = getMatchingReference(references, supportedTypes, i);
            if (ref != null) {
                return ref;
            }
        }
        
        return null;
    }

    public List<ServiceRegistryReference> getServices(String beanName, Class<?>[] supportedTypes) {

        List<ServiceRegistryReference> references = getServicesInternal(beanName, supportedTypes);
        List<ServiceRegistryReference> serviceList = new LinkedList<ServiceRegistryReference>();
        
        //FIXME may only be looking only at export types here
        for (ServiceRegistryReference serviceReference : references) {
            if (classChecker.matchesTypes(serviceReference, supportedTypes)) {
                serviceList.add(serviceReference);
            }
        }
        
        return serviceList;
    }
    
    /**
     * Returns named service, which has to implement all of implementation types specified
     */
    @SuppressWarnings("unchecked")
    private List<ServiceRegistryReference> getServicesInternal(String beanName, Class<?>[] supportedTypes) {
        
        Assert.notNull(beanName, "beanName cannot be null");
        List<ServiceRegistryReference> references;
        
        synchronized (registryLock) {
            List<ServiceRegistryReference> list = beanNameToService.get(beanName);
            references = (list == null ? Collections.EMPTY_LIST : new ArrayList<ServiceRegistryReference>(list));
        }
        
        //no need to sort here, as it is already sorted
        return references;
    }

    /**
     * Returns filtered services, which has to implement all of implemenation types specified.
     * @param filter the filter which is used to match service references. Can be null. If so, {@link IdentityServiceReferenceFilter} is used,
     * which always returns true. See {@link IdentityServiceReferenceFilter#matches(ServiceRegistryReference)}.
     * @param types the types with which returned references should be compatible
     * @param if true only matches against explicit export types
     */
    public List<ServiceRegistryReference> getServices(ServiceReferenceFilter filter, Class<?>[] types, boolean exportTypesOnly) {

        if (filter == null) {
            filter = IDENTIFY_FILTER;
        }
        
        if (exportTypesOnly) {
            
            Assert.notNull(types, "exportTypesOnly is true but types is null");
            Assert.notEmpty(types, "exportTypesOnly is true but types is empty");
            
            final List<ServiceRegistryReference> values; 
            
            //check each type against 
            synchronized (registryLock) {
                
                values = classNameToServices.get(types[0].getName());
                
                if (values == null) {
                    return Collections.emptyList();
                }
                
                //check each type
                for (Iterator<ServiceRegistryReference> iterator = values.iterator(); iterator.hasNext();) {
                    ServiceRegistryReference serviceReference = iterator.next();
                    
                    //make sure that all other types contain
                    if (types.length > 1) {
                        for (int i = 1; i < types.length; i++) {
                            List<ServiceRegistryReference> secondaryValues = classNameToServices.get(types[i].getName());
                            boolean found = false;
                            
                            if (secondaryValues != null) {
                                if (secondaryValues.contains(serviceReference)) {
                                    found = true;
                                }
                            }
                            
                            if (!found) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
                
            //check each entry against filter
            for (Iterator<ServiceRegistryReference> iterator = values.iterator(); iterator.hasNext();) {
                ServiceRegistryReference serviceReference = iterator.next();
               
                if (!filter.matches(serviceReference)) {
                    iterator.remove();
                }
            }
            
            return serviceReferenceSorter.sort(values, true);
        } else {
        
            //Create new list of services for concurrency protection
            final List<ServiceRegistryReference> values;
            
            synchronized (registryLock) {
                values = new LinkedList<ServiceRegistryReference>(services);
            }
            
            for (Iterator<ServiceRegistryReference> iterator = values.iterator(); iterator.hasNext();) {
                ServiceRegistryReference serviceReference = iterator.next();
               
                if (!(classChecker.matchesTypes(serviceReference, types) && filter.matches(serviceReference))) {
                    iterator.remove();
                }
            }
            //sort, reusing existing list
            return serviceReferenceSorter.sort(values, true);
        }
        
    }
    
    /* ************ listener related methods * ************** */

    /**
     * Adds to global event listeners to which all service registry events will
     * be broadcast. Note that a single event listener instance can only be added once.
     * Any subsequent attempts to add this listener will not have any effect.
     */
    public boolean addEventListener(ServiceRegistryEventListener listener) {
        Assert.notNull(listener);
        
        synchronized (listenersLock) {
            if (this.listeners.contains(listener)) {
                logger.warn("Listener " + ObjectUtils.identityToString(listener) + " already a listener for this service registry");
                return false;
            } else {
                listeners.add(listener);
                if (logger.isDebugEnabled()) {
                    logger.debug("Added service registry listener " + listener);
                }
                return true;
            }
        }
    }
    
    /**
     * Removes global event listeners to which all service registry events will
     * be broadcast
     * @return true if listener was removed.
     */
    public boolean removeEventListener(ServiceRegistryEventListener listener) {
        List<ServiceRegistryEventListener> listeners = getCopyOfListeners();
        
        return removeListener(listener, listeners);
    }
    
    /* ************ registry collection accessor methods * ************** */
    
    List<ServiceRegistryReference> getClassReferences(Class<?> exportType) {
        final List<ServiceRegistryReference> list = classNameToServices.get(exportType.getName());
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }
    
    List<ServiceRegistryReference> getModuleReferences(String moduleName) {
        final List<ServiceRegistryReference> list = moduleNameToServices.get(moduleName);
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }
    
    List<ServiceRegistryReference> getBeanReferences(String beanName) {
        final List<ServiceRegistryReference> list = beanNameToService.get(beanName);
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }

    ServiceRegistryReference getBeanReference(String beanName) {
        final List<ServiceRegistryReference> beanReferences = getBeanReferences(beanName);
        if (beanReferences.isEmpty()) return null;
        return beanReferences.get(0);
    }
    
    boolean hasService(ServiceRegistryReference serviceReference) {     
        return services.contains(serviceReference);
    }
    
    /* ************ helper methods * ************** */
    
    List<Class<?>> deriveExportTypes(Object service, String beanName, List<Class<?>> classes) {

        //pass on job of figuring out export types to ExportTypeDeriver      
        if (exportTypeDeriver != null) {
            return exportTypeDeriver.deriveExportTypes(service, beanName, classes);
        }
        return Collections.emptyList();
    }

    void checkClasses(ServiceRegistryReference serviceReference) {
        classChecker.checkClasses(serviceReference);
    }

    private ServiceRegistryReference getMatchingReference(
            final List<ServiceRegistryReference> list, 
            Class<?>[] supportedTypes,
            int index) {
        
        final ServiceRegistryReference reference = list.get(index);
        if (reference == null) {
            return null;
        }
        
        if (classChecker.matchesTypes(reference, supportedTypes)) {
            return reference;
        } else {
            return null;
        }
    }
    
    private List<ServiceRegistryEventListener> getCopyOfListeners() {
        return new ArrayList<ServiceRegistryEventListener>(listeners);
    }

    private void invokeListeners(ServiceRegistryEvent event) {
        
        List<ServiceRegistryEventListener> listeners = getCopyOfListeners();
        
        //inform all listeners of service listener event
        for (ServiceRegistryEventListener listener : listeners) {
            listener.handleServiceRegistryEvent(event);
        }
    }

    private boolean removeListener(ServiceRegistryEventListener listener, List<ServiceRegistryEventListener> listeners) {
        
        for (ServiceRegistryEventListener currentListener : listeners) {
            
            if (currentListener == listener) {
                boolean removed = this.listeners.remove(currentListener);
                if (logger.isDebugEnabled())
                    logger.debug("Removed service registry listener " + listener + ": " + removed);
                return removed;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void addReferenceToMap(Map map,
            Object key,
            ServiceRegistryReference serviceReference, boolean sort) {
        
        List<ServiceRegistryReference> list = (List<ServiceRegistryReference>) map.get(key);
        if (list == null) {
            list = new ArrayList<ServiceRegistryReference>();
            map.put(key, list);
        }
        list.add(serviceReference);
        if (sort) {
            this.serviceReferenceSorter.sort(list, true);
        }
    }
    
    /* ************ dependency injection setters * ************** */

    public void setExportTypeDeriver(ExportTypeDeriver exportTypeDeriver) {
        this.exportTypeDeriver = exportTypeDeriver;
    }
    
    /**
     * Used to hold the keys by which a particular {@link ServiceRegistryReference} instance is held in
     * this service registry as a value.
     * @author Phil Zoio
     */
    class MapTargetInfo {
        
        /**
         * The key by which the {@link ServiceRegistryReference} is held in the bean name map
         */
        private final String beanName;
        
        /**
         * The keys by which the {@link ServiceRegistryReference} is held in the classes map
         */
        private final List<Class<?>> classes;
        
        /**
         * The keys by which the {@link ServiceRegistryReference} is held in the modules map
         */
        private final String moduleName;
        
        private MapTargetInfo(List<Class<?>> classes, String beanName,
                String moduleName) {
            super();
            Assert.notNull(moduleName);
            Assert.notNull(classes);
            this.classes = classes;
            this.beanName = beanName;
            this.moduleName = moduleName;
        }

        public String getBeanName() {
            return beanName;
        }

        public List<Class<?>> getExportTypes() {
            return classes;
        }

        public String getModuleName() {
            return moduleName;
        }
        
    }

    private static class IdentityServiceReferenceFilter implements ServiceReferenceFilter {

        public boolean matches(ServiceRegistryReference reference) {
            return true;
        }
        
    }

}

