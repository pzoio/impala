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

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * @author Phil Zoio
 */
public abstract class AbstractModuleLocationResolver implements ModuleLocationResolver {

	protected abstract String getWorkspaceRoot();

	protected File getRootDirectory() {
		String workspace = getWorkspaceRoot();
		if (workspace != null) {
			File candidate = new File(workspace);

			if (!candidate.exists()) {
				throw new ConfigurationException("'workspace.root' (" + workspace + ") does not exist");
			}
			if (!candidate.isDirectory()) {
				throw new ConfigurationException("'workspace.root' (" + workspace + ") is not a directory");
			}
			return candidate;
		}
		return null;
	}

	protected String getRootDirectoryPath() {
		File rootDirectory = getRootDirectory();
		
		if (rootDirectory == null) {
			throw new ConfigurationException("Unable to determine application's root directory. Has the property 'workspace.root' been set?");
		}
		
		String absolutePath = rootDirectory.getAbsolutePath();
		return StringUtils.cleanPath(absolutePath);
	}

	protected List<Resource> getResources(String moduleName, String classDir) {
		String path = PathUtils.getPath(getRootDirectoryPath(), moduleName);
		path = PathUtils.getPath(path, classDir);
		Resource resource = new FileSystemResource(path);
		return Collections.singletonList(resource);
	}

}
