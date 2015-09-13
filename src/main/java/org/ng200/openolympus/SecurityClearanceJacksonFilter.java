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
import java.util.stream.Stream;

import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.SecurityClearancePredicate;
import org.ng200.openolympus.services.SecurityClearanceVerificationService;
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

	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Autowired
	private SecurityClearanceVerificationService securityClearanceVerificationService;

	private final Map<Class<? extends SecurityClearancePredicate>, SecurityClearancePredicate> predicateBeans = new HashMap<>();

	private SecurityClearancePredicate getPredicate(
			Class<? extends SecurityClearancePredicate> predicateClass) {
		return this.predicateBeans.computeIfAbsent(predicateClass,
				(c) -> this.autowireCapableBeanFactory
						.createBean(c));
	}

	protected boolean include(Object object, BeanPropertyWriter writer) {
		final SecurityClearanceRequired requestedClearance = writer
				.findAnnotation(SecurityClearanceRequired.class);
		return this.include(object, requestedClearance, writer.getFullName());
	}

	protected boolean include(Object object, PropertyWriter writer) {
		final SecurityClearanceRequired requestedClearance = writer
				.findAnnotation(SecurityClearanceRequired.class);
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

		// No annotation -> no restriction.
		if (requestedClearance == null) {
			return true;
		}

		return securityClearanceVerificationService
				.doesCurrentSecurityContextHaveClearance(
						requestedClearance.minimumClearance())
				&&
				Stream.of(requestedClearance.predicates())
						.map(predicateClass -> this.getPredicate(predicateClass)
								.getRequiredClearanceForObject(user, object))
						.map(req -> req != SecurityClearanceType.DENIED
								? securityClearanceVerificationService
										.doesCurrentSecurityContextHaveClearance(
												req)
								: false)
						.reduce(true, (l, r) -> l && r);
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
