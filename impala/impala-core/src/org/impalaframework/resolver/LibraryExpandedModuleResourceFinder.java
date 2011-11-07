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

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

/**
 * Searches for jar in expanded module directory, under the assumption that the
 * module will be found in
 * 
 * [workspace_root/module_name/lib]
 * 
 * @author Phil Zoio
 */
public class LibraryExpandedModuleResourceFinder implements
        ModuleResourceFinder {

    public List<Resource> findResources(String workspaceRootPath, String moduleName, String moduleVersion) {
        System.out.println(workspaceRootPath);
        
        //FIXME test
        
        String path = PathUtils.getPath(workspaceRootPath, moduleName);
        path = PathUtils.getPath(path, "lib");
        File internalModulesDirectory = new File(path);
        if (internalModulesDirectory.exists()) {
            
            final File[] listFiles = internalModulesDirectory.listFiles(new FileFilter() {
                
                public boolean accept(File file) {
                    if (file.getName().endsWith(".jar")) {
                        return true;
                    }
                    return false;
                }
            });
            
            final Resource[] resources = ResourceUtils.getResources(listFiles);
            return Arrays.asList(resources);
        }
        
        return Collections.emptyList();
    }

}