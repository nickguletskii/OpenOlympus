/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.interfaces.IPrincipal;


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
public class Principal implements IPrincipal {

	private static final long serialVersionUID = 1127224046;

	private Long                    id;
	private GeneralPermissionType[] permissions;

	public Principal() {}

	public Principal(Principal value) {
		this.id = value.id;
		this.permissions = value.permissions;
	}

	public Principal(
		Long                    id,
		GeneralPermissionType[] permissions
	) {
		this.id = id;
		this.permissions = permissions;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Principal setId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "permissions", nullable = false)
	@Override
	public GeneralPermissionType[] getPermissions() {
		return this.permissions;
	}

	@Override
	public Principal setPermissions(GeneralPermissionType[] permissions) {
		this.permissions = permissions;
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IPrincipal from) {
		setId(from.getId());
		setPermissions(from.getPermissions());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IPrincipal> E into(E into) {
		into.from(this);
		return into;
	}
}
