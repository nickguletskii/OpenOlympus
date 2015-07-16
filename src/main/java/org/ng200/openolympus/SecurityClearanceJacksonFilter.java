/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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
package org.ng200.openolympus;

import org.ng200.openolympus.annotations.SecurityClearance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class SecurityClearanceJacksonFilter extends SimpleBeanPropertyFilter {

	private static final Logger logger = LoggerFactory
			.getLogger(SecurityClearanceJacksonFilter.class);

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		logger.info("Should I include {}?", writer.getFullName().toString());
		SecurityClearance requestedClearance = writer
				.findAnnotation(SecurityClearance.class);
		if (requestedClearance == null || requestedClearance
				.value() == SecurityClearanceType.ANNONYMOUS)
			return true;
		return include(requestedClearance);
	}

	@Override
	protected boolean include(PropertyWriter writer) {
		logger.info("Should I include {}?", writer.getFullName().toString());
		SecurityClearance requestedClearance = writer
				.findAnnotation(SecurityClearance.class);
		if (requestedClearance == null || requestedClearance
				.value() == SecurityClearanceType.ANNONYMOUS)
			return true;
		return include(requestedClearance);
	}

	private boolean include(SecurityClearance requestedClearance) {
		logger.info("Requested clearance: {}", requestedClearance.value());
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities()
				.stream()
				.anyMatch(
						(authority) -> authority instanceof Authorities.OlympusAuthority
								&&
								((Authorities.OlympusAuthority) authority)
										.getClearanceType() == requestedClearance
												.value());
	}

}
