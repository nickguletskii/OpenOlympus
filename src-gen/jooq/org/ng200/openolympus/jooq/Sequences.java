/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;

/**
 * Convenience access to all sequences in public
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

	/**
	 * The sequence <code>public.hibernate_sequence</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> HIBERNATE_SEQUENCE = new org.jooq.impl.SequenceImpl<java.lang.Long>("hibernate_sequence", org.ng200.openolympus.jooq.Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>public.verdicts_id_seq</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> VERDICTS_ID_SEQ = new org.jooq.impl.SequenceImpl<java.lang.Long>("verdicts_id_seq", org.ng200.openolympus.jooq.Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));
}
