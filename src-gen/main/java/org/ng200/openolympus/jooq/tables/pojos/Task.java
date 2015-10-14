/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import java.time.OffsetDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.ITask;
import org.ng200.openolympus.model.ITaskSecurityDescription;
import org.ng200.openolympus.model.LockableResourceImplWithIntegerId;


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
public class Task extends LockableResourceImplWithIntegerId implements ITaskSecurityDescription, ITask {

	private static final long serialVersionUID = 1514961417;

	private Integer        id;
	private String         descriptionFile;
	private String         name;
	private String         taskLocation;
	private OffsetDateTime createdDate;

	public Task() {}

	public Task(Task value) {
		this.id = value.id;
		this.descriptionFile = value.descriptionFile;
		this.name = value.name;
		this.taskLocation = value.taskLocation;
		this.createdDate = value.createdDate;
	}

	public Task(
		Integer        id,
		String         descriptionFile,
		String         name,
		String         taskLocation,
		OffsetDateTime createdDate
	) {
		this.id = id;
		this.descriptionFile = descriptionFile;
		this.name = name;
		this.taskLocation = taskLocation;
		this.createdDate = createdDate;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 32)
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public Task setId(Integer id) {
		this.id = id;
		return this;
	}

	@Column(name = "description_file", nullable = false)
	@Override
	public String getDescriptionFile() {
		return this.descriptionFile;
	}

	@Override
	public Task setDescriptionFile(String descriptionFile) {
		this.descriptionFile = descriptionFile;
		return this;
	}

	@Column(name = "name", unique = true, length = 255)
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Task setName(String name) {
		this.name = name;
		return this;
	}

	@Column(name = "task_location", nullable = false)
	@Override
	public String getTaskLocation() {
		return this.taskLocation;
	}

	@Override
	public Task setTaskLocation(String taskLocation) {
		this.taskLocation = taskLocation;
		return this;
	}

	@Column(name = "created_date")
	@Override
	public OffsetDateTime getCreatedDate() {
		return this.createdDate;
	}

	@Override
	public Task setCreatedDate(OffsetDateTime createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Task (");

		sb.append(id);
		sb.append(", ").append(descriptionFile);
		sb.append(", ").append(name);
		sb.append(", ").append(taskLocation);
		sb.append(", ").append(createdDate);

		sb.append(")");
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ITask from) {
		setId(from.getId());
		setDescriptionFile(from.getDescriptionFile());
		setName(from.getName());
		setTaskLocation(from.getTaskLocation());
		setCreatedDate(from.getCreatedDate());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITask> E into(E into) {
		into.from(this);
		return into;
	}
}
