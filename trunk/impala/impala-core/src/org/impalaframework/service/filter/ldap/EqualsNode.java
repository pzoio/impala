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

/**
 * Implements = operator in RFC 1960 matching expression.
 * 
 * @author Phil Zoio
 */
class EqualsNode extends ItemNode {

	EqualsNode(String key, String value) {
		super(key, value);
	}

	@Override
	public String toString() {
		return wrapBrackets(getKey() + "=" + getEncodedValue());
	}

	@Override
	protected boolean matchString(String external) {
		return getValue().equals(external);
	}

	@Override
	protected boolean matchBoolean(Boolean external) {
		return TypeHelper.equalsBoolean(getValue(), external);
	}

	@Override
	protected boolean matchByte(Byte external) {
		return TypeHelper.equalsByte(getValue(), external);
	}

	@Override
	protected boolean matchCharacter(Character external) {
		return TypeHelper.equalsCharacter(getValue(), external);
	}

	@Override
	protected boolean matchDouble(Double external) {
		return TypeHelper.equalsDouble(getValue(), external);
	}

	@Override
	protected boolean matchFloat(Float external) {
		return TypeHelper.equalsFloat(getValue(), external);
	}

	@Override
	protected boolean matchInteger(Integer external) {
		return TypeHelper.equalsInteger(getValue(), external);
	}

	@Override
	protected boolean matchLong(Long external) {
		return TypeHelper.equalsLong(getValue(), external);
	}

	@Override
	protected boolean matchShort(Short external) {
		return TypeHelper.equalsShort(getValue(), external);
	}

}
