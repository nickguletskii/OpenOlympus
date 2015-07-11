/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.ContestTasks;
import org.ng200.openolympus.jooq.tables.records.ContestTasksRecord;


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
public class ContestTasksDao extends DAOImpl<ContestTasksRecord, org.ng200.openolympus.jooq.tables.pojos.ContestTasks, Record2<Integer, Integer>> {

	/**
	 * Create a new ContestTasksDao without any configuration
	 */
	public ContestTasksDao() {
		super(ContestTasks.CONTEST_TASKS, org.ng200.openolympus.jooq.tables.pojos.ContestTasks.class);
	}

	/**
	 * Create a new ContestTasksDao with an attached configuration
	 */
	public ContestTasksDao(Configuration configuration) {
		super(ContestTasks.CONTEST_TASKS, org.ng200.openolympus.jooq.tables.pojos.ContestTasks.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record2<Integer, Integer> getId(org.ng200.openolympus.jooq.tables.pojos.ContestTasks object) {
		return compositeKeyRecord(object.getContestId(), object.getTaskId());
	}

	/**
	 * Fetch records that have <code>contest_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestTasks> fetchByContestId(Integer... values) {
		return fetch(ContestTasks.CONTEST_TASKS.CONTEST_ID, values);
	}

	/**
	 * Fetch records that have <code>task_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestTasks> fetchByTaskId(Integer... values) {
		return fetch(ContestTasks.CONTEST_TASKS.TASK_ID, values);
	}
}
