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

package org.impalaframework.module.builder;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.builder.XmlModulelDefinitionDocumentLoader;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

public class XmlModuleDefinitionDocumentLoaderTest extends TestCase {

	public final void testLoadDocument() {
		XmlModulelDefinitionDocumentLoader builder = new XmlModulelDefinitionDocumentLoader();
		Document document = builder.loadDocument(new ClassPathResource("xmlspec/moduledefinition.xml"));
		assertNotNull(document);
	}
	
	public final void testNotPresentLoadDocument() {
		XmlModulelDefinitionDocumentLoader builder = new XmlModulelDefinitionDocumentLoader();
		try {
			builder.loadDocument(new ClassPathResource("xmlspec/notpresent.xml"));
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Could not load module specification, as unable to obtain input stream for resource class path resource [xmlspec/notpresent.xml]", e.getMessage());
		}
	}
	
	public final void testBadlyFormedLoadDocument() {
		XmlModulelDefinitionDocumentLoader builder = new XmlModulelDefinitionDocumentLoader();
		try {
			builder.loadDocument(new ClassPathResource("xmlspec/badlyformedspec.xml"));
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to load XML module specification document from resource class path resource [xmlspec/badlyformedspec.xml]", e.getMessage());
		}
	}

}
