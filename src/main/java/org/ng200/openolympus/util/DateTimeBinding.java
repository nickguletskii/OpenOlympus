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
package org.ng200.openolympus.util;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.Objects;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.impl.DSL;

public class DateTimeBinding
		implements Binding<OffsetDateTime, OffsetDateTime> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4382283013179240766L;

	@Override
	public Converter<OffsetDateTime, OffsetDateTime> converter() {
		return new Converter<OffsetDateTime, OffsetDateTime>() {

			/**
			 *
			 */
			private static final long serialVersionUID = -7152099140751140234L;

			@Override
			public OffsetDateTime from(OffsetDateTime databaseObject) {
				return databaseObject;
			}

			@Override
			public Class<OffsetDateTime> fromType() {
				return OffsetDateTime.class;
			}

			@Override
			public OffsetDateTime to(OffsetDateTime userObject) {
				return userObject;
			}

			@Override
			public Class<OffsetDateTime> toType() {
				return OffsetDateTime.class;
			}

		};
	}

	@Override
	public void get(BindingGetResultSetContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.convert(this.converter())
				.value(OffsetDateTime.parse(
						ctx.resultSet().getString(ctx.index()),
						DateUtils.ISO_OFFSET_DATE_TIME));
	}

	@Override
	public void get(BindingGetSQLInputContext<OffsetDateTime> ctx)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetStatementContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.convert(this.converter())
				.value(OffsetDateTime.parse(
						ctx.statement().getString(ctx.index()),
						DateUtils.ISO_OFFSET_DATE_TIME));
	}

	@Override
	public void register(BindingRegisterContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.statement().registerOutParameter(ctx.index(),
				Types.TIMESTAMP_WITH_TIMEZONE);
	}

	@Override
	public void set(BindingSetSQLOutputContext<OffsetDateTime> ctx)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void set(BindingSetStatementContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.statement().setString(ctx.index(),
				Objects.toString(ctx.convert(this.converter()).value(), null));
	}

	@Override
	public void sql(BindingSQLContext<OffsetDateTime> ctx) throws SQLException {
		ctx.render()
				.visit(DSL.val(ctx.convert(this.converter()).value()))
				.sql("::TIMESTAMP WITH TIME ZONE");
	}
}