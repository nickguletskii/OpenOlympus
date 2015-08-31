package org.ng200.openolympus.services;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.Principal;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableSet;

@Service
public class AclService extends GenericCreateUpdateRepository {

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

	public boolean hasContestPermission(Contest contest, User user,
	        ContestPermissionType value) {
		return dslContext.select(Routines.hasContestPermission(contest.getId(),
		        user.getId(), value)).fetchOne().value1();
	}

	public boolean hasTaskPermission(Task task, User user,
	        TaskPermissionType value) {
		return dslContext.select(Routines.hasTaskPermission(task.getId(),
		        user.getId(), value)).fetchOne().value1();
	}

	public boolean hasGroupPermission(Group group, User user,
	        GroupPermissionType value) {
		return dslContext.select(Routines.hasGroupPermission(group.getId(),
		        user.getId(), value)).fetchOne().value1();
	}

	public Principal setPrincipalGeneralPermissions(Principal principal,
	        Map<GeneralPermissionType, Boolean> generalPermissions) {
		principal
		        .setPermissions((GeneralPermissionType[]) generalPermissions
		                .entrySet().stream()
		                .filter(entry -> Boolean.TRUE
		                        .equals(entry.getValue()))
		                .map(entry -> entry
		                        .getKey())
		                .toArray());

		return update(principal, Tables.PRINCIPAL);
	}

	public Map<GeneralPermissionType, Boolean> getPrincipalGeneralPermissions(
	        Principal principal) {
		Set<GeneralPermissionType> permissionTypes = ImmutableSet
		        .copyOf(principal.getPermissions());
		return Stream.of(GeneralPermissionType.values()).collect(
		        Collectors
		                .<GeneralPermissionType, GeneralPermissionType, Boolean> toMap(
		                        (type) -> type,
		                        (type) -> permissionTypes.contains(type)));
	}
}
