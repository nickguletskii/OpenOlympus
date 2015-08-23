package org.ng200.openolympus.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ng200.openolympus.SecurityClearanceType;

@Target({
			ElementType.METHOD,
			ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityLeaf {
	public SecurityClearanceType value() default SecurityClearanceType.ANONYMOUS;

	public Class<?>[]predicates() default {};
}
