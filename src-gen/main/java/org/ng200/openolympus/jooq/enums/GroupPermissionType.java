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
public enum GroupPermissionType implements EnumType {

	view_members("view_members"),

	add_member("add_member"),

	remove_member("remove_member"),

	know_about("know_about"),

	edit("edit");

	private final String literal;

	private GroupPermissionType(String literal) {
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
		return "group_permission_type";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return literal;
	}
}
