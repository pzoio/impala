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

package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.impalaframework.spring.resource.DirectoryResource;

/**
 * @author Phil Zoio
 */
public class ModuleClassLoaderTest extends TestCase {

	public void testLoadClassString() throws Exception {
		ModuleClassLoader pcl = new ModuleClassLoader(new File[] { new File("../impala-interactive/bin") });

		// check that this class loader loads the named class
		Class<?> cls1 = Class.forName("org.impalaframework.command.interactive.CommandStateConstants", false, pcl);
		assertSame(cls1.getClassLoader(), pcl);
	}
	
	public void testLoadClassURL() throws Exception {
		ModuleClassLoader pcl = new ModuleClassLoader(new URL[] { new DirectoryResource("../impala-interactive/bin").getURL() });

		// check that this class loader loads the named class
		Class<?> cls1 = Class.forName("org.impalaframework.command.interactive.CommandStateConstants", false, pcl);
		assertSame(cls1.getClassLoader(), pcl);
	}

}
