/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;
import org.ng200.openolympus.jooq.Sequences;
import org.ng200.openolympus.jooq.tables.Contest;
import org.ng200.openolympus.jooq.tables.ContestMessage;
import org.ng200.openolympus.jooq.tables.ContestParticipation;
import org.ng200.openolympus.jooq.tables.ContestPermission;
import org.ng200.openolympus.jooq.tables.ContestPermissionPrincipal;
import org.ng200.openolympus.jooq.tables.ContestQuestion;
import org.ng200.openolympus.jooq.tables.ContestTasks;
import org.ng200.openolympus.jooq.tables.Group;
import org.ng200.openolympus.jooq.tables.GroupUsers;
import org.ng200.openolympus.jooq.tables.PersistentLogins;
import org.ng200.openolympus.jooq.tables.Principal;
import org.ng200.openolympus.jooq.tables.Property;
import org.ng200.openolympus.jooq.tables.Resource;
import org.ng200.openolympus.jooq.tables.Solution;
import org.ng200.openolympus.jooq.tables.Task;
import org.ng200.openolympus.jooq.tables.TaskPermission;
import org.ng200.openolympus.jooq.tables.TimeExtension;
import org.ng200.openolympus.jooq.tables.User;
import org.ng200.openolympus.jooq.tables.Verdict;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

	private static final long serialVersionUID = -823062637;

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
	public final List<Sequence<?>> getSequences() {
		List result = new ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final List<Sequence<?>> getSequences0() {
		return Arrays.<Sequence<?>>asList(
			Sequences.CONTEST_ID_SEQ,
			Sequences.CONTEST_MESSAGE_ID_SEQ,
			Sequences.CONTEST_PARTICIPATION_ID_SEQ,
			Sequences.CONTEST_PERMISSION_ID_SEQ,
			Sequences.CONTEST_QUESTION_ID_SEQ,
			Sequences.PRINCIPAL_SEQUENCE,
			Sequences.RESOURCE_ID_SEQ,
			Sequences.SOLUTION_ID_SEQ,
			Sequences.TASK_ID_SEQ,
			Sequences.TIME_EXTENSION_ID_SEQ,
			Sequences.VERDICT_ID_SEQ);
	}

	@Override
	public final List<Table<?>> getTables() {
		List result = new ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final List<Table<?>> getTables0() {
		return Arrays.<Table<?>>asList(
			Contest.CONTEST,
			ContestMessage.CONTEST_MESSAGE,
			ContestParticipation.CONTEST_PARTICIPATION,
			ContestPermission.CONTEST_PERMISSION,
			ContestPermissionPrincipal.CONTEST_PERMISSION_PRINCIPAL,
			ContestQuestion.CONTEST_QUESTION,
			ContestTasks.CONTEST_TASKS,
			Group.GROUP,
			GroupUsers.GROUP_USERS,
			PersistentLogins.PERSISTENT_LOGINS,
			Principal.PRINCIPAL,
			Property.PROPERTY,
			Resource.RESOURCE,
			Solution.SOLUTION,
			Task.TASK,
			TaskPermission.TASK_PERMISSION,
			TimeExtension.TIME_EXTENSION,
			User.USER,
			Verdict.VERDICT);
	}
}
