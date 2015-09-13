package org.ng200.openolympus.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

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
