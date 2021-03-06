/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
		"jOOQ version:3.7.3"
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
