/*
 * Copyright 2007-2010 the original author or authors.
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

import java.util.Collections;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * {@link ModuleResourceFinder} implementation for module jar, under the assumption that the jar will be present
 * in the directory denoted [workspace_root_path/moduleName-version.jar]
 * @author Phil Zoio
 */
public class JarModuleClassResourceFinder implements ModuleResourceFinder {
    
    public List<Resource> findResources(
            String workspaceRootPath,
            String moduleName, 
            String moduleVersion) {
        
        String jarName = moduleName;
        String jarWithVersionName = null;
        
        if (moduleVersion != null){
            jarWithVersionName = jarName + "-" + moduleVersion;
        }
        
        Resource resource = null;
        
        if (jarWithVersionName != null)
            resource = findJarFile(workspaceRootPath, jarWithVersionName);
        if (resource == null) {
            resource = findJarFile(workspaceRootPath, jarName);
        }
        
        if (resource == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(resource);
    }

    private Resource findJarFile(String workspaceRootFile, String jarName) {
        String path = PathUtils.getPath(workspaceRootFile, jarName + ".jar");
        Resource resource = new FileSystemResource(path);
        
        if (resource.exists()) {
            return resource;
        } else {
            return null;
        }
    }
}
