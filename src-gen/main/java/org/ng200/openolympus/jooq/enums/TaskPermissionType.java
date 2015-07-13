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
public enum TaskPermissionType implements EnumType {

	view("view"),

	view_during_contest("view_during_contest"),

	modify("modify"),

	manage_acl("manage_acl");

	private final String literal;

	private TaskPermissionType(String literal) {
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
		return "task_permission_type";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return literal;
	}
}
