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


import java.time.OffsetDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.Task;
import org.ng200.openolympus.jooq.tables.records.TaskRecord;


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
public class TaskDao extends DAOImpl<TaskRecord, org.ng200.openolympus.jooq.tables.pojos.Task, Integer> {

	/**
	 * Create a new TaskDao without any configuration
	 */
	public TaskDao() {
		super(Task.TASK, org.ng200.openolympus.jooq.tables.pojos.Task.class);
	}

	/**
	 * Create a new TaskDao with an attached configuration
	 */
	public TaskDao(Configuration configuration) {
		super(Task.TASK, org.ng200.openolympus.jooq.tables.pojos.Task.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getId(org.ng200.openolympus.jooq.tables.pojos.Task object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Task> fetchById(Integer... values) {
		return fetch(Task.TASK.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Task fetchOneById(Integer value) {
		return fetchOne(Task.TASK.ID, value);
	}

	/**
	 * Fetch records that have <code>description_file IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Task> fetchByDescriptionFile(String... values) {
		return fetch(Task.TASK.DESCRIPTION_FILE, values);
	}

	/**
	 * Fetch records that have <code>name IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Task> fetchByName(String... values) {
		return fetch(Task.TASK.NAME, values);
	}

	/**
	 * Fetch a unique record that has <code>name = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Task fetchOneByName(String value) {
		return fetchOne(Task.TASK.NAME, value);
	}

	/**
	 * Fetch records that have <code>task_location IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Task> fetchByTaskLocation(String... values) {
		return fetch(Task.TASK.TASK_LOCATION, values);
	}

	/**
	 * Fetch records that have <code>created_date IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Task> fetchByCreatedDate(OffsetDateTime... values) {
		return fetch(Task.TASK.CREATED_DATE, values);
	}
	public org.ng200.openolympus.jooq.tables.pojos.Task fetchOneById(String id) {
		return fetchOneById(java.lang.Integer.valueOf(id));
	}
}
