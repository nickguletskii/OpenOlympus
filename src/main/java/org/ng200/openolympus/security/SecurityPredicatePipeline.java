/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ng200.openolympus.security;

import java.util.Optional;
import java.util.function.Function;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityPredicatePipeline {

	private User user;

	private Decision decision = Decision.NO_DECISION;

	private DSLContext dslContext;

	private SecurityPredicatePipeline(DSLContext dslContext) {
		this.dslContext = dslContext;
		user = (User) Optional.ofNullable(SecurityContextHolder.getContext())
				.map(c -> c.getAuthentication()).map(a -> a.getPrincipal())
				.filter(p -> p instanceof User)
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
