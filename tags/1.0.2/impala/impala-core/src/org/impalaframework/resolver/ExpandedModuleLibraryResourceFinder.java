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

import org.springframework.util.Assert;

/**
 * Searches for jar in expanded module directory, under the assumption that the
 * module will be found in
 * 
 * [workspace_root/module_name/lib]
 * 
 * @author Phil Zoio
 */
public class ExpandedModuleLibraryResourceFinder extends BaseModuleLibraryResourceFinder {

    private String libDirectory = "lib";

    @Override
    protected String getLibraryDirectory() {
        return libDirectory;
    }
    
    public void setLibDirectory(String libDirectory) {
        Assert.notNull(libDirectory, "libDirectory cannot be null");
        this.libDirectory = libDirectory;
    }

}
