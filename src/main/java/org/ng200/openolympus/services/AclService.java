package org.ng200.openolympus.services;

import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AclService {

	@Autowired
	private DSLContext dslContext;

	public OlympusPrincipal extractPrincipal(
	        Long id) {
		return Optional.<OlympusPrincipal> ofNullable(
		        this.dslContext.selectFrom(Tables.GROUP)
		                .where(Tables.GROUP.ID
		                        .eq(id))
		                .fetchOneInto(Group.class))
		        .orElse(
		                this.dslContext.selectFrom(Tables.USER)
		                        .where(Tables.USER.ID
		                                .eq(id))
		                        .fetchOneInto(User.class));
	}
}
