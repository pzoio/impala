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

package org.impalaframework.interactive.resolver;

import java.util.List;

import org.impalaframework.resolver.CascadingModuleLocationResolver;
import org.impalaframework.resolver.ModuleResourceFinder;
import org.springframework.core.io.Resource;

public class TestEnabledModuleLocationResolver extends CascadingModuleLocationResolver {

    private List<ModuleResourceFinder> testResourceFinders;
    
    @Override
    public List<Resource> getModuleTestClassLocations(String moduleName) {
        return super.getResources(moduleName, testResourceFinders, true);
    }

    public void setTestResourceFinders(List<ModuleResourceFinder> testResourceFinders) {
        this.testResourceFinders = testResourceFinders;
    }

}
