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
package org.ng200.openolympus.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.ContestQuestionRecord;


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
public class ContestQuestion extends TableImpl<ContestQuestionRecord> {

	private static final long serialVersionUID = 157410972;

	/**
	 * The reference instance of <code>public.contest_question</code>
	 */
	public static final ContestQuestion CONTEST_QUESTION = new ContestQuestion();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ContestQuestionRecord> getRecordType() {
		return ContestQuestionRecord.class;
	}

	/**
	 * The column <code>public.contest_question.id</code>.
	 */
	public final TableField<ContestQuestionRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.contest_question.question</code>.
	 */
	public final TableField<ContestQuestionRecord, String> QUESTION = createField("question", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.contest_question.response</code>.
	 */
	public final TableField<ContestQuestionRecord, String> RESPONSE = createField("response", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.contest_question.user_id</code>.
	 */
	public final TableField<ContestQuestionRecord, Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.contest_question.contest_id</code>.
	 */
	public final TableField<ContestQuestionRecord, Integer> CONTEST_ID = createField("contest_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>public.contest_question</code> table reference
	 */
	public ContestQuestion() {
		this("contest_question", null);
	}

	/**
	 * Create an aliased <code>public.contest_question</code> table reference
	 */
	public ContestQuestion(String alias) {
		this(alias, CONTEST_QUESTION);
	}

	private ContestQuestion(String alias, Table<ContestQuestionRecord> aliased) {
		this(alias, aliased, null);
	}

	private ContestQuestion(String alias, Table<ContestQuestionRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ContestQuestionRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CONTEST_QUESTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ContestQuestionRecord> getPrimaryKey() {
		return Keys.CONTEST_QUESTION_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ContestQuestionRecord>> getKeys() {
		return Arrays.<UniqueKey<ContestQuestionRecord>>asList(Keys.CONTEST_QUESTION_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ContestQuestionRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ContestQuestionRecord, ?>>asList(Keys.CONTEST_QUESTION__USER_FK, Keys.CONTEST_QUESTION__CONTEST_FK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestQuestion as(String alias) {
		return new ContestQuestion(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ContestQuestion rename(String name) {
		return new ContestQuestion(name, null);
	}
}
