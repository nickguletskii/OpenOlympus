/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "task", schema = "public")
public interface ITask extends Serializable {

	/**
	 * Setter for <code>public.task.id</code>.
	 */
	public ITask setId(Integer value);

	/**
	 * Getter for <code>public.task.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 32)
	public Integer getId();

	/**
	 * Setter for <code>public.task.description_file</code>.
	 */
	public ITask setDescriptionFile(String value);

	/**
	 * Getter for <code>public.task.description_file</code>.
	 */
	@Column(name = "description_file", nullable = false)
	public String getDescriptionFile();

	/**
	 * Setter for <code>public.task.name</code>.
	 */
	public ITask setName(String value);

	/**
	 * Getter for <code>public.task.name</code>.
	 */
	@Column(name = "name", unique = true, length = 255)
	public String getName();

	/**
	 * Setter for <code>public.task.task_location</code>.
	 */
	public ITask setTaskLocation(String value);

	/**
	 * Getter for <code>public.task.task_location</code>.
	 */
	@Column(name = "task_location", nullable = false)
	public String getTaskLocation();

	/**
	 * Setter for <code>public.task.created_date</code>.
	 */
	public ITask setCreatedDate(OffsetDateTime value);

	/**
	 * Getter for <code>public.task.created_date</code>.
	 */
	@Column(name = "created_date")
	public OffsetDateTime getCreatedDate();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITask
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.ITask from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITask
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.ITask> E into(E into);
}
