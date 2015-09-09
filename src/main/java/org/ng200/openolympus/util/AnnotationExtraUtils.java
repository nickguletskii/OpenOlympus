package org.ng200.openolympus.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

public class AnnotationExtraUtils {
	
	public static <A extends Annotation> A findAnnotation(Method method,
			Class<?> targetClass,
			Class<A> annotationClass) {
		Method specificMethod = ClassUtils.getMostSpecificMethod(method,
				targetClass);
		return Stream.<Supplier<? extends A>> of(
				() -> AnnotationUtils.findAnnotation(specificMethod,
						annotationClass),
				() -> AnnotationUtils.findAnnotation(method,
						annotationClass),
				() -> AnnotationUtils.findAnnotation(
						specificMethod.getDeclaringClass(),
						annotationClass))
				.map(x -> x.get())
				.filter(x -> x != null)
				.findFirst().orElse(null);
	}

}
