/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


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
import org.ng200.openolympus.jooq.tables.records.ContestMessageRecord;


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
public class ContestMessage extends TableImpl<ContestMessageRecord> {

	private static final long serialVersionUID = 89625076;

	/**
	 * The reference instance of <code>public.contest_message</code>
	 */
	public static final ContestMessage CONTEST_MESSAGE = new ContestMessage();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ContestMessageRecord> getRecordType() {
		return ContestMessageRecord.class;
	}

	/**
	 * The column <code>public.contest_message.id</code>.
	 */
	public final TableField<ContestMessageRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.contest_message.question</code>.
	 */
	public final TableField<ContestMessageRecord, String> QUESTION = createField("question", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.contest_message.response</code>.
	 */
	public final TableField<ContestMessageRecord, String> RESPONSE = createField("response", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.contest_message.user_id</code>.
	 */
	public final TableField<ContestMessageRecord, Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.contest_message.contest_id</code>.
	 */
	public final TableField<ContestMessageRecord, Integer> CONTEST_ID = createField("contest_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>public.contest_message</code> table reference
	 */
	public ContestMessage() {
		this("contest_message", null);
	}

	/**
	 * Create an aliased <code>public.contest_message</code> table reference
	 */
	public ContestMessage(String alias) {
		this(alias, CONTEST_MESSAGE);
	}

	private ContestMessage(String alias, Table<ContestMessageRecord> aliased) {
		this(alias, aliased, null);
	}

	private ContestMessage(String alias, Table<ContestMessageRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ContestMessageRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CONTEST_MESSAGE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ContestMessageRecord> getPrimaryKey() {
		return Keys.CONTEST_MESSAGES_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ContestMessageRecord>> getKeys() {
		return Arrays.<UniqueKey<ContestMessageRecord>>asList(Keys.CONTEST_MESSAGES_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ContestMessageRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ContestMessageRecord, ?>>asList(Keys.CONTEST_MESSAGE__USER_FK, Keys.CONTEST_MESSAGE__CONTEST_FK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestMessage as(String alias) {
		return new ContestMessage(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ContestMessage rename(String name) {
		return new ContestMessage(name, null);
	}
}
