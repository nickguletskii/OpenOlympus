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
import org.ng200.openolympus.jooq.tables.ContestMessage;
import org.ng200.openolympus.jooq.tables.interfaces.IContestMessage;


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
@Table(name = "contest_message", schema = "public")
public class ContestMessageRecord extends UpdatableRecordImpl<ContestMessageRecord> implements Record5<Integer, String, String, Long, Integer>, IContestMessage {

	private static final long serialVersionUID = 481804980;

	/**
	 * Setter for <code>public.contest_message.id</code>.
	 */
	@Override
	public ContestMessageRecord setId(Integer value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_message.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 32)
	@Override
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_message.question</code>.
	 */
	@Override
	public ContestMessageRecord setQuestion(String value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_message.question</code>.
	 */
	@Column(name = "question")
	@Override
	public String getQuestion() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_message.response</code>.
	 */
	@Override
	public ContestMessageRecord setResponse(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_message.response</code>.
	 */
	@Column(name = "response")
	@Override
	public String getResponse() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>public.contest_message.user_id</code>.
	 */
	@Override
	public ContestMessageRecord setUserId(Long value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_message.user_id</code>.
	 */
	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return (Long) getValue(3);
	}

	/**
	 * Setter for <code>public.contest_message.contest_id</code>.
	 */
	@Override
	public ContestMessageRecord setContestId(Integer value) {
		setValue(4, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_message.contest_id</code>.
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
		return ContestMessage.CONTEST_MESSAGE.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return ContestMessage.CONTEST_MESSAGE.QUESTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return ContestMessage.CONTEST_MESSAGE.RESPONSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field4() {
		return ContestMessage.CONTEST_MESSAGE.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return ContestMessage.CONTEST_MESSAGE.CONTEST_ID;
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
	public ContestMessageRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestMessageRecord value2(String value) {
		setQuestion(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestMessageRecord value3(String value) {
		setResponse(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestMessageRecord value4(Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestMessageRecord value5(Integer value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestMessageRecord values(Integer value1, String value2, String value3, Long value4, Integer value5) {
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
	public void from(IContestMessage from) {
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
	public <E extends IContestMessage> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestMessageRecord
	 */
	public ContestMessageRecord() {
		super(ContestMessage.CONTEST_MESSAGE);
	}

	/**
	 * Create a detached, initialised ContestMessageRecord
	 */
	public ContestMessageRecord(Integer id, String question, String response, Long userId, Integer contestId) {
		super(ContestMessage.CONTEST_MESSAGE);

		setValue(0, id);
		setValue(1, question);
		setValue(2, response);
		setValue(3, userId);
		setValue(4, contestId);
	}
}
