package org.ng200.openolympus.controller;

import org.opensaml.util.resource.ClasspathResource;
import org.opensaml.util.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SinglepageController {

	private static final Logger logger = LoggerFactory.getLogger(SinglepageController.class);

	@RequestMapping("/**")
	public String singlepage() throws ResourceException {
		return "/singlepage.html";
	}
}
