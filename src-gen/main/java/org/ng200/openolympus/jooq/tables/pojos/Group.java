/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.IGroup;
import org.ng200.openolympus.model.OlympusPrincipal;


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
@Table(name = "group", schema = "public")
public class Group implements OlympusPrincipal, IGroup {

	private static final long serialVersionUID = 1505757783;

	private Long    id;
	private String  name;
	private Boolean hidden;

	public Group() {}

	public Group(Group value) {
		this.id = value.id;
		this.name = value.name;
		this.hidden = value.hidden;
	}

	public Group(
		Long    id,
		String  name,
		Boolean hidden
	) {
		this.id = id;
		this.name = name;
		this.hidden = hidden;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Group setId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "name", unique = true, nullable = false)
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Group setName(String name) {
		this.name = name;
		return this;
	}

	@Column(name = "hidden", nullable = false)
	@Override
	public Boolean getHidden() {
		return this.hidden;
	}

	@Override
	public Group setHidden(Boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IGroup from) {
		setId(from.getId());
		setName(from.getName());
		setHidden(from.getHidden());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IGroup> E into(E into) {
		into.from(this);
		return into;
	}
}
