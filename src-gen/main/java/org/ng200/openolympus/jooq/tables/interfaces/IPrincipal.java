/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


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
@Table(name = "principal", schema = "public")
public interface IPrincipal extends Serializable {

	/**
	 * Setter for <code>public.principal.id</code>.
	 */
	public IPrincipal setId(Long value);

	/**
	 * Getter for <code>public.principal.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	public Long getId();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IPrincipal
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IPrincipal from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IPrincipal
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IPrincipal> E into(E into);
}
