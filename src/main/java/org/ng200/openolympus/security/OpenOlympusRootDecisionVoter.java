package org.ng200.openolympus.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.Principal;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class OpenOlympusRootDecisionVoter
		implements AccessDecisionVoter<Object> {

	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Autowired
	private SecurityClearanceVerificationService securityClearanceVerificationService;

	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof PreInvocationAttribute;
	}

	public boolean supports(Class<?> clazz) {
		return MethodInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		if (!(object instanceof MethodInvocation))
			return ACCESS_ABSTAIN;
		MethodInvocation invocation = (MethodInvocation) object;
		SecurityOr securityOr = AnnotationUtils.findAnnotation(
				invocation.getMethod(),
				SecurityOr.class);

		if (securityOr == null) {
			return ACCESS_GRANTED;
		}

		return processSecurityOr(securityOr, invocation);
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
			if (processSecurityAnd(securityAnd, invocation) == ACCESS_GRANTED)
				return ACCESS_GRANTED;
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
								"Couldn't execute @MethodSecurityPredicate annotated method!",
								e);
					}
				}
			}
		}
		return ACCESS_GRANTED;
	}

	private Object[] computeArguments(Method method,
			MethodInvocation invocation) {
		return Stream.<Parameter> of(method.getParameters())
				.map(parameter -> getObjectForParameter(invocation,
						parameter))
				.toArray();
	}

	@SuppressWarnings("unchecked")
	private Object getObjectForParameter(MethodInvocation invocation,
			Parameter parameter) {
		if (parameter.isAnnotationPresent(FindAnnotation.class)) {
			return AnnotationUtils.findAnnotation(
					invocation.getMethod(),
					(Class) parameter.getType());
		}
		if (parameter.isAnnotationPresent(CurrentUser.class)) {
			return (User) Optional
					.ofNullable(SecurityContextHolder.getContext())
					.map(c -> c.getAuthentication()).map(a -> a.getPrincipal())
					.orElse(null);
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
						map.put(invokedMethodParameters[i].getName(), i);
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

}
