/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


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
@Entity
@Table(name = "contest_permission_principal", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"contest_permission_id", "principal_id"})
})
public interface IContestPermissionPrincipal extends Serializable {

	/**
	 * Setter for <code>public.contest_permission_principal.contest_permission_id</code>.
	 */
	public IContestPermissionPrincipal setContestPermissionId(Long value);

	/**
	 * Getter for <code>public.contest_permission_principal.contest_permission_id</code>.
	 */
	@Column(name = "contest_permission_id", nullable = false, precision = 64)
	public Long getContestPermissionId();

	/**
	 * Setter for <code>public.contest_permission_principal.principal_id</code>.
	 */
	public IContestPermissionPrincipal setPrincipalId(Long value);

	/**
	 * Getter for <code>public.contest_permission_principal.principal_id</code>.
	 */
	@Column(name = "principal_id", nullable = false, precision = 64)
	public Long getPrincipalId();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IContestPermissionPrincipal
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IContestPermissionPrincipal from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IContestPermissionPrincipal
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IContestPermissionPrincipal> E into(E into);
}
