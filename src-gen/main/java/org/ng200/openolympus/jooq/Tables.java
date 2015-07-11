/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;


import javax.annotation.Generated;

import org.ng200.openolympus.jooq.tables.Contest;
import org.ng200.openolympus.jooq.tables.ContestMessage;
import org.ng200.openolympus.jooq.tables.ContestParticipation;
import org.ng200.openolympus.jooq.tables.ContestPermission;
import org.ng200.openolympus.jooq.tables.ContestPermissionPrincipal;
import org.ng200.openolympus.jooq.tables.ContestQuestion;
import org.ng200.openolympus.jooq.tables.ContestTasks;
import org.ng200.openolympus.jooq.tables.Group;
import org.ng200.openolympus.jooq.tables.PersistentLogins;
import org.ng200.openolympus.jooq.tables.Principal;
import org.ng200.openolympus.jooq.tables.Property;
import org.ng200.openolympus.jooq.tables.Resource;
import org.ng200.openolympus.jooq.tables.Solution;
import org.ng200.openolympus.jooq.tables.Task;
import org.ng200.openolympus.jooq.tables.TaskPermission;
import org.ng200.openolympus.jooq.tables.TaskPermissionPrincipal;
import org.ng200.openolympus.jooq.tables.TimeExtension;
import org.ng200.openolympus.jooq.tables.User;
import org.ng200.openolympus.jooq.tables.UserGroups;
import org.ng200.openolympus.jooq.tables.Verdict;


/**
 * Convenience access to all tables in public
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

	/**
	 * The table public.contest
	 */
	public static final Contest CONTEST = org.ng200.openolympus.jooq.tables.Contest.CONTEST;

	/**
	 * The table public.contest_message
	 */
	public static final ContestMessage CONTEST_MESSAGE = org.ng200.openolympus.jooq.tables.ContestMessage.CONTEST_MESSAGE;

	/**
	 * The table public.contest_participation
	 */
	public static final ContestParticipation CONTEST_PARTICIPATION = org.ng200.openolympus.jooq.tables.ContestParticipation.CONTEST_PARTICIPATION;

	/**
	 * The table public.contest_permission
	 */
	public static final ContestPermission CONTEST_PERMISSION = org.ng200.openolympus.jooq.tables.ContestPermission.CONTEST_PERMISSION;

	/**
	 * The table public.contest_permission_principal
	 */
	public static final ContestPermissionPrincipal CONTEST_PERMISSION_PRINCIPAL = org.ng200.openolympus.jooq.tables.ContestPermissionPrincipal.CONTEST_PERMISSION_PRINCIPAL;

	/**
	 * The table public.contest_question
	 */
	public static final ContestQuestion CONTEST_QUESTION = org.ng200.openolympus.jooq.tables.ContestQuestion.CONTEST_QUESTION;

	/**
	 * The table public.contest_tasks
	 */
	public static final ContestTasks CONTEST_TASKS = org.ng200.openolympus.jooq.tables.ContestTasks.CONTEST_TASKS;

	/**
	 * The table public.group
	 */
	public static final Group GROUP = org.ng200.openolympus.jooq.tables.Group.GROUP;

	/**
	 * The table public.persistent_logins
	 */
	public static final PersistentLogins PERSISTENT_LOGINS = org.ng200.openolympus.jooq.tables.PersistentLogins.PERSISTENT_LOGINS;

	/**
	 * The table public.principal
	 */
	public static final Principal PRINCIPAL = org.ng200.openolympus.jooq.tables.Principal.PRINCIPAL;

	/**
	 * The table public.property
	 */
	public static final Property PROPERTY = org.ng200.openolympus.jooq.tables.Property.PROPERTY;

	/**
	 * The table public.resource
	 */
	public static final Resource RESOURCE = org.ng200.openolympus.jooq.tables.Resource.RESOURCE;

	/**
	 * The table public.solution
	 */
	public static final Solution SOLUTION = org.ng200.openolympus.jooq.tables.Solution.SOLUTION;

	/**
	 * The table public.task
	 */
	public static final Task TASK = org.ng200.openolympus.jooq.tables.Task.TASK;

	/**
	 * The table public.task_permission
	 */
	public static final TaskPermission TASK_PERMISSION = org.ng200.openolympus.jooq.tables.TaskPermission.TASK_PERMISSION;

	/**
	 * The table public.task_permission_principal
	 */
	public static final TaskPermissionPrincipal TASK_PERMISSION_PRINCIPAL = org.ng200.openolympus.jooq.tables.TaskPermissionPrincipal.TASK_PERMISSION_PRINCIPAL;

	/**
	 * The table public.time_extension
	 */
	public static final TimeExtension TIME_EXTENSION = org.ng200.openolympus.jooq.tables.TimeExtension.TIME_EXTENSION;

	/**
	 * The table public.USER
	 */
	public static final User USER = org.ng200.openolympus.jooq.tables.User.USER;

	/**
	 * The table public.user_groups
	 */
	public static final UserGroups USER_GROUPS = org.ng200.openolympus.jooq.tables.UserGroups.USER_GROUPS;

	/**
	 * The table public.verdict
	 */
	public static final Verdict VERDICT = org.ng200.openolympus.jooq.tables.Verdict.VERDICT;
}
