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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.TestCase;

import org.impalaframework.spring.resource.DirectoryResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;

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
	
	public void testParentClassString() throws Exception {
		ParentClassLoader pcl = new ParentClassLoader(new File[] { new File("../impala-sample-dynamic-plugin1/bin") });

		// check that this class loader loads the named class
		Class<?> cls1 = Class.forName("classes.FileMonitorBean1", false, pcl);
		assertSame(cls1.getClassLoader(), pcl);
	}
	
	public void testLoadParentURL() throws Exception {
		ParentClassLoader pcl = new ParentClassLoader(new URL[] { new DirectoryResource("../impala-sample-dynamic-plugin1/bin").getURL() });

		// check that this class loader loads the named class
		Class<?> cls1 = Class.forName("classes.FileMonitorBean1", false, pcl);
		assertSame(cls1.getClassLoader(), pcl);
	}
	
	public void testParent() throws Exception {
		ClassLoader parent = ClassUtils.getDefaultClassLoader();
		File base = new File("../impala-core/resources/classloader");
		File location1 = new File(base, "impl-one");
		File location2 = new File(base, "impl-two");
		
		ParentClassLoader pcl1 = new ParentClassLoader(parent, new File[]{location1});
		ParentClassLoader pcl2 = new ParentClassLoader(pcl1, new File[] { location2 });

		System.out.println(pcl2.toString());
		
		Class<?> cl = Class.forName("ClassLoaderImpl", false, pcl2);
		ClassLoaderInterface impl = (ClassLoaderInterface) cl.newInstance();
		assertEquals("The first implementation", impl.getString());
		
		InputStream stream = pcl2.getResourceAsStream("propsfile.properties");
		String text = FileCopyUtils.copyToString(new InputStreamReader(stream));
		assertEquals("value2", text);
	}
	
	public void testModule() throws Exception {
		ClassLoader parent = ClassUtils.getDefaultClassLoader();
		File base = new File("../impala-core/resources/classloader");
		File location1 = new File(base, "impl-one");
		File location2 = new File(base, "impl-two");
		
		ModuleClassLoader pcl1 = new ModuleClassLoader(parent, new File[]{location1});
		ModuleClassLoader pcl2 = new ModuleClassLoader(pcl1, new File[] { location2 });

		System.out.println(pcl2.toString());
		
		Class<?> cl = Class.forName("ClassLoaderImpl", false, pcl2);
		ClassLoaderInterface impl = (ClassLoaderInterface) cl.newInstance();
		assertEquals("The second implementation", impl.getString());
		
		InputStream stream = pcl2.getResourceAsStream("propsfile.properties");
		String text = FileCopyUtils.copyToString(new InputStreamReader(stream));
		assertEquals("value2", text);
	}

	
}
