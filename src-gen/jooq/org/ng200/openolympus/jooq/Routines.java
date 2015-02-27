/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;

/**
 * Convenience access to all stored procedures and functions in public
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

	/**
	 * Call <code>public.get_contest_end</code>
	 */
	public static java.sql.Timestamp getContestEnd(org.jooq.Configuration configuration, java.lang.Long __1) {
		org.ng200.openolympus.jooq.routines.GetContestEnd p = new org.ng200.openolympus.jooq.routines.GetContestEnd();
		p.set__1(__1);

		p.execute(configuration);
		return p.getF1();
	}

	/**
	 * Call <code>public.get_contest_end_for_user</code>
	 */
	public static java.sql.Timestamp getContestEndForUser(org.jooq.Configuration configuration, java.lang.Long __1, java.lang.Long __2) {
		org.ng200.openolympus.jooq.routines.GetContestEndForUser p = new org.ng200.openolympus.jooq.routines.GetContestEndForUser();
		p.set__1(__1);
		p.set__2(__2);

		p.execute(configuration);
		return p.getF1();
	}

	/**
	 * Call <code>public.get_contest_start</code>
	 */
	public static java.sql.Timestamp getContestStart(org.jooq.Configuration configuration, java.lang.Long __1) {
		org.ng200.openolympus.jooq.routines.GetContestStart p = new org.ng200.openolympus.jooq.routines.GetContestStart();
		p.set__1(__1);

		p.execute(configuration);
		return p.getF1();
	}

	/**
	 * Call <code>public.get_contest_start_for_user</code>
	 */
	public static java.sql.Timestamp getContestStartForUser(org.jooq.Configuration configuration, java.lang.Long __1, java.lang.Long __2) {
		org.ng200.openolympus.jooq.routines.GetContestStartForUser p = new org.ng200.openolympus.jooq.routines.GetContestStartForUser();
		p.set__1(__1);
		p.set__2(__2);

		p.execute(configuration);
		return p.getF1();
	}

	/**
	 * Call <code>public.get_solution_author</code>
	 */
	public static java.lang.Long getSolutionAuthor(org.jooq.Configuration configuration, java.lang.Long __1) {
		org.ng200.openolympus.jooq.routines.GetSolutionAuthor p = new org.ng200.openolympus.jooq.routines.GetSolutionAuthor();
		p.set__1(__1);

		p.execute(configuration);
		return p.getF1();
	}

	/**
	 * Call <code>public.get_solution_time_added</code>
	 */
	public static java.sql.Timestamp getSolutionTimeAdded(org.jooq.Configuration configuration, java.lang.Long __1) {
		org.ng200.openolympus.jooq.routines.GetSolutionTimeAdded p = new org.ng200.openolympus.jooq.routines.GetSolutionTimeAdded();
		p.set__1(__1);

		p.execute(configuration);
		return p.getF1();
	}

	/**
	 * Call <code>public.maintain_solution_score</code>
	 */
	public static java.lang.Object maintainSolutionScore(org.jooq.Configuration configuration) {
		org.ng200.openolympus.jooq.routines.MaintainSolutionScore f = new org.ng200.openolympus.jooq.routines.MaintainSolutionScore();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_solution_score</code> as a field
	 */
	public static org.jooq.Field<java.lang.Object> maintainSolutionScore() {
		org.ng200.openolympus.jooq.routines.MaintainSolutionScore f = new org.ng200.openolympus.jooq.routines.MaintainSolutionScore();

		return f.asField();
	}

	/**
	 * Get <code>public.contest_at</code> as a field
	 */
	public static org.ng200.openolympus.jooq.tables.ContestAt contestAt(java.sql.Timestamp __1) {
		return org.ng200.openolympus.jooq.tables.ContestAt.CONTEST_AT.call(__1);
	}

	/**
	 * Get <code>public.contest_at</code> as a field
	 */
	public static org.ng200.openolympus.jooq.tables.ContestAt contestAt(org.jooq.Field<java.sql.Timestamp> __1) {
		return org.ng200.openolympus.jooq.tables.ContestAt.CONTEST_AT.call(__1);
	}

	/**
	 * Get <code>public.contest_at_for_user</code> as a field
	 */
	public static org.ng200.openolympus.jooq.tables.ContestAtForUser contestAtForUser(java.sql.Timestamp __1, java.lang.Long __2) {
		return org.ng200.openolympus.jooq.tables.ContestAtForUser.CONTEST_AT_FOR_USER.call(__1, __2);
	}

	/**
	 * Get <code>public.contest_at_for_user</code> as a field
	 */
	public static org.ng200.openolympus.jooq.tables.ContestAtForUser contestAtForUser(org.jooq.Field<java.sql.Timestamp> __1, org.jooq.Field<java.lang.Long> __2) {
		return org.ng200.openolympus.jooq.tables.ContestAtForUser.CONTEST_AT_FOR_USER.call(__1, __2);
	}
}
