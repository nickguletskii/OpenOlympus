/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends org.jooq.impl.SchemaImpl {

	private static final long serialVersionUID = 1185274575;

	/**
	 * The reference instance of <code>public</code>
	 */
	public static final Public PUBLIC = new Public();

	/**
	 * No further instances allowed
	 */
	private Public() {
		super("public");
	}

	@Override
	public final java.util.List<org.jooq.Sequence<?>> getSequences() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final java.util.List<org.jooq.Sequence<?>> getSequences0() {
		return java.util.Arrays.<org.jooq.Sequence<?>>asList(
			org.ng200.openolympus.jooq.Sequences.HIBERNATE_SEQUENCE,
			org.ng200.openolympus.jooq.Sequences.VERDICTS_ID_SEQ);
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final java.util.List<org.jooq.Table<?>> getTables0() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			org.ng200.openolympus.jooq.tables.ContestParticipations.CONTEST_PARTICIPATIONS,
			org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS,
			org.ng200.openolympus.jooq.tables.Contests.CONTESTS,
			org.ng200.openolympus.jooq.tables.ContestsTasks.CONTESTS_TASKS,
			org.ng200.openolympus.jooq.tables.Properties.PROPERTIES,
			org.ng200.openolympus.jooq.tables.RememberMeTokens.REMEMBER_ME_TOKENS,
			org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS,
			org.ng200.openolympus.jooq.tables.TaskMaintenanceInformation.TASK_MAINTENANCE_INFORMATION,
			org.ng200.openolympus.jooq.tables.Tasks.TASKS,
			org.ng200.openolympus.jooq.tables.TimeExtensions.TIME_EXTENSIONS,
			org.ng200.openolympus.jooq.tables.UserRoles.USER_ROLES,
			org.ng200.openolympus.jooq.tables.Users.USERS,
			org.ng200.openolympus.jooq.tables.UsersRoles.USERS_ROLES,
			org.ng200.openolympus.jooq.tables.Verdicts.VERDICTS);
	}
}
