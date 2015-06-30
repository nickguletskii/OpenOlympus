/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.records;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;
import org.ng200.openolympus.jooq.tables.Resource;
import org.ng200.openolympus.jooq.tables.interfaces.IResource;


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
@Table(name = "resource", schema = "public")
public class ResourceRecord extends TableRecordImpl<ResourceRecord> implements Record5<Long, String, String, Long, Integer>, IResource {

	private static final long serialVersionUID = -113424606;

	/**
	 * Setter for <code>public.resource.id</code>.
	 */
	@Override
	public ResourceRecord setId(Long value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.resource.id</code>.
	 */
	@Column(name = "id", nullable = false, precision = 64)
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>public.resource.name</code>.
	 */
	@Override
	public ResourceRecord setName(String value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.resource.name</code>.
	 */
	@Column(name = "name", nullable = false)
	@Override
	public String getName() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>public.resource.filename</code>.
	 */
	@Override
	public ResourceRecord setFilename(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.resource.filename</code>.
	 */
	@Column(name = "filename", nullable = false)
	@Override
	public String getFilename() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>public.resource.USER_id</code>.
	 */
	@Override
	public ResourceRecord setUserId(Long value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.resource.USER_id</code>.
	 */
	@Column(name = "USER_id", precision = 64)
	@Override
	public Long getUserId() {
		return (Long) getValue(3);
	}

	/**
	 * Setter for <code>public.resource.task_id</code>.
	 */
	@Override
	public ResourceRecord setTaskId(Integer value) {
		setValue(4, value);
		return this;
	}

	/**
	 * Getter for <code>public.resource.task_id</code>.
	 */
	@Column(name = "task_id", precision = 32)
	@Override
	public Integer getTaskId() {
		return (Integer) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Long, String, String, Long, Integer> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Long, String, String, Long, Integer> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return Resource.RESOURCE.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Resource.RESOURCE.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return Resource.RESOURCE.FILENAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field4() {
		return Resource.RESOURCE.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return Resource.RESOURCE.TASK_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getFilename();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value4() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value5() {
		return getTaskId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceRecord value2(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceRecord value3(String value) {
		setFilename(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceRecord value4(Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceRecord value5(Integer value) {
		setTaskId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceRecord values(Long value1, String value2, String value3, Long value4, Integer value5) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IResource from) {
		setId(from.getId());
		setName(from.getName());
		setFilename(from.getFilename());
		setUserId(from.getUserId());
		setTaskId(from.getTaskId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IResource> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ResourceRecord
	 */
	public ResourceRecord() {
		super(Resource.RESOURCE);
	}

	/**
	 * Create a detached, initialised ResourceRecord
	 */
	public ResourceRecord(Long id, String name, String filename, Long userId, Integer taskId) {
		super(Resource.RESOURCE);

		setValue(0, id);
		setValue(1, name);
		setValue(2, filename);
		setValue(3, userId);
		setValue(4, taskId);
	}
}
