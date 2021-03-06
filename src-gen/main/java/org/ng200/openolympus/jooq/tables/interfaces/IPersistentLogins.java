/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
		"jOOQ version:3.7.3"
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
