/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.web.servlet.wrapper.context;

import static org.easymock.EasyMock.createMock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.util.CollectionStringUtils;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.servlet.qualifier.DefaultWebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.context.PartitionedServletContext;
import org.springframework.util.ClassUtils;

public class PartitionedServletContextTest extends TestCase {

    private ServletContext servletContext;
    private PartitionedServletContext wrapperContext;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        wrapperContext = new PartitionedServletContext(servletContext, "", "mymodule", new DefaultWebAttributeQualifier(), ClassUtils.getDefaultClassLoader());
    }
    
    @SuppressWarnings("unchecked")
    public void testGetAttributeNames() throws Exception {
        AttributeServletContext realContext = new AttributeServletContext();
        realContext.setAttribute("application__module_mymodule:mykey", "value1");
        realContext.setAttribute("mykey", "value2");
        realContext.setAttribute("application__module_mymodule:anotherkey", "value3");
        realContext.setAttribute("anotherkey", "value2");
        
        wrapperContext = new PartitionedServletContext(realContext, "", "mymodule", new DefaultWebAttributeQualifier(), ClassUtils.getDefaultClassLoader());
        Enumeration<String> attributeNames = wrapperContext.getAttributeNames();
        ArrayList<String> list = Collections.list(attributeNames);
        assertEquals(CollectionStringUtils.parseStringList("application__module_mymodule:mykey,application__module_mymodule:anotherkey"), list);
    }
    
    public void testGetWriteKeyToUse() throws Exception {
        assertEquals("application__module_mymodule:mykey", wrapperContext.getWriteKeyToUse("mykey"));
        assertEquals("mykey", wrapperContext.getWriteKeyToUse("shared:mykey"));
    }

}
