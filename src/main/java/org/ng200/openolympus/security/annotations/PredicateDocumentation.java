package org.ng200.openolympus.security.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PredicateDocumentation {
	public String[]value();
}
