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
import java.math.BigDecimal;
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
@Table(name = "solution", schema = "public")
public interface ISolution extends Serializable {

	/**
	 * Setter for <code>public.solution.id</code>.
	 */
	public ISolution setId(Long value);

	/**
	 * Getter for <code>public.solution.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	public Long getId();

	/**
	 * Setter for <code>public.solution.file</code>.
	 */
	public ISolution setFile(String value);

	/**
	 * Getter for <code>public.solution.file</code>.
	 */
	@Column(name = "file", length = 255)
	public String getFile();

	/**
	 * Setter for <code>public.solution.maximum_score</code>.
	 */
	public ISolution setMaximumScore(BigDecimal value);

	/**
	 * Getter for <code>public.solution.maximum_score</code>.
	 */
	@Column(name = "maximum_score", precision = 19, scale = 2)
	public BigDecimal getMaximumScore();

	/**
	 * Setter for <code>public.solution.score</code>.
	 */
	public ISolution setScore(BigDecimal value);

	/**
	 * Getter for <code>public.solution.score</code>.
	 */
	@Column(name = "score", precision = 19, scale = 2)
	public BigDecimal getScore();

	/**
	 * Setter for <code>public.solution.tested</code>.
	 */
	public ISolution setTested(Boolean value);

	/**
	 * Getter for <code>public.solution.tested</code>.
	 */
	@Column(name = "tested", nullable = false)
	public Boolean getTested();

	/**
	 * Setter for <code>public.solution.time_added</code>.
	 */
	public ISolution setTimeAdded(OffsetDateTime value);

	/**
	 * Getter for <code>public.solution.time_added</code>.
	 */
	@Column(name = "time_added")
	public OffsetDateTime getTimeAdded();

	/**
	 * Setter for <code>public.solution.user_id</code>.
	 */
	public ISolution setUserId(Long value);

	/**
	 * Getter for <code>public.solution.user_id</code>.
	 */
	@Column(name = "user_id", precision = 64)
	public Long getUserId();

	/**
	 * Setter for <code>public.solution.task_id</code>.
	 */
	public ISolution setTaskId(Integer value);

	/**
	 * Getter for <code>public.solution.task_id</code>.
	 */
	@Column(name = "task_id", nullable = false, precision = 32)
	public Integer getTaskId();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ISolution
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.ISolution from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ISolution
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.ISolution> E into(E into);
}
