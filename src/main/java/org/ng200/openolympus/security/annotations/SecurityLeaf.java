package org.ng200.openolympus.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.security.DynamicSecurityPredicate;

@Target({
          ElementType.METHOD,
          ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityLeaf {
	public SecurityClearanceType value() default SecurityClearanceType.ANONYMOUS;

	public Class<? extends DynamicSecurityPredicate>[]predicates() default {};
}
