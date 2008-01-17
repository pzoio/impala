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

package tests;

import interfaces.Child;
import interfaces.Parent;
import junit.framework.TestCase;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.testrun.InteractiveTestRunner;

public class ParentChildTest extends TestCase implements ModuleDefinitionSource {

	public static void main(String[] args) {
		System.setProperty("impala.parent.project", "impala-core");
		InteractiveTestRunner.run(ParentChildTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testOne() {
		System.out.println("Doing stuff in testOne ...");

		Parent bean = DynamicContextHolder.getBean("parent", Parent.class);
		System.out.println("Got bean of type " + bean);

		Child child = bean.tryGetChild();
		try {
			child.childMethod();
			fail();
		}
		catch (RuntimeException e) {
			// e.printStackTrace();
		}
	}

	public RootModuleDefinition getModuleDefinition() {
		return new SimpleModuleDefinitionSource("parent-context.xml", new String[] { "impala-sample-dynamic-plugin1" }).getModuleDefinition();
	}

}