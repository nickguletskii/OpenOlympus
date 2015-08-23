package org.ng200.openolympus.security;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({
			ElementType.METHOD,
			ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@PreAuthorize("")
public @interface SecurityOr {
	public SecurityAnd[]value();

	public boolean allowSuperuser() default true;
}
