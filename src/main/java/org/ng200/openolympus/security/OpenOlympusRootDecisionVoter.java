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
package org.ng200.openolympus.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.FindAnnotation;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.NamedParameter;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.SecurityClearanceVerificationService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.util.AnnotationExtraUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class OpenOlympusRootDecisionVoter
		implements AccessDecisionVoter<Object> {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenOlympusRootDecisionVoter.class);
	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Autowired
	private SecurityClearanceVerificationService securityClearanceVerificationService;

	@Autowired
	private UserService userService;

	private final Map<Class<?>, Object> predicateBeans = new HashMap<>();

	private final Map<Method, Map<String, Integer>> methodParameterIndex = new HashMap<>();

	private Object[] computeArguments(Method method,
			MethodInvocation invocation) {
		final Object[] objs = Stream.<Parameter> of(method.getParameters())
				.map(parameter -> this.getObjectForParameter(invocation,
						parameter))
				.toArray();
		return objs;
	}

	private String getName(Parameter parameter) {
		if (parameter.isAnnotationPresent(RequestParam.class)) {
			final RequestParam requestParam = AnnotationUtils.findAnnotation(
					parameter, RequestParam.class);
			return requestParam.value() == null
					? requestParam.name()
					: requestParam.value();
		}
		if (parameter.isAnnotationPresent(PathVariable.class)) {
			return AnnotationUtils.findAnnotation(
					parameter, PathVariable.class).value();
		}
		if (parameter.isAnnotationPresent(NamedParameter.class)) {
			return AnnotationUtils
					.findAnnotation(parameter, NamedParameter.class).value();
		}
		return parameter.getName();
	}

	@SuppressWarnings("unchecked")
	private Object getObjectForParameter(MethodInvocation invocation,
			Parameter parameter) {
		if (parameter.isAnnotationPresent(FindAnnotation.class)) {
			return AnnotationExtraUtils.findAnnotation(
					invocation.getMethod(),
					invocation.getMethod().getDeclaringClass(),
					(Class) parameter.getType());
		}
		if (parameter.isAnnotationPresent(CurrentUser.class)) {
			return Optional
					.ofNullable(SecurityContextHolder.getContext())
					.map(c -> c.getAuthentication())
					.map(a -> this.userService.getUserByUsername(a.getName()))
					.orElse(null);
		}
		if (parameter.isAnnotationPresent(
				org.ng200.openolympus.security.annotations.Parameter.class)) {
			final String parameterName = AnnotationUtils.findAnnotation(
					parameter,
					org.ng200.openolympus.security.annotations.Parameter.class)
					.value();
			final Object objectForParameterName = this
					.getObjectForParameterName(
							invocation,
							parameterName);
			return objectForParameterName;
		}
		return this.getObjectForParameterName(invocation, parameter.getName());
	}

	private Object getObjectForParameterName(MethodInvocation invocation,
			String name) {
		this.methodParameterIndex.computeIfAbsent(invocation.getMethod(),
				(x) -> {
					final Parameter[] invokedMethodParameters = invocation
							.getMethod()
							.getParameters();

					final Map<String, Integer> map = new HashMap<>();
					for (int i = 0; i < invokedMethodParameters.length; i++) {
						map.put(this.getName(invokedMethodParameters[i]), i);
					}

					return map;
				});
		final Integer idx = this.methodParameterIndex
				.get(invocation.getMethod())
				.get(name);

		if (idx == null) {
			throw new IllegalArgumentException(
					"An argument that isn't present in the invoked method was requested");
		}
		return invocation.getArguments()[idx];
	}

	private Object getPredicate(
			Class<?> predicateClass) {
		return this.predicateBeans.computeIfAbsent(predicateClass,
				(c) -> this.autowireCapableBeanFactory
						.createBean(c));
	}

	private int processSecurityAnd(SecurityAnd securityAnd,
			MethodInvocation invocation) {
		for (final SecurityLeaf securityLeaf : securityAnd.value()) {
			if (this.processSecurityLeaf(securityLeaf,
					invocation) == AccessDecisionVoter.ACCESS_DENIED) {
				return AccessDecisionVoter.ACCESS_DENIED;
			}
		}
		return AccessDecisionVoter.ACCESS_GRANTED;
	}

	private int processSecurityLeaf(SecurityLeaf securityLeaf,
			MethodInvocation invocation) {
		final SecurityClearanceType minimal = securityLeaf.value();
		if (!this.securityClearanceVerificationService
				.doesCurrentSecurityContextHaveClearance(minimal)) {
			// Early exit if minimal conditions not satisfied
			return AccessDecisionVoter.ACCESS_DENIED;
		}
		for (final Class<?> predicate : securityLeaf.predicates()) {
			for (final Method method : predicate.getMethods()) {
				if (method.getAnnotationsByType(
						MethodSecurityPredicate.class).length > 0) {
					if (!SecurityClearanceType.class
							.isAssignableFrom(method.getReturnType())) {
						throw new IllegalMethodReturnTypeException(
								"Methods annotated with @MethodSecurityPredicate are supposed to return SecurityClearanceType.");
					}
					try {
						final SecurityClearanceType clearanceType = (SecurityClearanceType) method
								.invoke(
										this.getPredicate(predicate),
										this.computeArguments(method,
												invocation));
						if (!this.securityClearanceVerificationService
								.doesCurrentSecurityContextHaveClearance(
										clearanceType)) {
							return AccessDecisionVoter.ACCESS_DENIED;
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						throw new GeneralNestedRuntimeException(
								"Couldn't execute @MethodSecurityPredicate annotated method! Method: "
										+ method,
								e);
					}
				}
			}
		}
		return AccessDecisionVoter.ACCESS_GRANTED;
	}

	private int processSecurityOr(SecurityOr securityOr,
			MethodInvocation invocation) {
		if (securityOr.allowSuperuser()
				&& this.securityClearanceVerificationService
						.doesCurrentSecurityContextHaveClearance(
								SecurityClearanceType.SUPERUSER)) {
			return AccessDecisionVoter.ACCESS_GRANTED;
		}

		for (final SecurityAnd securityAnd : securityOr.value()) {
			if (this.processSecurityAnd(securityAnd,
					invocation) == AccessDecisionVoter.ACCESS_GRANTED) {
				return AccessDecisionVoter.ACCESS_GRANTED;
			}
		}
		return AccessDecisionVoter.ACCESS_DENIED;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return MethodInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof PreInvocationAttribute;
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		try {
			if (!(object instanceof MethodInvocation)) {
				return AccessDecisionVoter.ACCESS_ABSTAIN;
			}
			final MethodInvocation invocation = (MethodInvocation) object;

			final int vote1 = Optional
					.ofNullable(AnnotationUtils.findAnnotation(
							invocation.getMethod().getDeclaringClass(),
							SecurityOr.class))
					.map(x -> (Integer) this.processSecurityOr(x, invocation))
					.orElse(AccessDecisionVoter.ACCESS_ABSTAIN);

			final int vote2 = Optional
					.ofNullable(AnnotationUtils.findAnnotation(
							invocation.getMethod(),
							SecurityOr.class))
					.map(x -> (Integer) this.processSecurityOr(x, invocation))
					.orElse(AccessDecisionVoter.ACCESS_ABSTAIN);

			if (vote1 == AccessDecisionVoter.ACCESS_DENIED
					|| vote2 == AccessDecisionVoter.ACCESS_DENIED) {
				return AccessDecisionVoter.ACCESS_DENIED;
			}

			if (vote1 == AccessDecisionVoter.ACCESS_GRANTED
					|| vote2 == AccessDecisionVoter.ACCESS_GRANTED) {
				return AccessDecisionVoter.ACCESS_GRANTED;
			}
		} catch (final Exception e) {
			OpenOlympusRootDecisionVoter.logger
					.error("SECURITY SYSTEM FAILURE: {}", e);
			return AccessDecisionVoter.ACCESS_DENIED;
		}
		return AccessDecisionVoter.ACCESS_DENIED;
	}

}
