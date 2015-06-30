/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.IContestParticipation;


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
@Table(name = "contest_participation", schema = "public")
public class ContestParticipation implements IContestParticipation {

	private static final long serialVersionUID = -921966339;

	private Long       id;
	private BigDecimal score;
	private Long       userId;
	private Integer    contestId;

	public ContestParticipation() {}

	public ContestParticipation(ContestParticipation value) {
		this.id = value.id;
		this.score = value.score;
		this.userId = value.userId;
		this.contestId = value.contestId;
	}

	public ContestParticipation(
		Long       id,
		BigDecimal score,
		Long       userId,
		Integer    contestId
	) {
		this.id = id;
		this.score = score;
		this.userId = userId;
		this.contestId = contestId;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public ContestParticipation setId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "score", precision = 19, scale = 2)
	@Override
	public BigDecimal getScore() {
		return this.score;
	}

	@Override
	public ContestParticipation setScore(BigDecimal score) {
		this.score = score;
		return this;
	}

	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return this.userId;
	}

	@Override
	public ContestParticipation setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return this.contestId;
	}

	@Override
	public ContestParticipation setContestId(Integer contestId) {
		this.contestId = contestId;
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IContestParticipation from) {
		setId(from.getId());
		setScore(from.getScore());
		setUserId(from.getUserId());
		setContestId(from.getContestId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IContestParticipation> E into(E into) {
		into.from(this);
		return into;
	}
}
