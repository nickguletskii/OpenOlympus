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
package org.ng200.openolympus.jooq.routines;


import java.time.OffsetDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.util.DateTimeBinding;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetContestEndForUser extends AbstractRoutine<OffsetDateTime> {

	private static final long serialVersionUID = -34820148;

	/**
	 * The parameter <code>public.get_contest_end_for_user.RETURN_VALUE</code>.
	 */
	public static final Parameter<OffsetDateTime> RETURN_VALUE = createParameter("RETURN_VALUE", org.ng200.openolympus.jooqsupport.CustomTypes.OFFSETDATETIME, false, new DateTimeBinding());

	/**
	 * The parameter <code>public.get_contest_end_for_user.contest_id_p</code>.
	 */
	public static final Parameter<Integer> CONTEST_ID_P = createParameter("contest_id_p", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>public.get_contest_end_for_user.user_id_p</code>.
	 */
	public static final Parameter<Long> USER_ID_P = createParameter("user_id_p", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * Create a new routine call instance
	 */
	public GetContestEndForUser() {
		super("get_contest_end_for_user", Public.PUBLIC, org.ng200.openolympus.jooqsupport.CustomTypes.OFFSETDATETIME, new DateTimeBinding());

		setReturnParameter(RETURN_VALUE);
		addInParameter(CONTEST_ID_P);
		addInParameter(USER_ID_P);
	}

	/**
	 * Set the <code>contest_id_p</code> parameter IN value to the routine
	 */
	public void setContestIdP(Integer value) {
		setValue(CONTEST_ID_P, value);
	}

	/**
	 * Set the <code>contest_id_p</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GetContestEndForUser setContestIdP(Field<Integer> field) {
		setField(CONTEST_ID_P, field);
		return this;
	}

	/**
	 * Set the <code>user_id_p</code> parameter IN value to the routine
	 */
	public void setUserIdP(Long value) {
		setValue(USER_ID_P, value);
	}

	/**
	 * Set the <code>user_id_p</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GetContestEndForUser setUserIdP(Field<Long> field) {
		setField(USER_ID_P, field);
		return this;
	}
}
