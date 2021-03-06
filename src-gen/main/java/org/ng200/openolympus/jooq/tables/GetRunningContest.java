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
package org.ng200.openolympus.jooq.tables;


import java.time.Duration;
import java.time.OffsetDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.GetRunningContestRecord;
import org.ng200.openolympus.util.DateTimeBinding;
import org.ng200.openolympus.util.DurationConverter;


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
public class GetRunningContest extends TableImpl<GetRunningContestRecord> {

	private static final long serialVersionUID = 1328324162;

	/**
	 * The reference instance of <code>public.get_running_contest</code>
	 */
	public static final GetRunningContest GET_RUNNING_CONTEST = new GetRunningContest();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<GetRunningContestRecord> getRecordType() {
		return GetRunningContestRecord.class;
	}

	/**
	 * The column <code>public.get_running_contest.id</code>.
	 */
	public final TableField<GetRunningContestRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.get_running_contest.duration</code>.
	 */
	public final TableField<GetRunningContestRecord, Duration> DURATION = createField("duration", org.jooq.impl.SQLDataType.BIGINT, this, "", new DurationConverter());

	/**
	 * The column <code>public.get_running_contest.name</code>.
	 */
	public final TableField<GetRunningContestRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.get_running_contest.show_full_tests_during_contest</code>.
	 */
	public final TableField<GetRunningContestRecord, Boolean> SHOW_FULL_TESTS_DURING_CONTEST = createField("show_full_tests_during_contest", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

	/**
	 * The column <code>public.get_running_contest.start_time</code>.
	 */
	public final TableField<GetRunningContestRecord, OffsetDateTime> START_TIME = createField("start_time", org.ng200.openolympus.jooqsupport.CustomTypes.OFFSETDATETIME, this, "", new DateTimeBinding());

	/**
	 * Create a <code>public.get_running_contest</code> table reference
	 */
	public GetRunningContest() {
		this("get_running_contest", null);
	}

	/**
	 * Create an aliased <code>public.get_running_contest</code> table reference
	 */
	public GetRunningContest(String alias) {
		this(alias, GET_RUNNING_CONTEST);
	}

	private GetRunningContest(String alias, Table<GetRunningContestRecord> aliased) {
		this(alias, aliased, new Field[0]);
	}

	private GetRunningContest(String alias, Table<GetRunningContestRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<GetRunningContestRecord, Integer> getIdentity() {
		return Keys.IDENTITY_GET_RUNNING_CONTEST;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetRunningContest as(String alias) {
		return new GetRunningContest(alias, this, parameters);
	}

	/**
	 * Rename this table
	 */
	public GetRunningContest rename(String name) {
		return new GetRunningContest(name, null, parameters);
	}

	/**
	 * Call this table-valued function
	 */
	public GetRunningContest call() {
		return new GetRunningContest(getName(), null, new Field[] {  });
	}
}
