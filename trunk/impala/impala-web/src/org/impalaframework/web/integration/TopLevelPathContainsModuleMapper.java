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

package org.impalaframework.web.integration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link RequestModuleMapper} which attempts to match part of
 * the top level segment of the request path with one of an array of module
 * names. For example, if {@link #moduleNames} contains the array
 * <code>new String[]{"module1", "module2"}</code>, then the path
 * <code>/module1Suffix/file.htm</code> will match <code>module1</code>.
 * Since the comparison is case insensitve, <code>/Module2Suffix/file.htm</code>
 * will match <code>module2</code>.
 * 
 * @author Phil Zoio
 */
public class TopLevelPathContainsModuleMapper implements
		RequestModuleMapper {

	private String[] nameSegments;
	private String[] moduleNames;	
	private String prefix;

	public void init(ServletConfig servletConfig) {
	}

	public void init(FilterConfig filterConfig) {
	}

	public String getModuleForRequest(HttpServletRequest request) {
		Assert.notNull(nameSegments, "nameSegments cannot be null");

		if (nameSegments.length > 0) {
			final String topLevelPath = ModuleProxyUtils.getTopLevelPathSegment(request.getServletPath());
			
			if (topLevelPath != null) {
				for (int i = 0; i < nameSegments.length; i++) {
					String segment = nameSegments[i];
					if (topLevelPath.toLowerCase().startsWith(segment)) {
						return (prefix != null ? prefix : "") + moduleNames[i];
					}
				}
			}
		}
		return null;
	}

	public void setModuleNames(String[] moduleNames) {
		List<String> nameSegmentList = new ArrayList<String>();
		List<String> moduleList = new ArrayList<String>();
		for (int i = 0; i < moduleNames.length; i++) {
			final String moduleName = moduleNames[i];
			final String trim = moduleName.toLowerCase().trim();
			if (StringUtils.hasText(trim)) {
				nameSegmentList.add(trim);
				moduleList.add(moduleName);
			}
		}
		this.nameSegments = nameSegmentList.toArray(new String[0]);
		this.moduleNames = moduleList.toArray(new String[0]);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
