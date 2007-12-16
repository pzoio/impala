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

import interfaces.WineMerchant;

import java.util.Collection;

import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleBeansetPluginSpec;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.testrun.PluginTestRunner;

import classes.Wine;

public class WineMerchantTest extends BaseWineMerchantTest {

	public static void main(String[] args) {
		System.setProperty("impala.parent.project", "wineorder");
		PluginTestRunner.run(WineMerchantTest.class);
	}

	public void testVintage() {

		baseClassOperation();
		
		WineMerchant merchant = DynamicContextHolder.getBean("wineMerchant", WineMerchant.class);

		Wine wine = new Wine();
		wine.setId(1L);
		wine.setColor("red");
		wine.setVineyard("Chateau X");
		wine.setTitle("Cabernet");
		wine.setVintage(1996);
		merchant.addWine(wine);

		Collection<Wine> wines = merchant.getWinesOfVintage(1996);
		assertEquals(1, wines.size());

	}

	public ParentSpec getPluginSpec() {
		SimplePluginSpecBuilder spec = new SimplePluginSpecBuilder(new String[] { "parent-context.xml", "merchant-context.xml" }, 
						new String[] {
						"wineorder-hibernate", "wineorder-dao" });
		
		ParentSpec parent = spec.getPluginSpec();
		new SimpleBeansetPluginSpec(parent, "wineorder-merchant");
		
		return spec.getPluginSpec();
	}

}