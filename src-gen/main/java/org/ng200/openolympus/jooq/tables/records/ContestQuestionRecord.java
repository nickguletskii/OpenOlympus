/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.records;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.tables.ContestQuestion;
import org.ng200.openolympus.jooq.tables.interfaces.IContestQuestion;


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
@Table(name = "contest_question", schema = "public")
public class ContestQuestionRecord extends UpdatableRecordImpl<ContestQuestionRecord> implements Record5<Integer, String, String, Long, Integer>, IContestQuestion {

	private static final long serialVersionUID = 2086432537;

	/**
	 * Setter for <code>public.contest_question.id</code>.
	 */
	@Override
	public ContestQuestionRecord setId(Integer value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_question.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 32)
	@Override
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_question.question</code>.
	 */
	@Override
	public ContestQuestionRecord setQuestion(String value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_question.question</code>.
	 */
	@Column(name = "question")
	@Override
	public String getQuestion() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_question.response</code>.
	 */
	@Override
	public ContestQuestionRecord setResponse(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_question.response</code>.
	 */
	@Column(name = "response")
	@Override
	public String getResponse() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>public.contest_question.user_id</code>.
	 */
	@Override
	public ContestQuestionRecord setUserId(Long value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_question.user_id</code>.
	 */
	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return (Long) getValue(3);
	}

	/**
	 * Setter for <code>public.contest_question.contest_id</code>.
	 */
	@Override
	public ContestQuestionRecord setContestId(Integer value) {
		setValue(4, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_question.contest_id</code>.
	 */
	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return (Integer) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, String, String, Long, Integer> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, String, String, Long, Integer> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return ContestQuestion.CONTEST_QUESTION.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return ContestQuestion.CONTEST_QUESTION.QUESTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return ContestQuestion.CONTEST_QUESTION.RESPONSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field4() {
		return ContestQuestion.CONTEST_QUESTION.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return ContestQuestion.CONTEST_QUESTION.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getQuestion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getResponse();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value4() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value5() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionRecord value2(String value) {
		setQuestion(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionRecord value3(String value) {
		setResponse(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionRecord value4(Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionRecord value5(Integer value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionRecord values(Integer value1, String value2, String value3, Long value4, Integer value5) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IContestQuestion from) {
		setId(from.getId());
		setQuestion(from.getQuestion());
		setResponse(from.getResponse());
		setUserId(from.getUserId());
		setContestId(from.getContestId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IContestQuestion> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestQuestionRecord
	 */
	public ContestQuestionRecord() {
		super(ContestQuestion.CONTEST_QUESTION);
	}

	/**
	 * Create a detached, initialised ContestQuestionRecord
	 */
	public ContestQuestionRecord(Integer id, String question, String response, Long userId, Integer contestId) {
		super(ContestQuestion.CONTEST_QUESTION);

		setValue(0, id);
		setValue(1, question);
		setValue(2, response);
		setValue(3, userId);
		setValue(4, contestId);
	}
}
