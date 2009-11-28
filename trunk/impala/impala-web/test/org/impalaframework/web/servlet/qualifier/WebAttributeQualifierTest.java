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

package org.impalaframework.web.servlet.qualifier;

import org.impalaframework.web.servlet.qualifier.DefaultWebAttributeQualifier;

import junit.framework.TestCase;

public class WebAttributeQualifierTest extends TestCase {

    private DefaultWebAttributeQualifier qualifier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        qualifier = new DefaultWebAttributeQualifier();
    }
    
    public void testGetQualifiedAttributeName() throws Exception {
        assertEquals("application_id_module_mod:attribute", qualifier.getQualifiedAttributeName("attribute", "id", "mod"));
        assertEquals("application_id_module_mod:attribute", qualifier.getQualifiedAttributeName("application_id_module_mod:attribute", "id", "mod"));
        assertEquals("application__module_:attribute", qualifier.getQualifiedAttributeName("attribute", "", ""));
        assertEquals("attribute", qualifier.getQualifiedAttributeName("shared:attribute", "", ""));
    }
    
}
