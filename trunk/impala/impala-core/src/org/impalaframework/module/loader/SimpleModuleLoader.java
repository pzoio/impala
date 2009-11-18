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

package org.impalaframework.module.loader;

import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Class providing basic implementation of {@link ModuleLoader} methods.
 * 
 * @author Phil Zoio
 */
public class SimpleModuleLoader implements ModuleLoader {

    private ModuleLocationResolver moduleLocationResolver;
    
    public void init() {
        Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
    }
    
    /**
     * Used wired in {@link ModuleLocationResolver} to retrieve module class locations
     */
    public Resource[] getClassLocations(String applicationId, ModuleDefinition moduleDefinition) {
        Assert.notNull(moduleDefinition);
        Assert.notNull(moduleLocationResolver);
        
        List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
        return ResourceUtils.toArray(locations);
    }
    
    /* ****************** protected methods ************** */

    protected ModuleLocationResolver getClassLocationResolver() {
        return moduleLocationResolver;
    }
    
    /* ****************** injection setter methods ************** */

    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }

}
