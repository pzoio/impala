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

import junit.framework.TestCase;

public class FilterParserTest extends TestCase {
	
	public void testParse() throws Exception {
		parse(" ( cn =mytext)");
		parse("(c>=mytext)");
		parse("(c=*)");
		parse("(c>=  1)");
		parse("(c<= 2)");
		parse("(c = * anna *)");
		parse("(!(c=not))");
		parse("(&(objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))");
	}
	
	public void testCompound() throws Exception {
		parse("(&(objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))");
	}

	public void testParseToString() throws Exception {
		parseToString("(c>=mytext)");
		parseToString("(c=*)");
		parseToString("(!(c=not))");
		parseToString("(|(sn=Jensen)(!(cn=Babs J*))(a=b))");
		parseToString("(&(objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))");
	}
	
	public void testInvalidChar() throws Exception {
		try {
			parse("(|(sn=Jensen)(!(c(n=Ba(bs J*))(a=b))");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid character ) at position 22 in '(|(sn=Jensen)(!(c(n=Ba(bs J*))(a=b))'", e.getMessage());
		}
	}
	
	public void testEscapeValue() throws Exception {
		assertEquals("(c>=my\\(text)", parse("(c>=my\\(text)").toString());
		assertEquals("(c>=my\\)text)", parse("(c>=my\\)text)").toString());
		assertEquals("(c>=my\\\\text)", parse("(c>=my\\\\text)").toString());
	}
	
	public void testMissingValue() throws Exception {
		try {
			parse("(|(sn=))");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Missing filter value at position  6 in '(|(sn=))'", e.getMessage());
		}
	}
	
	public void testEscapeStars() throws Exception {
		FilterNode node = parse("(c=*)");
		assertTrue(node instanceof PresentNode);
		assertEquals("(c=*)", node.toString());
		
		node = parse("(c=\\*)");
		assertTrue(node instanceof EqualsNode);
		assertEquals("(c=\\*)", node.toString());

		node = parse("(c=start*middle\\*end)");
		assertTrue(node instanceof SubstringNode);
		assertEquals("(c=start*middle\\*end)", node.toString());

		node = parse("(c=start\\*middle\\*end)");
		assertTrue(node instanceof EqualsNode);
		assertEquals("(c=start\\*middle\\*end)", node.toString());
	}
	
	public void testExtraStartParenthesis() throws Exception {
		parseExtraStart("((c>=mytext)");
		parseExtraStart("((c=*)");
		parseExtraStart("(!((c=not))");
		parseExtraStart("(|((sn=Jensen)(!(cn=Babs J*))(a=b))");
		parseExtraStart("(&((objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))");
	}

	public void testDuffChars() throws Exception {
		parseDuffChars("(c>=mytext)stuff");
		parseDuffChars("(&(objectClass=Person)(|(!(sn=Jensen))(cn=Babs J*)))stuff)");
		parseDuffChars("(&(objectClass=Person)stuff(|(!(sn=Jensen))(cn=Babs J*))))");
		parseMissingEnd("(&(objectClass=Person)(|(!(sn=Jensen)stuff)(cn=Babs J*))))");
	}

	public void testTooManyParenthesis() throws Exception {
		parseTooMany("(c>=mytext))");
		parseTooMany("(c=*))");
		parseTooMany("(!(c=not))");
		parseTooMany("(&(objectClass=Person)(|(!(sn=Jensen))(cn=Babs J*))))");
	}

	public void testMissingParenthesis() throws Exception {
		parseMissingEnd("(c>=mytext");
		parseMissingEnd("(c=*");
		parseMissingEnd("(!(c=not");
		parseMissingEnd("(&(objectClass=Person)(|(!(sn=Jensen))(cn=Babs J*))");
	}
	
	private void parseExtraStart(String string) {
		try {
			parse(string).toString();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(e.getMessage().startsWith("Extra left parenthesis"));
		}
	}

	private void parseTooMany(String string) {
		try {
			parse(string).toString();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(e.getMessage().startsWith("Extra end parenthesis"));
		}
	}

	private void parseMissingEnd(String string) {
		try {
			parse(string).toString();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().startsWith("Missing end parenthesis"));
		}
	}
	
	private void parseDuffChars(String string) {
		try {
			parse(string).toString();
			fail();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().startsWith("Invalid char"));
		}
	}

	private void parseToString(String string) {
		assertEquals(string, parse(string).toString());
	}

	private FilterNode parse(final String text) {
		FilterParser impl = new FilterParser(text);
		final FilterNode node = impl.parse();
		System.out.println(node);
		return node;
	}
}
