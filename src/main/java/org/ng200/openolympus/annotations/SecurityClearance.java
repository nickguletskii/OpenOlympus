package org.ng200.openolympus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ng200.openolympus.SecurityClearanceType;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {
					ElementType.METHOD,
					ElementType.FIELD,
					ElementType.PACKAGE,
					ElementType.TYPE,
					ElementType.TYPE_USE
})
public @interface SecurityClearance {
	SecurityClearanceType value() default SecurityClearanceType.ANNONYMOUS;
}
