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
import org.ng200.openolympus.jooq.tables.records.ContestParticipationRecord;

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
public class ContestParticipation
		extends TableImpl<ContestParticipationRecord> {

	private static final long serialVersionUID = 1031150794;

	/**
	 * The reference instance of <code>public.contest_participation</code>
	 */
	public static final ContestParticipation CONTEST_PARTICIPATION = new ContestParticipation();

	/**
	 * The column <code>public.contest_participation.id</code>.
	 */
	public final TableField<ContestParticipationRecord, Long> ID = AbstractTable
			.createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false)
					.defaulted(true), this, "");

	/**
	 * The column <code>public.contest_participation.score</code>.
	 */
	public final TableField<ContestParticipationRecord, BigDecimal> SCORE = AbstractTable
			.createField("score",
					org.jooq.impl.SQLDataType.NUMERIC.precision(19, 2), this,
					"");

	/**
	 * The column <code>public.contest_participation.user_id</code>.
	 */
	public final TableField<ContestParticipationRecord, Long> USER_ID = AbstractTable
			.createField("user_id",
					org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.contest_participation.contest_id</code>.
	 */
	public final TableField<ContestParticipationRecord, Integer> CONTEST_ID = AbstractTable
			.createField("contest_id",
					org.jooq.impl.SQLDataType.INTEGER.nullable(false), this,
					"");

	/**
	 * Create a <code>public.contest_participation</code> table reference
	 */
	public ContestParticipation() {
		this("contest_participation", null);
	}

	/**
	 * Create an aliased <code>public.contest_participation</code> table
	 * reference
	 */
	public ContestParticipation(String alias) {
		this(alias, ContestParticipation.CONTEST_PARTICIPATION);
	}

	private ContestParticipation(String alias,
			Table<ContestParticipationRecord> aliased) {
		this(alias, aliased, null);
	}

	private ContestParticipation(String alias,
			Table<ContestParticipationRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipation as(String alias) {
		return new ContestParticipation(alias, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ContestParticipationRecord, Long> getIdentity() {
		return Keys.IDENTITY_CONTEST_PARTICIPATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ContestParticipationRecord>> getKeys() {
		return Arrays.<UniqueKey<ContestParticipationRecord>> asList(
				Keys.CONTEST_PARTICIPATION_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ContestParticipationRecord> getPrimaryKey() {
		return Keys.CONTEST_PARTICIPATION_PKEY;
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ContestParticipationRecord> getRecordType() {
		return ContestParticipationRecord.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ContestParticipationRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ContestParticipationRecord, ?>> asList(
				Keys.CONTEST_PARTICIPATION__USER_FK,
				Keys.CONTEST_PARTICIPATION__CONTEST_FK);
	}

	/**
	 * Rename this table
	 */
	public ContestParticipation rename(String name) {
		return new ContestParticipation(name, null);
	}
}
