package org.impalaframework.module.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.file.monitor.FileMonitorImpl;
import org.impalaframework.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ScheduledModuleChangeMonitor implements ModuleChangeMonitor {

	final Logger logger = LoggerFactory.getLogger(ScheduledModuleChangeMonitor.class);
	
	private static final int DEFAULT_INITIAL_DELAY_SECONDS = 10;

	private static final int DEFAULT_INTERVAL_SECONDS = 2;

	private int initialDelay = DEFAULT_INTERVAL_SECONDS;

	private int checkInterval = DEFAULT_INITIAL_DELAY_SECONDS;

	private FileMonitor fileMonitor;

	private ScheduledExecutorService executor;

	public ScheduledModuleChangeMonitor() {
		super();
	}	

	private List<ModuleContentChangeListener> modificationListeners = new ArrayList<ModuleContentChangeListener>();

	private Map<String, ResourceInfo> resourcesToMonitor = new ConcurrentHashMap<String, ResourceInfo>();

	public void addModificationListener(ModuleContentChangeListener listener) {
		modificationListeners.add(listener);
	}

	public void setResourcesToMonitor(String moduleName, Resource[] resources) {
		if (resources != null && resources.length > 0) {
			logger.info("Monitoring for changes in module " + moduleName + ": " + Arrays.toString(resources));
			resourcesToMonitor.put(moduleName, new ResourceInfo(System.currentTimeMillis(), resources));
		}
		else {
			logger.info("No resources to monitor for module " + moduleName);
		}
	}

	protected void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	protected void setInterval(int interval) {
		this.checkInterval = interval;
	}

	void setDefaultsIfNecessary() {
		if (fileMonitor == null) {
			fileMonitor = new FileMonitorImpl();
		}

		if (executor == null)
			executor = Executors.newSingleThreadScheduledExecutor();
	}

	public void start() {

		setDefaultsIfNecessary();
		executor.scheduleWithFixedDelay(new Runnable() {

			public void run() {

				try {

					List<ModuleChangeInfo> modified = new LinkedList<ModuleChangeInfo>();

					final Set<String> moduleNames = resourcesToMonitor.keySet();
					
					for (String moduleName : moduleNames) {
						
						ResourceInfo ri = resourcesToMonitor.get(moduleName);
						if (ri != null) {
							// should be null except for case where item is
							// removed
							File[] files = ResourceUtils.getFiles(ri.resources);
							long lastModified = fileMonitor.lastModified(files);

							if (lastModified > ri.lastModified) {
								// add to the list of modified modules
								modified.add(new ModuleChangeInfo(moduleName));
								// set the ResourceInfo object
								ri.lastModified = lastModified;
							}
						}
					}

					if (!modified.isEmpty()) {
						logger.info("Found modified modules: {}", modified);
						final ModuleChangeEvent event = new ModuleChangeEvent(modified);
						for (ModuleContentChangeListener listener : modificationListeners) {
							listener.moduleContentsModified(event);
						}
					}
					else {
						if (logger.isDebugEnabled()) logger.debug("Completed check for modified modules. No modified module contents found");
					}

				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, initialDelay, checkInterval, TimeUnit.SECONDS);
	}

	public void setModificationListeners(List<ModuleContentChangeListener> modificationListeners) {
		this.modificationListeners.clear();
		this.modificationListeners.addAll(modificationListeners);
	}

	public void stop() {
		executor.shutdown();
	}

	private class ResourceInfo {
		private long lastModified;

		private Resource[] resources;

		public ResourceInfo(long lastModified, Resource[] resources) {
			super();
			this.lastModified = lastModified;
			this.resources = resources;
		}
	}
}
