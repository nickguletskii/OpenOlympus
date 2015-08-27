package org.ng200.openolympus.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ng200.openolympus.jooq.enums.ContestPermissionType;

@Target({
          ElementType.METHOD,
          ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContestPermissionRequired {
	public ContestPermissionType value();
}