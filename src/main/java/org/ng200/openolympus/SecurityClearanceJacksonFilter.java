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
