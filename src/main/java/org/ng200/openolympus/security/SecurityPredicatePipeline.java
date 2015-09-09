package org.ng200.openolympus.security;

import java.util.Optional;
import java.util.function.Function;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityPredicatePipeline {

	private User user;

	private Decision decision = Decision.NO_DECISION;

	private DSLContext dslContext;

	private SecurityPredicatePipeline(DSLContext dslContext) {
		this.dslContext = dslContext;
		user = (User) Optional.ofNullable(SecurityContextHolder.getContext())
				.map(c -> c.getAuthentication()).map(a -> a.getPrincipal())
				.orElse(null);
	}

	public SecurityPredicatePipeline defaultSecurity() {
		if (decision != Decision.NO_DECISION)
			return this;

		if (user != null && user.getSuperuser())
			decision = Decision.ACCEPT;

		return this;
	}

	public SecurityPredicatePipeline matches(
			Function<User, Boolean> predicate) {
		if (decision != Decision.NO_DECISION || user == null)
			return this;

		if (Boolean.TRUE.equals(predicate.apply(user))) {
			decision = Decision.ACCEPT;
		}

		return this;
	}

	public SecurityPredicatePipeline notMatches(
			Function<User, Boolean> predicate) {
		if (decision != Decision.NO_DECISION || user == null)
			return this;

		if (Boolean.TRUE.equals(predicate.apply(user))) {
			decision = Decision.DENY;
		}

		return this;
	}

	public SecurityPredicatePipeline hasPermission(
			GeneralPermissionType generalPermissionType) {
		if (decision != Decision.NO_DECISION || user == null)
			return this;

		if (dslContext.select(
				DSL.field(Tables.PRINCIPAL.PERMISSIONS
						.contains(new GeneralPermissionType[] {
																generalPermissionType
		})))
				.from(Tables.PRINCIPAL)
				.where(Tables.PRINCIPAL.ID.eq(user.getId()))
				.fetchOne().value1()) {
			decision = Decision.ACCEPT;
		}

		return this;
	}

	public static SecurityPredicatePipeline defaultPipeline(
			DSLContext dslContext) {
		return new SecurityPredicatePipeline(dslContext).defaultSecurity();
	}

	public static SecurityPredicatePipeline emptyPipeline(
			DSLContext dslContext) {
		return new SecurityPredicatePipeline(dslContext);
	}

	private static enum Decision {
		NO_DECISION, ACCEPT, DENY
	}

	public boolean isAllowed() {
		return decision == Decision.ACCEPT;
	}
}
