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

package org.impalaframework.module.modification.graph;

import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.spi.ModificationExtractor;

/**
 * Package level interface to support contract between graph ModificationExtractors and their
 * delegates.
 * @author Phil Zoio
 */
interface GraphAwareModificationExtractor extends ModificationExtractor {
    
    public DependencyManager getOldDependencyManager();

    public DependencyManager getNewDependencyManager();
    
}
