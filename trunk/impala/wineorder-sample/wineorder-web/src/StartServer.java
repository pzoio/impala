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

import java.io.File;

import org.impalaframework.web.StartJetty;
import org.impalaframework.web.WebConstants;

public class StartServer {
	public static void main(String[] args) {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "web-locations.properties");
		System.setProperty("org.mortbay.log.class", "org.mortbay.log.StdErrLog");
		System.setProperty("workspace.root", new File("../").getAbsolutePath());
		System.setProperty("impala.root.projects", "wineorder");
		System.out.println(System.getProperty("workspace.root"));
		StartJetty.main(new String[]{"8080", "../wineorder-web/context", "/wineorder"});
	}
}
