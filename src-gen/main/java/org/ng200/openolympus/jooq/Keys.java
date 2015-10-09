/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;


import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;
import org.ng200.openolympus.jooq.tables.Contest;
import org.ng200.openolympus.jooq.tables.ContestMessage;
import org.ng200.openolympus.jooq.tables.ContestParticipation;
import org.ng200.openolympus.jooq.tables.ContestPermission;
import org.ng200.openolympus.jooq.tables.ContestQuestion;
import org.ng200.openolympus.jooq.tables.ContestTasks;
import org.ng200.openolympus.jooq.tables.GetContestsThatIntersect;
import org.ng200.openolympus.jooq.tables.Group;
import org.ng200.openolympus.jooq.tables.GroupPermission;
import org.ng200.openolympus.jooq.tables.GroupUsers;
import org.ng200.openolympus.jooq.tables.PersistentLogins;
import org.ng200.openolympus.jooq.tables.Principal;
import org.ng200.openolympus.jooq.tables.Property;
import org.ng200.openolympus.jooq.tables.Solution;
import org.ng200.openolympus.jooq.tables.Task;
import org.ng200.openolympus.jooq.tables.TaskPermission;
import org.ng200.openolympus.jooq.tables.TimeExtension;
import org.ng200.openolympus.jooq.tables.User;
import org.ng200.openolympus.jooq.tables.Verdict;
import org.ng200.openolympus.jooq.tables.records.ContestMessageRecord;
import org.ng200.openolympus.jooq.tables.records.ContestParticipationRecord;
import org.ng200.openolympus.jooq.tables.records.ContestPermissionRecord;
import org.ng200.openolympus.jooq.tables.records.ContestQuestionRecord;
import org.ng200.openolympus.jooq.tables.records.ContestRecord;
import org.ng200.openolympus.jooq.tables.records.ContestTasksRecord;
import org.ng200.openolympus.jooq.tables.records.GetContestsThatIntersectRecord;
import org.ng200.openolympus.jooq.tables.records.GroupPermissionRecord;
import org.ng200.openolympus.jooq.tables.records.GroupRecord;
import org.ng200.openolympus.jooq.tables.records.GroupUsersRecord;
import org.ng200.openolympus.jooq.tables.records.PersistentLoginsRecord;
import org.ng200.openolympus.jooq.tables.records.PrincipalRecord;
import org.ng200.openolympus.jooq.tables.records.PropertyRecord;
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;
import org.ng200.openolympus.jooq.tables.records.TaskPermissionRecord;
import org.ng200.openolympus.jooq.tables.records.TaskRecord;
import org.ng200.openolympus.jooq.tables.records.TimeExtensionRecord;
import org.ng200.openolympus.jooq.tables.records.UserRecord;
import org.ng200.openolympus.jooq.tables.records.VerdictRecord;


/**
 * A class modelling foreign key relationships between tables of the <code>public</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<ContestRecord, Integer> IDENTITY_CONTEST = Identities0.IDENTITY_CONTEST;
	public static final Identity<ContestMessageRecord, Integer> IDENTITY_CONTEST_MESSAGE = Identities0.IDENTITY_CONTEST_MESSAGE;
	public static final Identity<ContestParticipationRecord, Long> IDENTITY_CONTEST_PARTICIPATION = Identities0.IDENTITY_CONTEST_PARTICIPATION;
	public static final Identity<ContestQuestionRecord, Integer> IDENTITY_CONTEST_QUESTION = Identities0.IDENTITY_CONTEST_QUESTION;
	public static final Identity<GetContestsThatIntersectRecord, Integer> IDENTITY_GET_CONTESTS_THAT_INTERSECT = Identities0.IDENTITY_GET_CONTESTS_THAT_INTERSECT;
	public static final Identity<GroupRecord, Long> IDENTITY_GROUP = Identities0.IDENTITY_GROUP;
	public static final Identity<PrincipalRecord, Long> IDENTITY_PRINCIPAL = Identities0.IDENTITY_PRINCIPAL;
	public static final Identity<SolutionRecord, Long> IDENTITY_SOLUTION = Identities0.IDENTITY_SOLUTION;
	public static final Identity<TaskRecord, Integer> IDENTITY_TASK = Identities0.IDENTITY_TASK;
	public static final Identity<TimeExtensionRecord, Long> IDENTITY_TIME_EXTENSION = Identities0.IDENTITY_TIME_EXTENSION;
	public static final Identity<UserRecord, Long> IDENTITY_USER = Identities0.IDENTITY_USER;
	public static final Identity<VerdictRecord, Long> IDENTITY_VERDICT = Identities0.IDENTITY_VERDICT;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<ContestRecord> CONTEST_PKEY = UniqueKeys0.CONTEST_PKEY;
	public static final UniqueKey<ContestRecord> CONTEST_NAME_UNIQUE = UniqueKeys0.CONTEST_NAME_UNIQUE;
	public static final UniqueKey<ContestMessageRecord> CONTEST_MESSAGES_PKEY = UniqueKeys0.CONTEST_MESSAGES_PKEY;
	public static final UniqueKey<ContestParticipationRecord> CONTEST_PARTICIPATION_PKEY = UniqueKeys0.CONTEST_PARTICIPATION_PKEY;
	public static final UniqueKey<ContestPermissionRecord> CONTEST_PERMISSION_PK = UniqueKeys0.CONTEST_PERMISSION_PK;
	public static final UniqueKey<ContestQuestionRecord> CONTEST_QUESTION_PKEY = UniqueKeys0.CONTEST_QUESTION_PKEY;
	public static final UniqueKey<ContestTasksRecord> CONTEST_TASKS_PK = UniqueKeys0.CONTEST_TASKS_PK;
	public static final UniqueKey<GroupRecord> GROUP_PK = UniqueKeys0.GROUP_PK;
	public static final UniqueKey<GroupRecord> GROUP_NAME_UNIQUE = UniqueKeys0.GROUP_NAME_UNIQUE;
	public static final UniqueKey<GroupPermissionRecord> GROUP_PERMISSION_PK = UniqueKeys0.GROUP_PERMISSION_PK;
	public static final UniqueKey<GroupUsersRecord> GROUP_USERS_PK = UniqueKeys0.GROUP_USERS_PK;
	public static final UniqueKey<PersistentLoginsRecord> PERSISTENT_LOGINS_PKEY = UniqueKeys0.PERSISTENT_LOGINS_PKEY;
	public static final UniqueKey<PrincipalRecord> PRINCIPAL_PK = UniqueKeys0.PRINCIPAL_PK;
	public static final UniqueKey<PropertyRecord> PROPERTY_PKEY = UniqueKeys0.PROPERTY_PKEY;
	public static final UniqueKey<PropertyRecord> UK_8JYTV8TU3PUI7RAM00B44TN4U = UniqueKeys0.UK_8JYTV8TU3PUI7RAM00B44TN4U;
	public static final UniqueKey<PropertyRecord> UK_4B6VATGJ30955XSJR51YEGXI9 = UniqueKeys0.UK_4B6VATGJ30955XSJR51YEGXI9;
	public static final UniqueKey<SolutionRecord> SOLUTION_PKEY = UniqueKeys0.SOLUTION_PKEY;
	public static final UniqueKey<TaskRecord> TASK_PKEY = UniqueKeys0.TASK_PKEY;
	public static final UniqueKey<TaskRecord> TASK_NAME_UNIQUE = UniqueKeys0.TASK_NAME_UNIQUE;
	public static final UniqueKey<TaskPermissionRecord> TASK_PERMISSION_PK = UniqueKeys0.TASK_PERMISSION_PK;
	public static final UniqueKey<TimeExtensionRecord> TIME_EXTENSION_PKEY = UniqueKeys0.TIME_EXTENSION_PKEY;
	public static final UniqueKey<UserRecord> USER_PK = UniqueKeys0.USER_PK;
	public static final UniqueKey<UserRecord> UK_R43AF9AP4EDM43MMTQ01ODDJ6 = UniqueKeys0.UK_R43AF9AP4EDM43MMTQ01ODDJ6;
	public static final UniqueKey<VerdictRecord> VERDICT_PK = UniqueKeys0.VERDICT_PK;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<ContestMessageRecord, UserRecord> CONTEST_MESSAGE__USER_FK = ForeignKeys0.CONTEST_MESSAGE__USER_FK;
	public static final ForeignKey<ContestMessageRecord, ContestRecord> CONTEST_MESSAGE__CONTEST_FK = ForeignKeys0.CONTEST_MESSAGE__CONTEST_FK;
	public static final ForeignKey<ContestParticipationRecord, UserRecord> CONTEST_PARTICIPATION__USER_FK = ForeignKeys0.CONTEST_PARTICIPATION__USER_FK;
	public static final ForeignKey<ContestParticipationRecord, ContestRecord> CONTEST_PARTICIPATION__CONTEST_FK = ForeignKeys0.CONTEST_PARTICIPATION__CONTEST_FK;
	public static final ForeignKey<ContestPermissionRecord, ContestRecord> CONTEST_PERMISSION__CONTEST_PERMISSION_CONTEST_ID_FK = ForeignKeys0.CONTEST_PERMISSION__CONTEST_PERMISSION_CONTEST_ID_FK;
	public static final ForeignKey<ContestPermissionRecord, PrincipalRecord> CONTEST_PERMISSION__CONTEST_PERMISSION_PRINCIPAL_ID_FK = ForeignKeys0.CONTEST_PERMISSION__CONTEST_PERMISSION_PRINCIPAL_ID_FK;
	public static final ForeignKey<ContestQuestionRecord, UserRecord> CONTEST_QUESTION__USER_FK = ForeignKeys0.CONTEST_QUESTION__USER_FK;
	public static final ForeignKey<ContestQuestionRecord, ContestRecord> CONTEST_QUESTION__CONTEST_FK = ForeignKeys0.CONTEST_QUESTION__CONTEST_FK;
	public static final ForeignKey<ContestTasksRecord, ContestRecord> CONTEST_TASKS__CONTEST_FK = ForeignKeys0.CONTEST_TASKS__CONTEST_FK;
	public static final ForeignKey<ContestTasksRecord, TaskRecord> CONTEST_TASKS__TASK_FK = ForeignKeys0.CONTEST_TASKS__TASK_FK;
	public static final ForeignKey<GroupRecord, PrincipalRecord> GROUP__GROUP_PRINCIPAL_ID_MAPPING = ForeignKeys0.GROUP__GROUP_PRINCIPAL_ID_MAPPING;
	public static final ForeignKey<GroupPermissionRecord, PrincipalRecord> GROUP_PERMISSION__GROUP_PERMISSION_PRINCIPAL_FK = ForeignKeys0.GROUP_PERMISSION__GROUP_PERMISSION_PRINCIPAL_FK;
	public static final ForeignKey<GroupPermissionRecord, GroupRecord> GROUP_PERMISSION__GROUP_PERMISSION_GROUP_FK = ForeignKeys0.GROUP_PERMISSION__GROUP_PERMISSION_GROUP_FK;
	public static final ForeignKey<GroupUsersRecord, GroupRecord> GROUP_USERS__GROUP_FK = ForeignKeys0.GROUP_USERS__GROUP_FK;
	public static final ForeignKey<GroupUsersRecord, UserRecord> GROUP_USERS__USER_FK = ForeignKeys0.GROUP_USERS__USER_FK;
	public static final ForeignKey<SolutionRecord, UserRecord> SOLUTION__USER_FK = ForeignKeys0.SOLUTION__USER_FK;
	public static final ForeignKey<SolutionRecord, TaskRecord> SOLUTION__TASK_FK = ForeignKeys0.SOLUTION__TASK_FK;
	public static final ForeignKey<TaskPermissionRecord, TaskRecord> TASK_PERMISSION__TASK_FK = ForeignKeys0.TASK_PERMISSION__TASK_FK;
	public static final ForeignKey<TaskPermissionRecord, PrincipalRecord> TASK_PERMISSION__PRINCIPAL_FK = ForeignKeys0.TASK_PERMISSION__PRINCIPAL_FK;
	public static final ForeignKey<TimeExtensionRecord, UserRecord> TIME_EXTENSION__USER_FK = ForeignKeys0.TIME_EXTENSION__USER_FK;
	public static final ForeignKey<TimeExtensionRecord, ContestRecord> TIME_EXTENSION__CONTEST_FK = ForeignKeys0.TIME_EXTENSION__CONTEST_FK;
	public static final ForeignKey<UserRecord, PrincipalRecord> USER__USER_PRINCIPAL_ID_MAPPING = ForeignKeys0.USER__USER_PRINCIPAL_ID_MAPPING;
	public static final ForeignKey<VerdictRecord, SolutionRecord> VERDICT__SOLUTION_FK = ForeignKeys0.VERDICT__SOLUTION_FK;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<ContestRecord, Integer> IDENTITY_CONTEST = createIdentity(Contest.CONTEST, Contest.CONTEST.ID);
		public static Identity<ContestMessageRecord, Integer> IDENTITY_CONTEST_MESSAGE = createIdentity(ContestMessage.CONTEST_MESSAGE, ContestMessage.CONTEST_MESSAGE.ID);
		public static Identity<ContestParticipationRecord, Long> IDENTITY_CONTEST_PARTICIPATION = createIdentity(ContestParticipation.CONTEST_PARTICIPATION, ContestParticipation.CONTEST_PARTICIPATION.ID);
		public static Identity<ContestQuestionRecord, Integer> IDENTITY_CONTEST_QUESTION = createIdentity(ContestQuestion.CONTEST_QUESTION, ContestQuestion.CONTEST_QUESTION.ID);
		public static Identity<GetContestsThatIntersectRecord, Integer> IDENTITY_GET_CONTESTS_THAT_INTERSECT = createIdentity(GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT, GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT.ID);
		public static Identity<GroupRecord, Long> IDENTITY_GROUP = createIdentity(Group.GROUP, Group.GROUP.ID);
		public static Identity<PrincipalRecord, Long> IDENTITY_PRINCIPAL = createIdentity(Principal.PRINCIPAL, Principal.PRINCIPAL.ID);
		public static Identity<SolutionRecord, Long> IDENTITY_SOLUTION = createIdentity(Solution.SOLUTION, Solution.SOLUTION.ID);
		public static Identity<TaskRecord, Integer> IDENTITY_TASK = createIdentity(Task.TASK, Task.TASK.ID);
		public static Identity<TimeExtensionRecord, Long> IDENTITY_TIME_EXTENSION = createIdentity(TimeExtension.TIME_EXTENSION, TimeExtension.TIME_EXTENSION.ID);
		public static Identity<UserRecord, Long> IDENTITY_USER = createIdentity(User.USER, User.USER.ID);
		public static Identity<VerdictRecord, Long> IDENTITY_VERDICT = createIdentity(Verdict.VERDICT, Verdict.VERDICT.ID);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<ContestRecord> CONTEST_PKEY = createUniqueKey(Contest.CONTEST, Contest.CONTEST.ID);
		public static final UniqueKey<ContestRecord> CONTEST_NAME_UNIQUE = createUniqueKey(Contest.CONTEST, Contest.CONTEST.NAME);
		public static final UniqueKey<ContestMessageRecord> CONTEST_MESSAGES_PKEY = createUniqueKey(ContestMessage.CONTEST_MESSAGE, ContestMessage.CONTEST_MESSAGE.ID);
		public static final UniqueKey<ContestParticipationRecord> CONTEST_PARTICIPATION_PKEY = createUniqueKey(ContestParticipation.CONTEST_PARTICIPATION, ContestParticipation.CONTEST_PARTICIPATION.ID);
		public static final UniqueKey<ContestPermissionRecord> CONTEST_PERMISSION_PK = createUniqueKey(ContestPermission.CONTEST_PERMISSION, ContestPermission.CONTEST_PERMISSION.PERMISSION, ContestPermission.CONTEST_PERMISSION.CONTEST_ID, ContestPermission.CONTEST_PERMISSION.PRINCIPAL_ID);
		public static final UniqueKey<ContestQuestionRecord> CONTEST_QUESTION_PKEY = createUniqueKey(ContestQuestion.CONTEST_QUESTION, ContestQuestion.CONTEST_QUESTION.ID);
		public static final UniqueKey<ContestTasksRecord> CONTEST_TASKS_PK = createUniqueKey(ContestTasks.CONTEST_TASKS, ContestTasks.CONTEST_TASKS.CONTEST_ID, ContestTasks.CONTEST_TASKS.TASK_ID);
		public static final UniqueKey<GroupRecord> GROUP_PK = createUniqueKey(Group.GROUP, Group.GROUP.ID);
		public static final UniqueKey<GroupRecord> GROUP_NAME_UNIQUE = createUniqueKey(Group.GROUP, Group.GROUP.NAME);
		public static final UniqueKey<GroupPermissionRecord> GROUP_PERMISSION_PK = createUniqueKey(GroupPermission.GROUP_PERMISSION, GroupPermission.GROUP_PERMISSION.PRINCIPAL_ID, GroupPermission.GROUP_PERMISSION.GROUP_ID, GroupPermission.GROUP_PERMISSION.PERMISSION);
		public static final UniqueKey<GroupUsersRecord> GROUP_USERS_PK = createUniqueKey(GroupUsers.GROUP_USERS, GroupUsers.GROUP_USERS.GROUP_ID, GroupUsers.GROUP_USERS.USER_ID);
		public static final UniqueKey<PersistentLoginsRecord> PERSISTENT_LOGINS_PKEY = createUniqueKey(PersistentLogins.PERSISTENT_LOGINS, PersistentLogins.PERSISTENT_LOGINS.SERIES);
		public static final UniqueKey<PrincipalRecord> PRINCIPAL_PK = createUniqueKey(Principal.PRINCIPAL, Principal.PRINCIPAL.ID);
		public static final UniqueKey<PropertyRecord> PROPERTY_PKEY = createUniqueKey(Property.PROPERTY, Property.PROPERTY.ID);
		public static final UniqueKey<PropertyRecord> UK_8JYTV8TU3PUI7RAM00B44TN4U = createUniqueKey(Property.PROPERTY, Property.PROPERTY.PROPERTY_KEY);
		public static final UniqueKey<PropertyRecord> UK_4B6VATGJ30955XSJR51YEGXI9 = createUniqueKey(Property.PROPERTY, Property.PROPERTY.PROPERTY_VALUE);
		public static final UniqueKey<SolutionRecord> SOLUTION_PKEY = createUniqueKey(Solution.SOLUTION, Solution.SOLUTION.ID);
		public static final UniqueKey<TaskRecord> TASK_PKEY = createUniqueKey(Task.TASK, Task.TASK.ID);
		public static final UniqueKey<TaskRecord> TASK_NAME_UNIQUE = createUniqueKey(Task.TASK, Task.TASK.NAME);
		public static final UniqueKey<TaskPermissionRecord> TASK_PERMISSION_PK = createUniqueKey(TaskPermission.TASK_PERMISSION, TaskPermission.TASK_PERMISSION.TASK_ID, TaskPermission.TASK_PERMISSION.PRINCIPAL_ID, TaskPermission.TASK_PERMISSION.PERMISSION);
		public static final UniqueKey<TimeExtensionRecord> TIME_EXTENSION_PKEY = createUniqueKey(TimeExtension.TIME_EXTENSION, TimeExtension.TIME_EXTENSION.ID);
		public static final UniqueKey<UserRecord> USER_PK = createUniqueKey(User.USER, User.USER.ID);
		public static final UniqueKey<UserRecord> UK_R43AF9AP4EDM43MMTQ01ODDJ6 = createUniqueKey(User.USER, User.USER.USERNAME);
		public static final UniqueKey<VerdictRecord> VERDICT_PK = createUniqueKey(Verdict.VERDICT, Verdict.VERDICT.ID);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<ContestMessageRecord, UserRecord> CONTEST_MESSAGE__USER_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.USER_PK, ContestMessage.CONTEST_MESSAGE, ContestMessage.CONTEST_MESSAGE.USER_ID);
		public static final ForeignKey<ContestMessageRecord, ContestRecord> CONTEST_MESSAGE__CONTEST_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.CONTEST_PKEY, ContestMessage.CONTEST_MESSAGE, ContestMessage.CONTEST_MESSAGE.CONTEST_ID);
		public static final ForeignKey<ContestParticipationRecord, UserRecord> CONTEST_PARTICIPATION__USER_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.USER_PK, ContestParticipation.CONTEST_PARTICIPATION, ContestParticipation.CONTEST_PARTICIPATION.USER_ID);
		public static final ForeignKey<ContestParticipationRecord, ContestRecord> CONTEST_PARTICIPATION__CONTEST_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.CONTEST_PKEY, ContestParticipation.CONTEST_PARTICIPATION, ContestParticipation.CONTEST_PARTICIPATION.CONTEST_ID);
		public static final ForeignKey<ContestPermissionRecord, ContestRecord> CONTEST_PERMISSION__CONTEST_PERMISSION_CONTEST_ID_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.CONTEST_PKEY, ContestPermission.CONTEST_PERMISSION, ContestPermission.CONTEST_PERMISSION.CONTEST_ID);
		public static final ForeignKey<ContestPermissionRecord, PrincipalRecord> CONTEST_PERMISSION__CONTEST_PERMISSION_PRINCIPAL_ID_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.PRINCIPAL_PK, ContestPermission.CONTEST_PERMISSION, ContestPermission.CONTEST_PERMISSION.PRINCIPAL_ID);
		public static final ForeignKey<ContestQuestionRecord, UserRecord> CONTEST_QUESTION__USER_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.USER_PK, ContestQuestion.CONTEST_QUESTION, ContestQuestion.CONTEST_QUESTION.USER_ID);
		public static final ForeignKey<ContestQuestionRecord, ContestRecord> CONTEST_QUESTION__CONTEST_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.CONTEST_PKEY, ContestQuestion.CONTEST_QUESTION, ContestQuestion.CONTEST_QUESTION.CONTEST_ID);
		public static final ForeignKey<ContestTasksRecord, ContestRecord> CONTEST_TASKS__CONTEST_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.CONTEST_PKEY, ContestTasks.CONTEST_TASKS, ContestTasks.CONTEST_TASKS.CONTEST_ID);
		public static final ForeignKey<ContestTasksRecord, TaskRecord> CONTEST_TASKS__TASK_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.TASK_PKEY, ContestTasks.CONTEST_TASKS, ContestTasks.CONTEST_TASKS.TASK_ID);
		public static final ForeignKey<GroupRecord, PrincipalRecord> GROUP__GROUP_PRINCIPAL_ID_MAPPING = createForeignKey(org.ng200.openolympus.jooq.Keys.PRINCIPAL_PK, Group.GROUP, Group.GROUP.ID);
		public static final ForeignKey<GroupPermissionRecord, PrincipalRecord> GROUP_PERMISSION__GROUP_PERMISSION_PRINCIPAL_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.PRINCIPAL_PK, GroupPermission.GROUP_PERMISSION, GroupPermission.GROUP_PERMISSION.PRINCIPAL_ID);
		public static final ForeignKey<GroupPermissionRecord, GroupRecord> GROUP_PERMISSION__GROUP_PERMISSION_GROUP_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.GROUP_PK, GroupPermission.GROUP_PERMISSION, GroupPermission.GROUP_PERMISSION.GROUP_ID);
		public static final ForeignKey<GroupUsersRecord, GroupRecord> GROUP_USERS__GROUP_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.GROUP_PK, GroupUsers.GROUP_USERS, GroupUsers.GROUP_USERS.GROUP_ID);
		public static final ForeignKey<GroupUsersRecord, UserRecord> GROUP_USERS__USER_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.USER_PK, GroupUsers.GROUP_USERS, GroupUsers.GROUP_USERS.USER_ID);
		public static final ForeignKey<SolutionRecord, UserRecord> SOLUTION__USER_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.USER_PK, Solution.SOLUTION, Solution.SOLUTION.USER_ID);
		public static final ForeignKey<SolutionRecord, TaskRecord> SOLUTION__TASK_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.TASK_PKEY, Solution.SOLUTION, Solution.SOLUTION.TASK_ID);
		public static final ForeignKey<TaskPermissionRecord, TaskRecord> TASK_PERMISSION__TASK_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.TASK_PKEY, TaskPermission.TASK_PERMISSION, TaskPermission.TASK_PERMISSION.TASK_ID);
		public static final ForeignKey<TaskPermissionRecord, PrincipalRecord> TASK_PERMISSION__PRINCIPAL_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.PRINCIPAL_PK, TaskPermission.TASK_PERMISSION, TaskPermission.TASK_PERMISSION.PRINCIPAL_ID);
		public static final ForeignKey<TimeExtensionRecord, UserRecord> TIME_EXTENSION__USER_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.USER_PK, TimeExtension.TIME_EXTENSION, TimeExtension.TIME_EXTENSION.USER_ID);
		public static final ForeignKey<TimeExtensionRecord, ContestRecord> TIME_EXTENSION__CONTEST_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.CONTEST_PKEY, TimeExtension.TIME_EXTENSION, TimeExtension.TIME_EXTENSION.CONTEST_ID);
		public static final ForeignKey<UserRecord, PrincipalRecord> USER__USER_PRINCIPAL_ID_MAPPING = createForeignKey(org.ng200.openolympus.jooq.Keys.PRINCIPAL_PK, User.USER, User.USER.ID);
		public static final ForeignKey<VerdictRecord, SolutionRecord> VERDICT__SOLUTION_FK = createForeignKey(org.ng200.openolympus.jooq.Keys.SOLUTION_PKEY, Verdict.VERDICT, Verdict.VERDICT.SOLUTION_ID);
	}
}
