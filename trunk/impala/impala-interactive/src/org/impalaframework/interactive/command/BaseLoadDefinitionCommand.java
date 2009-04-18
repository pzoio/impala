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

package org.impalaframework.interactive.command;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandState;

public abstract class BaseLoadDefinitionCommand implements Command {

    protected void doLoad(CommandState commandState) {
        LoadTestClassContextCommand loadCommand = new LoadTestClassContextCommand();
        loadCommand.execute(commandState);
        InitContextCommand initCommand = new InitContextCommand();
        initCommand.execute(commandState);
    }   
    
}
