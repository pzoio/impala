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

package org.impalaframework.ant;

import java.io.File;


import org.apache.tools.ant.BuildException;
import org.impalaframework.ant.DownloadTask;
import org.impalaframework.ant.DownloadTask.ArtifactInfo;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class DownloadTaskTest extends TestCase {

	private DownloadTask downloadTask;

	public void setUp() {
		downloadTask = new DownloadTask();
		downloadTask.setDependencies(new File("somefile"));
	}
	
	public void testReplaceAndTrim() {
		assertEquals("first/second", downloadTask.replaceAndTrim("first.second"));
		assertEquals("/first/second/", downloadTask.replaceAndTrim(".first.second."));
		assertEquals(".first.second.", downloadTask.replaceAndTrim("\\.first\\.second\\."));
	}

	public void testParseDuff() {
		try {
			downloadTask.parseArtifactInfo("duff");
			fail();
		}
		catch (BuildException e) {
			assertContains(
					"duff in somefile has invalid format. Should be: [targetDir] from [organisation]:[artifact]:[version]",
					e.getMessage());
		}
	}

	private void assertContains(String expected, String actual) {
		try {
			assertTrue(actual.contains(expected));
		}
		catch (AssertionFailedError f) {
			System.out.println("Expected to contain: " + expected);
			System.out.println("Actual: " + actual);
			throw f;
		}
	}

	public void testParseArtifactInfo() {
		try {
			downloadTask.parseArtifactInfo("main from notaproperformat");
			fail();
		}
		catch (BuildException e) {
			assertContains(
					"main from notaproperformat in somefile has invalid format. Should be: [targetDir] from [organisation]:[artifact]:[version]",
					e.getMessage());
		}
	}

	public void testParseArtifact() {
		ArtifactInfo ai = downloadTask.parseArtifactInfo("main from org.apache.ant:ant:1.7.0");
		assertEquals("ant", ai.getArtifact());
		assertEquals("org/apache/ant", ai.getOrganisation());
		assertEquals("main", ai.getTargetSubdirectory());
		assertEquals("1.7.0", ai.getVersion());
		assertEquals(null, ai.isHasSource());
	}

	public void testParseArtifactWithSource() {
		ArtifactInfo ai = downloadTask.parseArtifactInfo("main from org.apache.ant:ant:1.7.0 source=true");
		assertEquals("ant", ai.getArtifact());
		assertEquals("org/apache/ant", ai.getOrganisation());
		assertEquals("main", ai.getTargetSubdirectory());
		assertEquals("1.7.0", ai.getVersion());
		assertEquals(Boolean.TRUE, ai.isHasSource());

		ai = downloadTask.parseArtifactInfo("main from org.apache.ant:ant:1.7.0 source=false");
		assertEquals(Boolean.FALSE, ai.isHasSource());
	}

}
