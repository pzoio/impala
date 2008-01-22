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

package org.impalaframework.resolver;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.impalaframework.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * @author Phil Zoio
 */
public class StandaloneModuleLocationResolver extends BaseModuleLocationResolver {
	
	final Logger logger = LoggerFactory.getLogger(StandaloneModuleLocationResolver.class);

	public StandaloneModuleLocationResolver() {
		super();
		init();
	}

	public StandaloneModuleLocationResolver(Properties properties) {
		super(properties);
		init();
	}

	protected void init() {

		// the parent directory in which tests are expected to be found
		mergeProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, null, null);

		// the plugin directory which is expected to contain classes
		mergeProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY, "bin", null);

		// the parent directory in which tests are expected to be found
		mergeProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY, "bin", null);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		String classDir = StringUtils.cleanPath(getProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY));
		return getResources(moduleName, classDir);
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		String classDir = getProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY);
		return getResources(moduleName, classDir);
	}

	private List<Resource> getResources(String moduleName, String classDir) {
		String path = PathUtils.getPath(getRootDirectoryPath(), moduleName);
		path = PathUtils.getPath(path, classDir);
		Resource resource = new FileSystemResource(path);
		return Collections.singletonList(resource);
	}
	
	@Override
	public File getRootDirectory() {
		File rootDirectory = super.getRootDirectory();
		if (rootDirectory != null) {
			return rootDirectory;
		}
		// note that if workspace root is not specified, then parent directory
		// is used
		return new File("../");
	}

}
