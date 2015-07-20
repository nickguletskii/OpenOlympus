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
public class Group extends TableImpl<GroupRecord> {

	private static final long serialVersionUID = -738328255;

	/**
	 * The reference instance of <code>public.group</code>
	 */
	public static final Group GROUP = new Group();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<GroupRecord> getRecordType() {
		return GroupRecord.class;
	}

	/**
	 * The column <code>public.group.id</code>.
	 */
	public final TableField<GroupRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.group.name</code>.
	 */
	public final TableField<GroupRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>public.group.hidden</code>.
	 */
	public final TableField<GroupRecord, Boolean> HIDDEN = createField("hidden", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>public.group</code> table reference
	 */
	public Group() {
		this("group", null);
	}

	/**
	 * Create an aliased <code>public.group</code> table reference
	 */
	public Group(String alias) {
		this(alias, GROUP);
	}

	private Group(String alias, Table<GroupRecord> aliased) {
		this(alias, aliased, null);
	}

	private Group(String alias, Table<GroupRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<GroupRecord, Long> getIdentity() {
		return Keys.IDENTITY_GROUP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<GroupRecord> getPrimaryKey() {
		return Keys.GROUP_PK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<GroupRecord>> getKeys() {
		return Arrays.<UniqueKey<GroupRecord>>asList(Keys.GROUP_PK, Keys.GROUP_NAME_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<GroupRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<GroupRecord, ?>>asList(Keys.GROUP__GROUP_PRINCIPAL_ID_MAPPING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Group as(String alias) {
		return new Group(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Group rename(String name) {
		return new Group(name, null);
	}
}
