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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.SecurityClearanceUnlessPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class SecurityClearanceJacksonFilter extends SimpleBeanPropertyFilter {

	private static final Logger logger = LoggerFactory
			.getLogger(SecurityClearanceJacksonFilter.class);

	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	private final Map<Class<? extends SecurityClearanceUnlessPredicate>, SecurityClearanceUnlessPredicate> predicateBeans = new HashMap<>();

	private SecurityClearanceUnlessPredicate getPredicate(
			Class<? extends SecurityClearanceUnlessPredicate> predicateClass) {
		return this.predicateBeans.computeIfAbsent(predicateClass,
				(c) -> this.autowireCapableBeanFactory
						.createBean(c));
	}

	protected boolean include(Object object, BeanPropertyWriter writer) {
		final SecurityClearanceRequired requestedClearance = writer
				.findAnnotation(SecurityClearanceRequired.class);
		SecurityClearanceJacksonFilter.logger.debug(
				"Voting on object: {}, property: {}, clearance: {}",
				object,
				writer.getFullName(),
				requestedClearance);

		return this.include(object, requestedClearance, writer.getFullName());
	}

	protected boolean include(Object object, PropertyWriter writer) {

		final SecurityClearanceRequired requestedClearance = writer
				.findAnnotation(SecurityClearanceRequired.class);
		SecurityClearanceJacksonFilter.logger.debug(
				"Voting on object: {}, property: {}, clearance: {}",
				object,
				writer.getFullName(),
				requestedClearance);

		return this.include(object, requestedClearance, writer.getFullName());
	}

	private boolean include(Object object,
			SecurityClearanceRequired requestedClearance,
			PropertyName propertyName) {
		final User user = Optional.ofNullable(SecurityContextHolder
				.getContext().getAuthentication())
				.map(authentication -> (User) authentication
						.getPrincipal())
				.orElse(null);
		if (Stream.of(requestedClearance.unless())
				.map(predicateClass -> this.getPredicate(predicateClass)
						.objectMatches(user, object))
				.reduce(false, (l, r) -> l || r)) {
			SecurityClearanceJacksonFilter.logger.debug(
					"Voted 'accept'  on object: {}, property: {}, clearance: {} because an unless predicate voted to allow access.",
					object, propertyName,
					requestedClearance);
			return true;
		}
		if (requestedClearance == null || requestedClearance
				.value() == SecurityClearanceType.ANONYMOUS) {
			SecurityClearanceJacksonFilter.logger.debug(
					"Voted 'accept'  on object: {}, property: {}, clearance: {} because anonymous access is allowed",
					object, propertyName,
					requestedClearance);
			return true;
		}
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			SecurityClearanceJacksonFilter.logger.debug(
					"Voted 'deny' on object: {}, property: {}, clearance: {} because there is no security context",
					object, propertyName, requestedClearance);
			return false;
		}
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& requestedClearance
						.value() == SecurityClearanceType.LOGGED_IN) {
			SecurityClearanceJacksonFilter.logger.debug(
					"Voted 'accept' on object: {}, clearance {} because any logged in user has access to this information",
					object, propertyName, requestedClearance);

			return true;
		}
		SecurityClearanceJacksonFilter.logger.debug("Olympus authorities: {}",
				SecurityContextHolder.getContext().getAuthentication()
						.getAuthorities());
		SecurityClearanceJacksonFilter.logger.debug(
				"Olympus authority clearances: {}",
				SecurityContextHolder.getContext().getAuthentication()
						.getAuthorities()
						.stream()
						.filter((
								authority) -> authority instanceof Authorities.OlympusAuthority)
						.map(authority -> ((Authorities.OlympusAuthority) authority)
								.getClearanceType())
						.collect(Collectors.toList()));
		final boolean flag = SecurityContextHolder.getContext()
				.getAuthentication()
				.getAuthorities()
				.stream()
				.anyMatch(
						(authority) -> authority instanceof Authorities.OlympusAuthority
								&&
								((Authorities.OlympusAuthority) authority)
										.getClearanceType() == requestedClearance
												.value());

		SecurityClearanceJacksonFilter.logger.debug(
				"Voted {} on object: {}, property: {}, clearance: {} because of user's authorities",
				flag,
				object, propertyName, requestedClearance);
		return flag;
	}

	@Override
	public void serializeAsElement(Object elementValue, JsonGenerator jgen,
			SerializerProvider provider, PropertyWriter writer)
					throws Exception {
		if (this.includeElement(elementValue)) {
			writer.serializeAsElement(elementValue, jgen, provider);
		}
	}

	@Override
	public void serializeAsField(Object bean, JsonGenerator jgen,
			SerializerProvider provider, BeanPropertyWriter writer)
					throws Exception {
		if (this.include(bean, writer)) {
			writer.serializeAsField(bean, jgen, provider);
		} else if (!jgen.canOmitFields()) { // since 2.3
			writer.serializeAsOmittedField(bean, jgen, provider);
		}
	}

	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen,
			SerializerProvider provider, PropertyWriter writer)
					throws Exception {
		if (this.include(pojo, writer)) {
			writer.serializeAsField(pojo, jgen, provider);
		} else if (!jgen.canOmitFields()) { // since 2.3
			writer.serializeAsOmittedField(pojo, jgen, provider);
		}
	}

}
