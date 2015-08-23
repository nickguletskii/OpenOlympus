/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.enums;


import javax.annotation.Generated;

import org.jooq.EnumType;
import org.jooq.Schema;
import org.ng200.openolympus.jooq.Public;


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
public enum ContestPermissionType implements EnumType {

	edit("edit"),

	view_tasks_before_contest_started("view_tasks_before_contest_started"),

	delete("delete"),

	add_user("add_user"),

	add_task("add_task"),

	list_tasks("list_tasks"),

	extend_time("extend_time"),

	know_about("know_about"),

	manage_acl("manage_acl"),

	participate("participate");

	private final String literal;

	private ContestPermissionType(String literal) {
		this.literal = literal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Schema getSchema() {
		return Public.PUBLIC;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "contest_permission_type";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return literal;
	}
}
