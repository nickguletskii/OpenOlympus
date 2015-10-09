/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.ContestRecord;
import org.ng200.openolympus.util.DateTimeBinding;
import org.ng200.openolympus.util.DurationConverter;


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
public class Contest extends TableImpl<ContestRecord> {

	private static final long serialVersionUID = 2138701941;

	/**
	 * The reference instance of <code>public.contest</code>
	 */
	public static final Contest CONTEST = new Contest();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ContestRecord> getRecordType() {
		return ContestRecord.class;
	}

	/**
	 * The column <code>public.contest.id</code>.
	 */
	public final TableField<ContestRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.contest.duration</code>.
	 */
	public final TableField<ContestRecord, Duration> DURATION = createField("duration", org.jooq.impl.SQLDataType.BIGINT, this, "", new DurationConverter());

	/**
	 * The column <code>public.contest.name</code>.
	 */
	public final TableField<ContestRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.contest.show_full_tests_during_contest</code>.
	 */
	public final TableField<ContestRecord, Boolean> SHOW_FULL_TESTS_DURING_CONTEST = createField("show_full_tests_during_contest", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

	/**
	 * The column <code>public.contest.start_time</code>.
	 */
	public final TableField<ContestRecord, OffsetDateTime> START_TIME = createField("start_time", org.ng200.openolympus.jooqsupport.CustomTypes.OFFSETDATETIME, this, "", new DateTimeBinding());

	/**
	 * Create a <code>public.contest</code> table reference
	 */
	public Contest() {
		this("contest", null);
	}

	/**
	 * Create an aliased <code>public.contest</code> table reference
	 */
	public Contest(String alias) {
		this(alias, CONTEST);
	}

	private Contest(String alias, Table<ContestRecord> aliased) {
		this(alias, aliased, null);
	}

	private Contest(String alias, Table<ContestRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ContestRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CONTEST;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ContestRecord> getPrimaryKey() {
		return Keys.CONTEST_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ContestRecord>> getKeys() {
		return Arrays.<UniqueKey<ContestRecord>>asList(Keys.CONTEST_PKEY, Keys.CONTEST_NAME_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contest as(String alias) {
		return new Contest(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Contest rename(String name) {
		return new Contest(name, null);
	}
}
