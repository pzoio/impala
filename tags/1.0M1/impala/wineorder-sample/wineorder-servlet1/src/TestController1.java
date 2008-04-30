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

import interfaces.WineMerchant;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TestController1 extends MultiActionController {

	private WineMerchant wineMerchant;

	public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("myparam", "1111");
		map.put("anotherparam", "" + wineMerchant.getWinesOfVintage(1996).size());

		ModelAndView mav = new ModelAndView("test", map);
		return mav;
	}

	public void setWineMerchant(WineMerchant wineMerchant) {
		this.wineMerchant = wineMerchant;
	}

}
