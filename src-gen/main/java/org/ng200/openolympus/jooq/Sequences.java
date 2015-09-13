/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in public
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

	/**
	 * The sequence <code>public.contest_id_seq</code>
	 */
	public static final Sequence<Long> CONTEST_ID_SEQ = new SequenceImpl<Long>("contest_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.contest_message_id_seq</code>
	 */
	public static final Sequence<Long> CONTEST_MESSAGE_ID_SEQ = new SequenceImpl<Long>("contest_message_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.contest_participation_id_seq</code>
	 */
	public static final Sequence<Long> CONTEST_PARTICIPATION_ID_SEQ = new SequenceImpl<Long>("contest_participation_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.contest_question_id_seq</code>
	 */
	public static final Sequence<Long> CONTEST_QUESTION_ID_SEQ = new SequenceImpl<Long>("contest_question_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.principal_sequence</code>
	 */
	public static final Sequence<Long> PRINCIPAL_SEQUENCE = new SequenceImpl<Long>("principal_sequence", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.solution_id_seq</code>
	 */
	public static final Sequence<Long> SOLUTION_ID_SEQ = new SequenceImpl<Long>("solution_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.task_id_seq</code>
	 */
	public static final Sequence<Long> TASK_ID_SEQ = new SequenceImpl<Long>("task_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.time_extension_id_seq</code>
	 */
	public static final Sequence<Long> TIME_EXTENSION_ID_SEQ = new SequenceImpl<Long>("time_extension_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.verdict_id_seq</code>
	 */
	public static final Sequence<Long> VERDICT_ID_SEQ = new SequenceImpl<Long>("verdict_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));
}
