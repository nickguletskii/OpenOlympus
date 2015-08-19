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
package org.ng200.openolympus.jooq.routines;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;

/**
 * This class is generated by jOOQ.
 */
@Generated(value = {
						"http://www.jooq.org",
						"jOOQ version:3.6.2"
}, comments = "This class is generated by jOOQ")
@SuppressWarnings({
					"all",
					"unchecked",
					"rawtypes"
})
public class GetContestEndForUser extends AbstractRoutine<Timestamp> {

	private static final long serialVersionUID = -1213358928;

	/**
	 * The parameter <code>public.get_contest_end_for_user.RETURN_VALUE</code>.
	 */
	public static final Parameter<Timestamp> RETURN_VALUE = AbstractRoutine
			.createParameter("RETURN_VALUE",
					org.jooq.impl.SQLDataType.TIMESTAMP, false);

	/**
	 * The parameter <code>public.get_contest_end_for_user.contest_id</code>.
	 */
	public static final Parameter<Integer> CONTEST_ID = AbstractRoutine
			.createParameter("contest_id", org.jooq.impl.SQLDataType.INTEGER,
					false);

	/**
	 * The parameter <code>public.get_contest_end_for_user.user_id</code>.
	 */
	public static final Parameter<Long> USER_ID = AbstractRoutine
			.createParameter("user_id", org.jooq.impl.SQLDataType.BIGINT,
					false);

	/**
	 * Create a new routine call instance
	 */
	public GetContestEndForUser() {
		super("get_contest_end_for_user", Public.PUBLIC,
				org.jooq.impl.SQLDataType.TIMESTAMP);

		this.setReturnParameter(GetContestEndForUser.RETURN_VALUE);
		this.addInParameter(GetContestEndForUser.CONTEST_ID);
		this.addInParameter(GetContestEndForUser.USER_ID);
	}

	/**
	 * Set the <code>contest_id</code> parameter to the function to be used with
	 * a {@link org.jooq.Select} statement
	 */
	public GetContestEndForUser setContestId(Field<Integer> field) {
		this.setField(GetContestEndForUser.CONTEST_ID, field);
		return this;
	}

	/**
	 * Set the <code>contest_id</code> parameter IN value to the routine
	 */
	public void setContestId(Integer value) {
		this.setValue(GetContestEndForUser.CONTEST_ID, value);
	}

	/**
	 * Set the <code>user_id</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GetContestEndForUser setUserId(Field<Long> field) {
		this.setField(GetContestEndForUser.USER_ID, field);
		return this;
	}

	/**
	 * Set the <code>user_id</code> parameter IN value to the routine
	 */
	public void setUserId(Long value) {
		this.setValue(GetContestEndForUser.USER_ID, value);
	}
}
