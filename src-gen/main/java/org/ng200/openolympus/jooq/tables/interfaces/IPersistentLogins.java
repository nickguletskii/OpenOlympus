/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;
import java.time.OffsetDateTime;

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
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "persistent_logins", schema = "public")
public interface IPersistentLogins extends Serializable {

	/**
	 * Setter for <code>public.persistent_logins.username</code>.
	 */
	public IPersistentLogins setUsername(String value);

	/**
	 * Getter for <code>public.persistent_logins.username</code>.
	 */
	@Column(name = "username", nullable = false, length = 64)
	public String getUsername();

	/**
	 * Setter for <code>public.persistent_logins.series</code>.
	 */
	public IPersistentLogins setSeries(String value);

	/**
	 * Getter for <code>public.persistent_logins.series</code>.
	 */
	@Id
	@Column(name = "series", unique = true, nullable = false, length = 64)
	public String getSeries();

	/**
	 * Setter for <code>public.persistent_logins.token</code>.
	 */
	public IPersistentLogins setToken(String value);

	/**
	 * Getter for <code>public.persistent_logins.token</code>.
	 */
	@Column(name = "token", nullable = false, length = 64)
	public String getToken();

	/**
	 * Setter for <code>public.persistent_logins.last_used</code>.
	 */
	public IPersistentLogins setLastUsed(OffsetDateTime value);

	/**
	 * Getter for <code>public.persistent_logins.last_used</code>.
	 */
	@Column(name = "last_used", nullable = false)
	public OffsetDateTime getLastUsed();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IPersistentLogins
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IPersistentLogins from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IPersistentLogins
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IPersistentLogins> E into(E into);
}
