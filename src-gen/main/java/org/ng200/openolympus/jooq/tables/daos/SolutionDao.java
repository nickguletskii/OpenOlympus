/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.daos;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.Solution;
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SolutionDao extends DAOImpl<SolutionRecord, org.ng200.openolympus.jooq.tables.pojos.Solution, Long> {

	/**
	 * Create a new SolutionDao without any configuration
	 */
	public SolutionDao() {
		super(Solution.SOLUTION, org.ng200.openolympus.jooq.tables.pojos.Solution.class);
	}

	/**
	 * Create a new SolutionDao with an attached configuration
	 */
	public SolutionDao(Configuration configuration) {
		super(Solution.SOLUTION, org.ng200.openolympus.jooq.tables.pojos.Solution.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(org.ng200.openolympus.jooq.tables.pojos.Solution object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchById(Long... values) {
		return fetch(Solution.SOLUTION.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Solution fetchOneById(Long value) {
		return fetchOne(Solution.SOLUTION.ID, value);
	}

	/**
	 * Fetch records that have <code>file IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByFile(String... values) {
		return fetch(Solution.SOLUTION.FILE, values);
	}

	/**
	 * Fetch records that have <code>maximum_score IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByMaximumScore(BigDecimal... values) {
		return fetch(Solution.SOLUTION.MAXIMUM_SCORE, values);
	}

	/**
	 * Fetch records that have <code>score IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByScore(BigDecimal... values) {
		return fetch(Solution.SOLUTION.SCORE, values);
	}

	/**
	 * Fetch records that have <code>tested IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByTested(Boolean... values) {
		return fetch(Solution.SOLUTION.TESTED, values);
	}

	/**
	 * Fetch records that have <code>time_added IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByTimeAdded(OffsetDateTime... values) {
		return fetch(Solution.SOLUTION.TIME_ADDED, values);
	}

	/**
	 * Fetch records that have <code>user_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByUserId(Long... values) {
		return fetch(Solution.SOLUTION.USER_ID, values);
	}

	/**
	 * Fetch records that have <code>task_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Solution> fetchByTaskId(Integer... values) {
		return fetch(Solution.SOLUTION.TASK_ID, values);
	}
	public org.ng200.openolympus.jooq.tables.pojos.Solution fetchOneById(String id) {
		return fetchOneById(java.lang.Long.valueOf(id));
	}
}
