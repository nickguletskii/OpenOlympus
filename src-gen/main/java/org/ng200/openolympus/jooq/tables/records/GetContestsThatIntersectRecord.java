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


import java.time.Duration;
import java.time.OffsetDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;
import org.ng200.openolympus.jooq.tables.GetContestsThatIntersect;
import org.ng200.openolympus.jooq.tables.interfaces.IGetContestsThatIntersect;


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
@Table(name = "get_contests_that_intersect", schema = "public")
public class GetContestsThatIntersectRecord extends TableRecordImpl<GetContestsThatIntersectRecord> implements Record5<Integer, Duration, String, Boolean, OffsetDateTime>, IGetContestsThatIntersect {

	private static final long serialVersionUID = 1619622783;

	/**
	 * Setter for <code>public.get_contests_that_intersect.id</code>.
	 */
	@Override
	public GetContestsThatIntersectRecord setId(Integer value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.get_contests_that_intersect.id</code>.
	 */
	@Column(name = "id", nullable = false, precision = 32)
	@Override
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>public.get_contests_that_intersect.duration</code>.
	 */
	@Override
	public GetContestsThatIntersectRecord setDuration(Duration value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.get_contests_that_intersect.duration</code>.
	 */
	@Column(name = "duration", precision = 64)
	@Override
	public Duration getDuration() {
		return (Duration) getValue(1);
	}

	/**
	 * Setter for <code>public.get_contests_that_intersect.name</code>.
	 */
	@Override
	public GetContestsThatIntersectRecord setName(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.get_contests_that_intersect.name</code>.
	 */
	@Column(name = "name", length = 255)
	@Override
	public String getName() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>public.get_contests_that_intersect.show_full_tests_during_contest</code>.
	 */
	@Override
	public GetContestsThatIntersectRecord setShowFullTestsDuringContest(Boolean value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.get_contests_that_intersect.show_full_tests_during_contest</code>.
	 */
	@Column(name = "show_full_tests_during_contest", nullable = false)
	@Override
	public Boolean getShowFullTestsDuringContest() {
		return (Boolean) getValue(3);
	}

	/**
	 * Setter for <code>public.get_contests_that_intersect.start_time</code>.
	 */
	@Override
	public GetContestsThatIntersectRecord setStartTime(OffsetDateTime value) {
		setValue(4, value);
		return this;
	}

	/**
	 * Getter for <code>public.get_contests_that_intersect.start_time</code>.
	 */
	@Column(name = "start_time")
	@Override
	public OffsetDateTime getStartTime() {
		return (OffsetDateTime) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, Duration, String, Boolean, OffsetDateTime> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, Duration, String, Boolean, OffsetDateTime> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Duration> field2() {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT.DURATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Boolean> field4() {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT.SHOW_FULL_TESTS_DURING_CONTEST;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<OffsetDateTime> field5() {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT.START_TIME;
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
	public Duration value2() {
		return getDuration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean value4() {
		return getShowFullTestsDuringContest();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OffsetDateTime value5() {
		return getStartTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetContestsThatIntersectRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetContestsThatIntersectRecord value2(Duration value) {
		setDuration(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetContestsThatIntersectRecord value3(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetContestsThatIntersectRecord value4(Boolean value) {
		setShowFullTestsDuringContest(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetContestsThatIntersectRecord value5(OffsetDateTime value) {
		setStartTime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetContestsThatIntersectRecord values(Integer value1, Duration value2, String value3, Boolean value4, OffsetDateTime value5) {
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
	public void from(IGetContestsThatIntersect from) {
		setId(from.getId());
		setDuration(from.getDuration());
		setName(from.getName());
		setShowFullTestsDuringContest(from.getShowFullTestsDuringContest());
		setStartTime(from.getStartTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IGetContestsThatIntersect> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached GetContestsThatIntersectRecord
	 */
	public GetContestsThatIntersectRecord() {
		super(GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT);
	}

	/**
	 * Create a detached, initialised GetContestsThatIntersectRecord
	 */
	public GetContestsThatIntersectRecord(Integer id, Duration duration, String name, Boolean showFullTestsDuringContest, OffsetDateTime startTime) {
		super(GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT);

		setValue(0, id);
		setValue(1, duration);
		setValue(2, name);
		setValue(3, showFullTestsDuringContest);
		setValue(4, startTime);
	}
}
