 package org.impalaframework.service.registry;

import java.util.HashSet;
import java.util.Set;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.spring.module.ModuleDefinitionAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ServiceArrayRegistryExporter implements ServiceRegistryAware, BeanFactoryAware, InitializingBean, DisposableBean, ModuleDefinitionAware {

	private String[] beanNames;
	
	private String[] exportNames;
	
	private ModuleDefinition moduleDefinition;
	
	private ServiceRegistry serviceRegistry;

	private BeanFactory beanFactory;
	
	private Set<Object> services = new HashSet<Object>();
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(beanNames, "beanNames cannot be null");
		Assert.notNull(serviceRegistry);
		Assert.notNull(beanFactory);
		Assert.notNull(moduleDefinition);
		
		if (exportNames == null) {
			exportNames = beanNames;
		} else {
			if (exportNames.length != beanNames.length) {
				throw new ConfigurationException("beanNames array length [" + beanNames.length + "] is not the same length as exportNames array [" + exportNames.length + "]");
			}
		}
		
		for (int i = 0; i < beanNames.length; i++) {
			Object service = beanFactory.getBean(beanNames[i]);
			services.add(service);		
			serviceRegistry.addService(exportNames[i], moduleDefinition.getName(), service);

		}
	}
	
	public void destroy() throws Exception {
		for (Object service : services) {
			serviceRegistry.remove(service);
		}
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}	

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

	public void setBeanNames(String[] beanNames) {
		this.beanNames = beanNames;
	}

	public void setExportNames(String[] exportNames) {
		this.exportNames = exportNames;
	}

}
