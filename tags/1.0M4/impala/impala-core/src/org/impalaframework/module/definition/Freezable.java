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

package org.impalaframework.module.definition;


/**
 * {@link Freezable} contains methods which can be used to determine whether an
 * object is mutable. If {@link #isFrozen()} returns true, then the object can
 * be seen as immutable and hence effectively thread safe. As long as
 * {@link #unfreeze()} is not called, method calls on the object do not need to
 * be synchronized.
 * 
 * @see RootModuleDefinition
 * @author Phil Zoio
 */
public interface Freezable {

	void freeze();

	void unfreeze();

	boolean isFrozen();

}
