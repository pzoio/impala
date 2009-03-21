/*
 * Copyright 2009 the original author or authors.
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

package org.impalaframework.service.filter.ldap;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

@SuppressWarnings("unchecked")
public class FilterTest extends TestCase {
	
	private Map data;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		data = new HashMap();
	}

	public void testEqualsAndOrNot() throws Exception {
		match(false, "(param1=value1)");
		match(false, "(|(param1=value1)(param2=value2))");
		data.put("param1", "value1");
		match(true, "(param1=value1)");
		match(false, "(!(param1=value1))");
		match(true, "(|(param1=value1)(param2=value2))");
		match(false, "(&(param1=value1)(param2=value2))");
		data.put("param2", "value2");
		match(true, "(&(param1=value1)(param2=value2))");
		match(false, "(!(&(param1=value1)(param2=value2)))");
	}
	
	public void testEqualsType() throws Exception {
		match(false, "(byteParam=value1)");
		match(false, "(byteParam=1)");
		data.put("byteParam", new Byte((byte)1));
		match(true, "(byteParam=1)");
		
		match(false, "(shortParam=value1)");
		match(false, "(shortParam=1)");
		data.put("shortParam", new Short((byte)1));
		match(true, "(shortParam=1)");
		
		match(false, "(intParam=value1)");
		match(false, "(intParam=1)");
		data.put("intParam", new Integer(1));
		match(true, "(intParam=1)");
		
		match(false, "(longParam=value1)");
		match(false, "(longParam=1)");
		data.put("longParam", new Long(1L));
		match(true, "(longParam=1)");
		
		match(false, "(floatParam=value1)");
		match(false, "(floatParam=1)");
		data.put("floatParam", new Float(1.0));
		match(true, "(floatParam=1)");
		
		match(false, "(doubleParam=value1)");
		match(false, "(doubleParam=1)");
		data.put("doubleParam", new Double(1.0));
		match(true, "(doubleParam=1)");
		
		match(false, "(charParam=value1)");
		match(false, "(charParam=1)");
		data.put("charParam", new Character('1'));
		match(true, "(charParam=1)");
		
		match(false, "(booleanParam=value1)");
		match(false, "(booleanParam=true)");
		data.put("booleanParam", new Boolean("true"));
		match(true, "(booleanParam=true)");
	}
	
	public void testGreatThanType() throws Exception {
		match(false, "(stringParam>=value1)");
		match(false, "(stringParam>=1)");
		data.put("stringParam", "2");
		match(true, "(stringParam>=1)");
		match(true, "(stringParam>=2)");
		match(false, "(stringParam>=3)");
		
		match(false, "(byteParam>=value1)");
		match(false, "(byteParam>=1)");
		data.put("byteParam", new Byte((byte)2));
		match(true, "(byteParam>=1)");
		match(true, "(byteParam>=2)");
		match(false, "(byteParam>=3)");
		
		match(false, "(shortParam>=value1)");
		match(false, "(shortParam>=1)");
		data.put("shortParam", new Short((byte)2));
		match(true, "(shortParam>=1)");
		match(true, "(shortParam>=2)");
		match(false, "(shortParam>=3)");
		
		match(false, "(intParam>=value1)");
		match(false, "(intParam>=1)");
		data.put("intParam", new Integer(2));
		match(true, "(intParam>=1)");
		match(true, "(intParam>=2)");
		match(false, "(intParam>=3)");
		
		match(false, "(longParam>=value1)");
		match(false, "(longParam>=1)");
		data.put("longParam", new Long(2L));
		match(true, "(longParam>=1)");
		match(true, "(longParam>=2)");
		match(false, "(longParam>=3)");
		
		match(false, "(floatParam>=value1)");
		match(false, "(floatParam>=1)");
		data.put("floatParam", new Float(2.0));
		match(true, "(floatParam>=1)");
		match(true, "(floatParam>=2)");
		match(false, "(floatParam>=3)");
		
		match(false, "(doubleParam>=value1)");
		match(false, "(doubleParam>=1)");
		data.put("doubleParam", new Double(2.0));
		match(true, "(doubleParam>=1)");
		match(true, "(doubleParam>=2)");
		match(false, "(doubleParam>=3)");
		
		match(false, "(charParam>=value1)");
		match(false, "(charParam>=1)");
		data.put("charParam", new Character('2'));
		match(true, "(charParam>=1)");
		match(true, "(charParam>=2)");
		match(false, "(charParam>=3)");
		
		match(false, "(booleanParam>=value1)");
		match(false, "(booleanParam>=true)");
		data.put("booleanParam", new Boolean("true"));
		match(true, "(booleanParam>=true)");
	}
	
	public void testLessThanType() throws Exception {
		match(false, "(stringParam<=value1)");
		match(false, "(stringParam<=1)");
		data.put("stringParam", "2");
		match(false, "(stringParam<=1)");
		match(true, "(stringParam<=2)");
		match(true, "(stringParam<=3)");
		
		match(false, "(byteParam<=value1)");
		match(false, "(byteParam<=1)");
		data.put("byteParam", new Byte((byte)2));
		match(false, "(byteParam<=1)");
		match(true, "(byteParam<=2)");
		match(true, "(byteParam<=3)");
		
		match(false, "(shortParam<=value1)");
		match(false, "(shortParam<=1)");
		data.put("shortParam", new Short((byte)2));
		match(false, "(shortParam<=1)");
		match(true, "(shortParam<=2)");
		match(true, "(shortParam<=3)");
		
		match(false, "(intParam<=value1)");
		match(false, "(intParam<=1)");
		data.put("intParam", new Integer(2));
		match(false, "(intParam<=1)");
		match(true, "(intParam<=2)");
		match(true, "(intParam<=3)");
		
		match(false, "(longParam<=value1)");
		match(false, "(longParam<=1)");
		data.put("longParam", new Long(2L));
		match(false, "(longParam<=1)");
		match(true, "(longParam<=2)");
		match(true, "(longParam<=3)");
		
		match(false, "(floatParam<=value1)");
		match(false, "(floatParam<=1)");
		data.put("floatParam", new Float(2.0));
		match(false, "(floatParam<=1)");
		match(true, "(floatParam<=2)");
		match(true, "(floatParam<=3)");
		
		match(false, "(doubleParam<=value1)");
		match(false, "(doubleParam<=1)");
		data.put("doubleParam", new Double(2.0));
		match(false, "(doubleParam<=1)");
		match(true, "(doubleParam<=2)");
		match(true, "(doubleParam<=3)");
		
		match(false, "(charParam<=value1)");
		match(false, "(charParam<=1)");
		data.put("charParam", new Character('2'));
		match(false, "(charParam<=1)");
		match(true, "(charParam<=2)");
		match(true, "(charParam<=3)");
		
		match(false, "(booleanParam<=value1)");
		match(false, "(booleanParam<=true)");
		data.put("booleanParam", new Boolean("true"));
		match(true, "(booleanParam<=true)");
	}
	
	public void testPresent() throws Exception {
		match(false, "(param1=*)");
		data.put("param1", "value1");
		match(true, "(param1=*)");
	}

	private void match(boolean match, final String filterString) {
		FilterNode filterNode = new FilterParser(filterString).parse();
		final boolean result = filterNode.match(data);
		if (match) assertTrue(result);
		else assertFalse(result);
	}
	
}
