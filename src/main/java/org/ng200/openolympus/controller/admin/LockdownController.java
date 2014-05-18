/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ng200.openolympus.controller.admin;

import org.ng200.openolympus.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LockdownController {

	@Autowired
	private PropertyService propertyService;

	@RequestMapping(value = "/admin/lockdown/disable", method = RequestMethod.POST)
	public String endLockdown() {
		this.propertyService.set("isOnLockdown", false);
		return "redirect:/admin/";
	}

	@RequestMapping(value = "/admin/lockdown/disable", method = RequestMethod.GET)
	public String endLockdownView() {
		return "admin/lockdownDisable";
	}

	@RequestMapping(value = "/admin/lockdown/enable", method = RequestMethod.POST)
	public String startLockdown() {
		this.propertyService.set("isOnLockdown", true);
		return "redirect:/admin/";
	}

	@RequestMapping(value = "/admin/lockdown/enable", method = RequestMethod.GET)
	public String startLockdownView() {
		return "admin/lockdownEnable";
	}
}
