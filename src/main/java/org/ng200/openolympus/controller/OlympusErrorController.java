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
package org.ng200.openolympus.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ng200.openolympus.cerberus.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OlympusErrorController implements ErrorController {

	@Value("${error.path:/error}")
	private String errorPath;

	@RequestMapping(value = "${error.path:/error}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(
			final HttpServletRequest request) {
		final ServletRequestAttributes attributes = new ServletRequestAttributes(
				request);
		final String trace = request.getParameter("trace");
		final Map<String, Object> extracted = this.extract(attributes,
				trace != null && !"false".equals(trace.toLowerCase()), true);
		final HttpStatus statusCode = this.getStatus((Integer) extracted
				.get("status"));
		return new ResponseEntity<Map<String, Object>>(extracted, statusCode);
	}

	@RequestMapping(value = "${error.path:/error}", produces = "text/html")
	public ModelAndView errorHtml(final HttpServletRequest request) {
		final Map<String, Object> map = this.extract(
				new ServletRequestAttributes(request), false, false);
		return new ModelAndView("error", map);
	}

	@Override
	public Map<String, Object> extract(final RequestAttributes attributes,
			final boolean b1, final boolean b2) {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("timestamp", new Date());
		try {
			Throwable error = (Throwable) attributes.getAttribute(
					"javax.servlet.error.exception",
					RequestAttributes.SCOPE_REQUEST);
			final Object obj = attributes.getAttribute(
					"javax.servlet.error.status_code",
					RequestAttributes.SCOPE_REQUEST);
			int status = 999;
			if (obj != null) {
				status = (Integer) obj;
			} else {
			}
			map.put("status", status);
			map.put("statusPhrase", HttpStatus.valueOf(status)
					.getReasonPhrase());
			final List<Throwable> exceptions = new ArrayList<Throwable>();
			while (error != null) {
				exceptions.add(error);
				error = error.getCause();
			}
			map.put("exceptions", exceptions);
			return map;
		} catch (final Exception ex) {
			map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			map.put("statusPhrase",
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			map.put("exceptions", Lists.from(ex));
			return map;
		}
	}

	@Override
	public String getErrorPath() {
		return this.errorPath;
	}

	private HttpStatus getStatus(final Integer value) {
		try {
			return HttpStatus.valueOf(value);
		} catch (final Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}