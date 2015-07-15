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
