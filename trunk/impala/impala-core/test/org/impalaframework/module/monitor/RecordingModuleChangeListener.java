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

package org.impalaframework.module.monitor;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleContentChangeListener;

/**
 * @author Phil Zoio
 */
public class RecordingModuleChangeListener implements ModuleContentChangeListener {

    private List<ModuleChangeEvent> events = new ArrayList<ModuleChangeEvent>();

    public void moduleContentsModified(ModuleChangeEvent event) {
        events.add(event);
    }

    protected List<ModuleChangeEvent> getEvents() {
        return events;
    }
    
    protected void clear() {
        events.clear();
    }

}
