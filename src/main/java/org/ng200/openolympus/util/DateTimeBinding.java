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
		implements Binding<Object, OffsetDateTime> {

	@Override
	public Converter<Object, OffsetDateTime> converter() {
		return new Converter<Object, OffsetDateTime>() {

			@Override
			public OffsetDateTime from(Object databaseObject) {
				return OffsetDateTime.parse(databaseObject.toString(),
						DateUtils.ISO_LOCAL_DATE_TIME);
			}

			@Override
			public String to(OffsetDateTime userObject) {
				return userObject.format(DateUtils.ISO_LOCAL_DATE_TIME);
			}

			@Override
			public Class<Object> fromType() {
				return Object.class;
			}

			@Override
			public Class<OffsetDateTime> toType() {
				return OffsetDateTime.class;
			}

		};
	}

	@Override
	public void sql(BindingSQLContext<OffsetDateTime> ctx) throws SQLException {
		ctx.render().sql("TIMESTAMP WITH TIME ZONE ")
				.visit(DSL.val(ctx.convert(converter()).value()));
	}

	@Override
	public void register(BindingRegisterContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.statement().registerOutParameter(ctx.index(),
				Types.TIME_WITH_TIMEZONE);
	}

	@Override
	public void set(BindingSetStatementContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.statement().setString(ctx.index(),
				Objects.toString(ctx.convert(converter()).value(), null));
	}

	@Override
	public void get(BindingGetResultSetContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
	}

	@Override
	public void get(BindingGetStatementContext<OffsetDateTime> ctx)
			throws SQLException {
		ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
	}

	@Override
	public void set(BindingSetSQLOutputContext<OffsetDateTime> ctx)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetSQLInputContext<OffsetDateTime> ctx)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}
}