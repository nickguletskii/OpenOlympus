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
@Table(name = "contest_question", schema = "public")
public interface IContestQuestion extends Serializable {

	/**
	 * Setter for <code>public.contest_question.id</code>.
	 */
	public IContestQuestion setId(Integer value);

	/**
	 * Getter for <code>public.contest_question.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 32)
	public Integer getId();

	/**
	 * Setter for <code>public.contest_question.question</code>.
	 */
	public IContestQuestion setQuestion(String value);

	/**
	 * Getter for <code>public.contest_question.question</code>.
	 */
	@Column(name = "question")
	public String getQuestion();

	/**
	 * Setter for <code>public.contest_question.response</code>.
	 */
	public IContestQuestion setResponse(String value);

	/**
	 * Getter for <code>public.contest_question.response</code>.
	 */
	@Column(name = "response")
	public String getResponse();

	/**
	 * Setter for <code>public.contest_question.user_id</code>.
	 */
	public IContestQuestion setUserId(Long value);

	/**
	 * Getter for <code>public.contest_question.user_id</code>.
	 */
	@Column(name = "user_id", nullable = false, precision = 64)
	public Long getUserId();

	/**
	 * Setter for <code>public.contest_question.contest_id</code>.
	 */
	public IContestQuestion setContestId(Integer value);

	/**
	 * Getter for <code>public.contest_question.contest_id</code>.
	 */
	@Column(name = "contest_id", nullable = false, precision = 32)
	public Integer getContestId();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IContestQuestion
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IContestQuestion from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IContestQuestion
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IContestQuestion> E into(E into);
}