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
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.FindAnnotation;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
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

	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof PreInvocationAttribute;
	}

	public boolean supports(Class<?> clazz) {
		return MethodInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		try {
			if (!(object instanceof MethodInvocation))
				return ACCESS_ABSTAIN;
			MethodInvocation invocation = (MethodInvocation) object;

			int vote1 = Optional.ofNullable(AnnotationUtils.findAnnotation(
					invocation.getMethod().getDeclaringClass(),
					SecurityOr.class))
					.map(x -> (Integer) processSecurityOr(x, invocation))
					.orElse(ACCESS_ABSTAIN);

			int vote2 = Optional.ofNullable(AnnotationUtils.findAnnotation(
					invocation.getMethod(),
					SecurityOr.class))
					.map(x -> (Integer) processSecurityOr(x, invocation))
					.orElse(ACCESS_ABSTAIN);

			if (vote1 == ACCESS_DENIED || vote2 == ACCESS_DENIED)
				return ACCESS_DENIED;

			if (vote1 == ACCESS_GRANTED || vote2 == ACCESS_GRANTED)
				return ACCESS_GRANTED;
		} catch (Exception e) {
			logger.error("SECURITY SYSTEM FAILURE: {}", e);
			return ACCESS_DENIED;
		}
		return ACCESS_DENIED;
	}

	private int processSecurityOr(SecurityOr securityOr,
			MethodInvocation invocation) {
		if (securityOr.allowSuperuser()
				&& securityClearanceVerificationService
						.doesCurrentSecurityContextHaveClearance(
								SecurityClearanceType.SUPERUSER)) {
			return ACCESS_GRANTED;
		}

		for (SecurityAnd securityAnd : securityOr.value()) {
			if (processSecurityAnd(securityAnd, invocation) == ACCESS_GRANTED) {
				return ACCESS_GRANTED;
			}
		}
		return ACCESS_DENIED;
	}

	private int processSecurityAnd(SecurityAnd securityAnd,
			MethodInvocation invocation) {
		for (SecurityLeaf securityLeaf : securityAnd.value()) {
			if (processSecurityLeaf(securityLeaf, invocation) == ACCESS_DENIED)
				return ACCESS_DENIED;
		}
		return ACCESS_GRANTED;
	}

	private final Map<Class<?>, Object> predicateBeans = new HashMap<>();

	private Object getPredicate(
			Class<?> predicateClass) {
		return this.predicateBeans.computeIfAbsent(predicateClass,
				(c) -> this.autowireCapableBeanFactory
						.createBean(c));
	}

	private final Map<Method, Map<String, Integer>> methodParameterIndex = new HashMap<>();

	private int processSecurityLeaf(SecurityLeaf securityLeaf,
			MethodInvocation invocation) {
		SecurityClearanceType minimal = securityLeaf.value();
		if (!securityClearanceVerificationService
				.doesCurrentSecurityContextHaveClearance(minimal)) {
			// Early exit if minimal conditions not satisfied
			return ACCESS_DENIED;
		}
		for (Class<?> predicate : securityLeaf.predicates()) {
			for (Method method : predicate.getMethods()) {
				if (method.getAnnotationsByType(
						MethodSecurityPredicate.class).length > 0) {
					if (!SecurityClearanceType.class
							.isAssignableFrom(method.getReturnType())) {
						throw new IllegalMethodReturnTypeException(
								"Methods annotated with @MethodSecurityPredicate are supposed to return SecurityClearanceType.");
					}
					try {
						SecurityClearanceType clearanceType = (SecurityClearanceType) method
								.invoke(
										getPredicate(predicate),
										computeArguments(method, invocation));
						if (!securityClearanceVerificationService
								.doesCurrentSecurityContextHaveClearance(
										clearanceType)) {
							return ACCESS_DENIED;
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
		return ACCESS_GRANTED;
	}

	private Object[] computeArguments(Method method,
			MethodInvocation invocation) {
		Object[] objs = Stream.<Parameter> of(method.getParameters())
				.map(parameter -> getObjectForParameter(invocation,
						parameter))
				.toArray();
		return objs;
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
			return (User) Optional
					.ofNullable(SecurityContextHolder.getContext())
					.map(c -> c.getAuthentication())
					.map(a -> userService.getUserByUsername(a.getName()))
					.orElse(null);
		}
		if (parameter.isAnnotationPresent(
				org.ng200.openolympus.security.annotations.Parameter.class)) {
			String parameterName = AnnotationUtils.findAnnotation(
					parameter,
					org.ng200.openolympus.security.annotations.Parameter.class)
					.value();
			Object objectForParameterName = getObjectForParameterName(
					invocation,
					parameterName);
			return objectForParameterName;
		}
		return getObjectForParameterName(invocation, parameter.getName());
	}

	private Object getObjectForParameterName(MethodInvocation invocation,
			String name) {
		methodParameterIndex.computeIfAbsent(invocation.getMethod(),
				(x) -> {
					Parameter[] invokedMethodParameters = invocation.getMethod()
							.getParameters();

					Map<String, Integer> map = new HashMap<>();
					for (int i = 0; i < invokedMethodParameters.length; i++) {
						map.put(getName(invokedMethodParameters[i]), i);
					}

					return map;
				});
		Integer idx = methodParameterIndex.get(invocation.getMethod())
				.get(name);

		if (idx == null) {
			throw new IllegalArgumentException(
					"An argument that isn't present in the invoked method was requested");
		}
		return invocation.getArguments()[idx];
	}

	private String getName(Parameter parameter) {
		if (parameter.isAnnotationPresent(RequestParam.class)) {
			RequestParam requestParam = AnnotationUtils.findAnnotation(
					parameter, RequestParam.class);
			return requestParam.value() == null
					? requestParam.name()
					: requestParam.value();
		}
		if (parameter.isAnnotationPresent(PathVariable.class)) {
			return AnnotationUtils.findAnnotation(
					parameter, PathVariable.class).value();
		}
		return parameter.getName();
	}

}
