/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


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
@Entity
@Table(name = "contest_tasks", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"id_contest", "id_task"})
})
public interface IContestTasks extends Serializable {

	/**
	 * Setter for <code>public.contest_tasks.id_contest</code>.
	 */
	public IContestTasks setIdContest(Integer value);

	/**
	 * Getter for <code>public.contest_tasks.id_contest</code>.
	 */
	@Column(name = "id_contest", nullable = false, precision = 32)
	public Integer getIdContest();

	/**
	 * Setter for <code>public.contest_tasks.id_task</code>.
	 */
	public IContestTasks setIdTask(Integer value);

	/**
	 * Getter for <code>public.contest_tasks.id_task</code>.
	 */
	@Column(name = "id_task", nullable = false, precision = 32)
	public Integer getIdTask();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IContestTasks
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IContestTasks from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IContestTasks
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IContestTasks> E into(E into);
}
