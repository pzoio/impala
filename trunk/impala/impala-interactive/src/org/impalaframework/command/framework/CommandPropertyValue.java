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
package org.impalaframework.command.framework;

import org.springframework.util.Assert;

public class CommandPropertyValue {
	private String value;

	private String description;

	public CommandPropertyValue(String input, String description) {
		super();
		Assert.notNull(input);
		this.value = input;
		this.description = description;
	}

	public CommandPropertyValue(String input) {
		this(input, null);
	}

	public String getDescription() {
		return description;
	}

	public String getValue() {
		return value;
	}

}
