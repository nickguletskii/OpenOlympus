package org.ng200.openolympus.security;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({
			ElementType.METHOD,
			ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityAnd {
	public SecurityLeaf[]value();
}