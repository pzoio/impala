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

package org.impalaframework.spring.module;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ModuleArrayContributionExporter extends BaseModuleContributionExporter {

	final Log logger = LogFactory.getLog(ModuleArrayContributionExporter.class);

	private String[] contributions;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(contributions, "contributions cannot be null");

		processContributions(Arrays.asList(contributions));
	}

	public void setContributions(String[] contributions) {
		this.contributions = contributions;
	}

}
