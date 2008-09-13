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

package tests;

import interfaces.EntryDAO;

import java.util.Collection;

import org.impalaframework.facade.Impala;
import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.InteractiveTestRunner;

import test.BaseDataTest;
import classes.Entry;

public class EntryDAOTest extends BaseDataTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(EntryDAOTest.class);
	}

	public void testDAO() {

		EntryDAO dao = Impala.getBean("wineDAO", EntryDAO.class);

		Entry wine = new Entry();
		wine.setTitle("Cabernet");
		wine.setCount(1996);
		dao.save(wine);

		Collection<Entry> winesOfVintage = dao.getWinesOfVintage(1996);
		System.out.println("Wines of vintage 1996: " + winesOfVintage.size());
		assertEquals(1, winesOfVintage.size());

		wine.setCount(2000);
		dao.update(wine);

		Entry updated = dao.findById(wine.getId());
		assertEquals(2000, updated.getCount());

	}

	public RootModuleDefinition getModuleDefinition() {
		return new InternalModuleDefinitionSource(new String[]{"example-dao", "example-hibernate"}).getModuleDefinition();
	}

}