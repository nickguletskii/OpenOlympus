package org.ng200.openolympus.services;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.Principal;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
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
			ContestPermissionType... values) {
		return Stream.of(values).anyMatch(value -> dslContext
				.select(Routines.hasContestPermission(contest.getId(),
						user.getId(), value))
				.fetchOne().value1());
	}

	public boolean hasTaskPermission(Task task, User user,
			TaskPermissionType... values) {
		return Stream.of(values).anyMatch(value -> dslContext
				.select(Routines.hasTaskPermission(task.getId(),
						user.getId(), value))
				.fetchOne().value1());
	}

	public boolean hasGroupPermission(Group group, User user,
			GroupPermissionType... values) {
		return Stream.of(values).anyMatch(value -> dslContext
				.select(Routines.hasGroupPermission(group.getId(),
						user.getId(), value))
				.fetchOne().value1());
	}

	public Principal setPrincipalGeneralPermissions(Principal principal,
			Map<GeneralPermissionType, Boolean> generalPermissions) {
		principal
				.setPermissions(generalPermissions
						.entrySet().stream()
						.filter(entry -> Boolean.TRUE
								.equals(entry.getValue()))
						.map(entry -> (GeneralPermissionType) entry
								.getKey())
						.toArray(size -> new GeneralPermissionType[size]));

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

	public boolean solutionIsModeratedByUser(Solution solution, User user) {
		return dslContext.select(
				DSL.field(
						DSL.exists(
								dslContext.select().from(Tables.CONTEST_TASKS)
										.join(Tables.CONTEST_PERMISSION)
										.on(Tables.CONTEST_TASKS.CONTEST_ID
												.eq(Tables.CONTEST_PERMISSION.CONTEST_ID)
												.and(Tables.CONTEST_PERMISSION.PRINCIPAL_ID
														.eq(user.getId())))
										.where(Tables.CONTEST_TASKS.TASK_ID
												.eq(solution.getTaskId())
												.and(Tables.CONTEST_PERMISSION.PERMISSION
														.eq(
																ContestPermissionType.view_all_solutions)
														.and(Tables.CONTEST_PERMISSION.PRINCIPAL_ID
																.eq(user.getId())))))))
				.fetchOne().value1();
	}
}
