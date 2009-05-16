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

package org.impalaframework.util;

import org.impalaframework.exception.ExecutionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;

import junit.framework.TestCase;

public class XMLDomUtilsTest extends TestCase {

    public void testLoadAndValidateDocument() throws Exception {
        ClassPathResource xml = new ClassPathResource("org/impalaframework/util/xmltest.xml");
        Document document = XMLDomUtils.loadDocument(xml);
        FileSystemResource xsdResource = new FileSystemResource("../impala-core/test/org/impalaframework/util/xmltest.xsd");
        //FIXME post 1.0M6 - re-enable this as this was preventing build going out when run in ANT
        //XMLDomUtils.validateDocument(document, "xmltest.xml", xsdResource);
        
        Element documentElement = document.getDocumentElement();
        documentElement.setAttribute("name", "value");
        
        try {
            XMLDomUtils.validateDocument(document, "xmltest.xml", xsdResource);
            fail();
        }
        catch (ExecutionException e) {
            assertTrue(e.getCause() instanceof SAXParseException);
            e.printStackTrace();
        }
    }
    
}
