/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.tables.ContestParticipation;
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
public class ContestParticipationRecord extends UpdatableRecordImpl<ContestParticipationRecord> implements Record4<Long, BigDecimal, Long, Integer>, IContestParticipation {

	private static final long serialVersionUID = -1889057448;

	/**
	 * Setter for <code>public.contest_participation.id</code>.
	 */
	@Override
	public ContestParticipationRecord setId(Long value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_participation.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_participation.score</code>.
	 */
	@Override
	public ContestParticipationRecord setScore(BigDecimal value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_participation.score</code>.
	 */
	@Column(name = "score", precision = 19, scale = 2)
	@Override
	public BigDecimal getScore() {
		return (BigDecimal) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_participation.user_id</code>.
	 */
	@Override
	public ContestParticipationRecord setUserId(Long value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_participation.user_id</code>.
	 */
	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return (Long) getValue(2);
	}

	/**
	 * Setter for <code>public.contest_participation.contest_id</code>.
	 */
	@Override
	public ContestParticipationRecord setContestId(Integer value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_participation.contest_id</code>.
	 */
	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return (Integer) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Long, BigDecimal, Long, Integer> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Long, BigDecimal, Long, Integer> valuesRow() {
		return (Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return ContestParticipation.CONTEST_PARTICIPATION.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<BigDecimal> field2() {
		return ContestParticipation.CONTEST_PARTICIPATION.SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field3() {
		return ContestParticipation.CONTEST_PARTICIPATION.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return ContestParticipation.CONTEST_PARTICIPATION.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal value2() {
		return getScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value3() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value4() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationRecord value2(BigDecimal value) {
		setScore(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationRecord value3(Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationRecord value4(Integer value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationRecord values(Long value1, BigDecimal value2, Long value3, Integer value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
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

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestParticipationRecord
	 */
	public ContestParticipationRecord() {
		super(ContestParticipation.CONTEST_PARTICIPATION);
	}

	/**
	 * Create a detached, initialised ContestParticipationRecord
	 */
	public ContestParticipationRecord(Long id, BigDecimal score, Long userId, Integer contestId) {
		super(ContestParticipation.CONTEST_PARTICIPATION);

		setValue(0, id);
		setValue(1, score);
		setValue(2, userId);
		setValue(3, contestId);
	}
}