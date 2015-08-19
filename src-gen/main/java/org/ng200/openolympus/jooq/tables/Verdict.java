/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.enums.VerdictStatusType;
import org.ng200.openolympus.jooq.tables.records.VerdictRecord;
import org.ng200.openolympus.util.DurationConverter;

/**
 * This class is generated by jOOQ.
 */
@Generated(value = {
						"http://www.jooq.org",
						"jOOQ version:3.6.2"
}, comments = "This class is generated by jOOQ")
@SuppressWarnings({
					"all",
					"unchecked",
					"rawtypes"
})
public class Verdict extends TableImpl<VerdictRecord> {

	private static final long serialVersionUID = 792849548;

	/**
	 * The reference instance of <code>public.verdict</code>
	 */
	public static final Verdict VERDICT = new Verdict();

	/**
	 * The column <code>public.verdict.id</code>.
	 */
	public final TableField<VerdictRecord, Long> ID = AbstractTable.createField(
			"id",
			org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true),
			this, "");

	/**
	 * The column <code>public.verdict.score</code>.
	 */
	public final TableField<VerdictRecord, BigDecimal> SCORE = AbstractTable
			.createField("score",
					org.jooq.impl.SQLDataType.NUMERIC.precision(19, 2), this,
					"");

	/**
	 * The column <code>public.verdict.maximum_score</code>.
	 */
	public final TableField<VerdictRecord, BigDecimal> MAXIMUM_SCORE = AbstractTable
			.createField("maximum_score", org.jooq.impl.SQLDataType.NUMERIC
					.precision(19, 2).nullable(false), this, "");

	/**
	 * The column <code>public.verdict.status</code>.
	 */
	public final TableField<VerdictRecord, VerdictStatusType> STATUS = AbstractTable
			.createField("status",
					org.jooq.util.postgres.PostgresDataType.VARCHAR
							.asEnumDataType(
									org.ng200.openolympus.jooq.enums.VerdictStatusType.class),
					this, "");

	/**
	 * The column <code>public.verdict.viewable_during_contest</code>.
	 */
	public final TableField<VerdictRecord, Boolean> VIEWABLE_DURING_CONTEST = AbstractTable
			.createField("viewable_during_contest",
					org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this,
					"");

	/**
	 * The column <code>public.verdict.path</code>.
	 */
	public final TableField<VerdictRecord, String> PATH = AbstractTable
			.createField("path", org.jooq.impl.SQLDataType.CLOB.nullable(false),
					this, "");

	/**
	 * The column <code>public.verdict.cpu_time</code>.
	 */
	public final TableField<VerdictRecord, Duration> CPU_TIME = AbstractTable
			.createField("cpu_time", org.jooq.impl.SQLDataType.BIGINT, this, "",
					new DurationConverter());

	/**
	 * The column <code>public.verdict.real_time</code>.
	 */
	public final TableField<VerdictRecord, Duration> REAL_TIME = AbstractTable
			.createField("real_time", org.jooq.impl.SQLDataType.BIGINT, this,
					"", new DurationConverter());

	/**
	 * The column <code>public.verdict.memory_peak</code>.
	 */
	public final TableField<VerdictRecord, Long> MEMORY_PEAK = AbstractTable
			.createField("memory_peak", org.jooq.impl.SQLDataType.BIGINT, this,
					"");

	/**
	 * The column <code>public.verdict.additional_information</code>.
	 */
	public final TableField<VerdictRecord, String> ADDITIONAL_INFORMATION = AbstractTable
			.createField("additional_information",
					org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.verdict.solution_id</code>.
	 */
	public final TableField<VerdictRecord, Long> SOLUTION_ID = AbstractTable
			.createField("solution_id", org.jooq.impl.SQLDataType.BIGINT, this,
					"");

	/**
	 * Create a <code>public.verdict</code> table reference
	 */
	public Verdict() {
		this("verdict", null);
	}

	/**
	 * Create an aliased <code>public.verdict</code> table reference
	 */
	public Verdict(String alias) {
		this(alias, Verdict.VERDICT);
	}

	private Verdict(String alias, Table<VerdictRecord> aliased) {
		this(alias, aliased, null);
	}

	private Verdict(String alias, Table<VerdictRecord> aliased,
			Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Verdict as(String alias) {
		return new Verdict(alias, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<VerdictRecord, Long> getIdentity() {
		return Keys.IDENTITY_VERDICT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<VerdictRecord>> getKeys() {
		return Arrays.<UniqueKey<VerdictRecord>> asList(Keys.VERDICT_PK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<VerdictRecord> getPrimaryKey() {
		return Keys.VERDICT_PK;
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<VerdictRecord> getRecordType() {
		return VerdictRecord.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<VerdictRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<VerdictRecord, ?>> asList(
				Keys.VERDICT__SOLUTION_FK);
	}

	/**
	 * Rename this table
	 */
	public Verdict rename(String name) {
		return new Verdict(name, null);
	}
}
