/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.Group;
import org.ng200.openolympus.jooq.tables.records.GroupRecord;


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
public class GroupDao extends DAOImpl<GroupRecord, org.ng200.openolympus.jooq.tables.pojos.Group, Long> {

	/**
	 * Create a new GroupDao without any configuration
	 */
	public GroupDao() {
		super(Group.GROUP, org.ng200.openolympus.jooq.tables.pojos.Group.class);
	}

	/**
	 * Create a new GroupDao with an attached configuration
	 */
	public GroupDao(Configuration configuration) {
		super(Group.GROUP, org.ng200.openolympus.jooq.tables.pojos.Group.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(org.ng200.openolympus.jooq.tables.pojos.Group object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Group> fetchById(Long... values) {
		return fetch(Group.GROUP.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Group fetchOneById(Long value) {
		return fetchOne(Group.GROUP.ID, value);
	}

	/**
	 * Fetch records that have <code>name IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Group> fetchByName(String... values) {
		return fetch(Group.GROUP.NAME, values);
	}

	/**
	 * Fetch a unique record that has <code>name = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Group fetchOneByName(String value) {
		return fetchOne(Group.GROUP.NAME, value);
	}
	public org.ng200.openolympus.jooq.tables.pojos.Group fetchOneById(String id) {
		return fetchOneById(java.lang.Long.valueOf(id));
	}
}
