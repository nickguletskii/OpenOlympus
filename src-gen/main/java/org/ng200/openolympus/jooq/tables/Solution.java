/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;


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
public class Solution extends TableImpl<SolutionRecord> {

	private static final long serialVersionUID = 471376660;

	/**
	 * The reference instance of <code>public.solution</code>
	 */
	public static final Solution SOLUTION = new Solution();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<SolutionRecord> getRecordType() {
		return SolutionRecord.class;
	}

	/**
	 * The column <code>public.solution.id</code>.
	 */
	public final TableField<SolutionRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.solution.file</code>.
	 */
	public final TableField<SolutionRecord, String> FILE = createField("file", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.solution.maximum_score</code>.
	 */
	public final TableField<SolutionRecord, BigDecimal> MAXIMUM_SCORE = createField("maximum_score", org.jooq.impl.SQLDataType.NUMERIC.precision(19, 2), this, "");

	/**
	 * The column <code>public.solution.score</code>.
	 */
	public final TableField<SolutionRecord, BigDecimal> SCORE = createField("score", org.jooq.impl.SQLDataType.NUMERIC.precision(19, 2), this, "");

	/**
	 * The column <code>public.solution.tested</code>.
	 */
	public final TableField<SolutionRecord, Boolean> TESTED = createField("tested", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

	/**
	 * The column <code>public.solution.time_added</code>.
	 */
	public final TableField<SolutionRecord, OffsetDateTime> TIME_ADDED = createField("time_added", org.ng200.openolympus.jooqsupport.CustomTypes.TIMESTAMPTZ, this, "");

	/**
	 * The column <code>public.solution.user_id</code>.
	 */
	public final TableField<SolutionRecord, Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * The column <code>public.solution.task_id</code>.
	 */
	public final TableField<SolutionRecord, Integer> TASK_ID = createField("task_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>public.solution</code> table reference
	 */
	public Solution() {
		this("solution", null);
	}

	/**
	 * Create an aliased <code>public.solution</code> table reference
	 */
	public Solution(String alias) {
		this(alias, SOLUTION);
	}

	private Solution(String alias, Table<SolutionRecord> aliased) {
		this(alias, aliased, null);
	}

	private Solution(String alias, Table<SolutionRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<SolutionRecord, Long> getIdentity() {
		return Keys.IDENTITY_SOLUTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<SolutionRecord> getPrimaryKey() {
		return Keys.SOLUTION_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<SolutionRecord>> getKeys() {
		return Arrays.<UniqueKey<SolutionRecord>>asList(Keys.SOLUTION_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<SolutionRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<SolutionRecord, ?>>asList(Keys.SOLUTION__USER_FK, Keys.SOLUTION__TASK_FK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Solution as(String alias) {
		return new Solution(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Solution rename(String name) {
		return new Solution(name, null);
	}
}
