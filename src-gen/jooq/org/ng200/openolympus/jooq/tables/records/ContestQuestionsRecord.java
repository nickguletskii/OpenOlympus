/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContestQuestionsRecord extends org.jooq.impl.UpdatableRecordImpl<org.ng200.openolympus.jooq.tables.records.ContestQuestionsRecord> implements org.jooq.Record9<java.lang.Long, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long> {

	private static final long serialVersionUID = 1473438460;

	/**
	 * Setter for <code>public.contest_questions.id</code>.
	 */
	public void setId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.contest_questions.id</code>.
	 */
	public java.lang.Long getId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_questions.created_date</code>.
	 */
	public void setCreatedDate(java.sql.Timestamp value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.contest_questions.created_date</code>.
	 */
	public java.sql.Timestamp getCreatedDate() {
		return (java.sql.Timestamp) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_questions.last_modified_date</code>.
	 */
	public void setLastModifiedDate(java.sql.Timestamp value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>public.contest_questions.last_modified_date</code>.
	 */
	public java.sql.Timestamp getLastModifiedDate() {
		return (java.sql.Timestamp) getValue(2);
	}

	/**
	 * Setter for <code>public.contest_questions.question</code>.
	 */
	public void setQuestion(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>public.contest_questions.question</code>.
	 */
	public java.lang.String getQuestion() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>public.contest_questions.response</code>.
	 */
	public void setResponse(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>public.contest_questions.response</code>.
	 */
	public java.lang.String getResponse() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>public.contest_questions.contest_id</code>.
	 */
	public void setContestId(java.lang.Long value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>public.contest_questions.contest_id</code>.
	 */
	public java.lang.Long getContestId() {
		return (java.lang.Long) getValue(5);
	}

	/**
	 * Setter for <code>public.contest_questions.created_by_id</code>.
	 */
	public void setCreatedById(java.lang.Long value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>public.contest_questions.created_by_id</code>.
	 */
	public java.lang.Long getCreatedById() {
		return (java.lang.Long) getValue(6);
	}

	/**
	 * Setter for <code>public.contest_questions.last_modified_by_id</code>.
	 */
	public void setLastModifiedById(java.lang.Long value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>public.contest_questions.last_modified_by_id</code>.
	 */
	public java.lang.Long getLastModifiedById() {
		return (java.lang.Long) getValue(7);
	}

	/**
	 * Setter for <code>public.contest_questions.user_id</code>.
	 */
	public void setUserId(java.lang.Long value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>public.contest_questions.user_id</code>.
	 */
	public java.lang.Long getUserId() {
		return (java.lang.Long) getValue(8);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Long> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record9 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Long, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long> fieldsRow() {
		return (org.jooq.Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Long, java.sql.Timestamp, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long> valuesRow() {
		return (org.jooq.Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field2() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.CREATED_DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field3() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.LAST_MODIFIED_DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.QUESTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.RESPONSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field6() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field7() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.CREATED_BY_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field8() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.LAST_MODIFIED_BY_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field9() {
		return org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value2() {
		return getCreatedDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value3() {
		return getLastModifiedDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getQuestion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getResponse();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value6() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value7() {
		return getCreatedById();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value8() {
		return getLastModifiedById();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value9() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value1(java.lang.Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value2(java.sql.Timestamp value) {
		setCreatedDate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value3(java.sql.Timestamp value) {
		setLastModifiedDate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value4(java.lang.String value) {
		setQuestion(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value5(java.lang.String value) {
		setResponse(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value6(java.lang.Long value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value7(java.lang.Long value) {
		setCreatedById(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value8(java.lang.Long value) {
		setLastModifiedById(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord value9(java.lang.Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestionsRecord values(java.lang.Long value1, java.sql.Timestamp value2, java.sql.Timestamp value3, java.lang.String value4, java.lang.String value5, java.lang.Long value6, java.lang.Long value7, java.lang.Long value8, java.lang.Long value9) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestQuestionsRecord
	 */
	public ContestQuestionsRecord() {
		super(org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS);
	}

	/**
	 * Create a detached, initialised ContestQuestionsRecord
	 */
	public ContestQuestionsRecord(java.lang.Long id, java.sql.Timestamp createdDate, java.sql.Timestamp lastModifiedDate, java.lang.String question, java.lang.String response, java.lang.Long contestId, java.lang.Long createdById, java.lang.Long lastModifiedById, java.lang.Long userId) {
		super(org.ng200.openolympus.jooq.tables.ContestQuestions.CONTEST_QUESTIONS);

		setValue(0, id);
		setValue(1, createdDate);
		setValue(2, lastModifiedDate);
		setValue(3, question);
		setValue(4, response);
		setValue(5, contestId);
		setValue(6, createdById);
		setValue(7, lastModifiedById);
		setValue(8, userId);
	}
}
