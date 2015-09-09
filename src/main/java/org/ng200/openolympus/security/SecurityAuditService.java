package org.ng200.openolympus.security;

import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import org.ng200.openolympus.exceptions.SecurityAuditCatastrophicFailureException;
import org.ng200.openolympus.security.annotations.PredicateDocumentation;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Service
public class SecurityAuditService implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory
			.getLogger(SecurityAuditService.class);

	@Autowired
	public SecurityAuditService(
			RequestMappingHandlerMapping handlerMapping) {
		StringBuilder builder = new StringBuilder();
		handlerMapping.getHandlerMethods()
				.forEach((RequestMappingInfo requestMappingInfo,
						HandlerMethod handlerMethod) -> {
					processMethod(requestMappingInfo, handlerMethod, builder);
				});
		logger.info(builder.toString());
	}

	private void processMethod(RequestMappingInfo requestMappingInfo,
			HandlerMethod handlerMethod, StringBuilder builder) {
		builder.append("URLS {")
				.append(requestMappingInfo.getPatternsCondition().getPatterns()
						.stream()
						.collect(Collectors.joining(", ")))
				.append("} ");

		SecurityOr or = AnnotationUtils.findAnnotation(
				handlerMethod.getMethod(),
				SecurityOr.class);

		if (or == null)
			or = AnnotationUtils.findAnnotation(
					handlerMethod.getMethod().getDeclaringClass(),
					SecurityOr.class);

		if (or == null) {
			builder.append("NOT SECURED!!!\n");
			return;
		}

		if ((handlerMethod.getMethod().getModifiers() & Modifier.PUBLIC) == 0) {
			throw new SecurityAuditCatastrophicFailureException(
					MessageFormat.format(
							"SECURITY AUDIT FAILURE: A REQUEST MAPPING METHOD WAS ANNOTATED WITH SECURITY ANNOTATION, YET IT IS NOT PUBLIC! METHOD: {0}",
							handlerMethod.getMethod()));
		}

		if (!or.allowSuperuser()) {
			builder.append(" Superuser not allowed.");
		}

		builder.append("\n");

		boolean flag1 = false;

		for (SecurityAnd and : or.value()) {
			if (flag1)
				builder.append(" OR\n");
			flag1 = true;

			boolean flag2 = false;

			for (SecurityLeaf leaf : and.value()) {
				if (flag2)
					builder.append("  AND\n");

				flag2 = true;

				builder.append("  IF USER HAS PERMISSION ")
						.append(leaf.value().toString())
						.append("\n");

				for (Class<? extends DynamicSecurityPredicate> predicate : leaf
						.predicates()) {
					PredicateDocumentation documentation = AnnotationUtils
							.findAnnotation(predicate,
									PredicateDocumentation.class);
					if (documentation == null) {
						builder.append("  AND ")
								.append(predicate.getName()).append("\n");
					} else {
						builder.append("  AND\n");

						boolean flag3 = false;

						for (String val : documentation.value()) {
							if (flag3)
								builder.append("   OR ");
							else
								builder.append("      ");

							flag3 = true;

							builder.append(val).append("\n");
						}
					}
				}

				builder.append("\n");
			}
			builder.append("\n");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
	}
}
