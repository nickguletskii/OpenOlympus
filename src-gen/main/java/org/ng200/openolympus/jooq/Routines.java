/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Field;
import org.ng200.openolympus.jooq.routines.GetContestEnd;
import org.ng200.openolympus.jooq.routines.GetContestEndForUser;
import org.ng200.openolympus.jooq.routines.GetContestStart;
import org.ng200.openolympus.jooq.routines.GetContestStartForUser;
import org.ng200.openolympus.jooq.routines.GetSolutionAuthor;
import org.ng200.openolympus.jooq.routines.GetSolutionTimeAdded;
import org.ng200.openolympus.jooq.routines.KeepUserAsPrincipal;
import org.ng200.openolympus.jooq.routines.MaintainContestRank;
import org.ng200.openolympus.jooq.routines.MaintainContestRankWithTask;
import org.ng200.openolympus.jooq.routines.MaintainContestRankWithTimeExtensions;
import org.ng200.openolympus.jooq.routines.MaintainSolutionScore;
import org.ng200.openolympus.jooq.routines.UpdateContest;
import org.ng200.openolympus.jooq.routines.UpdateSolution;
import org.ng200.openolympus.jooq.routines.UpdateUserInContest;


/**
 * Convenience access to all stored procedures and functions in public
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

	/**
	 * Call <code>public.get_contest_end</code>
	 */
	public static Timestamp getContestEnd(Configuration configuration, Integer contestId) {
		GetContestEnd f = new GetContestEnd();
		f.setContestId(contestId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_end</code> as a field
	 */
	public static Field<Timestamp> getContestEnd(Integer contestId) {
		GetContestEnd f = new GetContestEnd();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_end</code> as a field
	 */
	public static Field<Timestamp> getContestEnd(Field<Integer> contestId) {
		GetContestEnd f = new GetContestEnd();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_contest_end_for_user</code>
	 */
	public static Timestamp getContestEndForUser(Configuration configuration, Integer contestId, Long userId) {
		GetContestEndForUser f = new GetContestEndForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_end_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestEndForUser(Integer contestId, Long userId) {
		GetContestEndForUser f = new GetContestEndForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_end_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestEndForUser(Field<Integer> contestId, Field<Long> userId) {
		GetContestEndForUser f = new GetContestEndForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_contest_start</code>
	 */
	public static Timestamp getContestStart(Configuration configuration, Integer contestId) {
		GetContestStart f = new GetContestStart();
		f.setContestId(contestId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_start</code> as a field
	 */
	public static Field<Timestamp> getContestStart(Integer contestId) {
		GetContestStart f = new GetContestStart();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_start</code> as a field
	 */
	public static Field<Timestamp> getContestStart(Field<Integer> contestId) {
		GetContestStart f = new GetContestStart();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_contest_start_for_user</code>
	 */
	public static Timestamp getContestStartForUser(Configuration configuration, Integer contestId, Long userId) {
		GetContestStartForUser f = new GetContestStartForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_start_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestStartForUser(Integer contestId, Long userId) {
		GetContestStartForUser f = new GetContestStartForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_start_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestStartForUser(Field<Integer> contestId, Field<Long> userId) {
		GetContestStartForUser f = new GetContestStartForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_solution_author</code>
	 */
	public static Long getSolutionAuthor(Configuration configuration, Long solutionId) {
		GetSolutionAuthor f = new GetSolutionAuthor();
		f.setSolutionId(solutionId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_solution_author</code> as a field
	 */
	public static Field<Long> getSolutionAuthor(Long solutionId) {
		GetSolutionAuthor f = new GetSolutionAuthor();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_solution_author</code> as a field
	 */
	public static Field<Long> getSolutionAuthor(Field<Long> solutionId) {
		GetSolutionAuthor f = new GetSolutionAuthor();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_solution_time_added</code>
	 */
	public static Timestamp getSolutionTimeAdded(Configuration configuration, Long solutionId) {
		GetSolutionTimeAdded f = new GetSolutionTimeAdded();
		f.setSolutionId(solutionId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_solution_time_added</code> as a field
	 */
	public static Field<Timestamp> getSolutionTimeAdded(Long solutionId) {
		GetSolutionTimeAdded f = new GetSolutionTimeAdded();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_solution_time_added</code> as a field
	 */
	public static Field<Timestamp> getSolutionTimeAdded(Field<Long> solutionId) {
		GetSolutionTimeAdded f = new GetSolutionTimeAdded();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Call <code>public.keep_user_as_principal</code>
	 */
	public static Object keepUserAsPrincipal(Configuration configuration) {
		KeepUserAsPrincipal f = new KeepUserAsPrincipal();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.keep_user_as_principal</code> as a field
	 */
	public static Field<Object> keepUserAsPrincipal() {
		KeepUserAsPrincipal f = new KeepUserAsPrincipal();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_contest_rank</code>
	 */
	public static Object maintainContestRank(Configuration configuration) {
		MaintainContestRank f = new MaintainContestRank();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_contest_rank</code> as a field
	 */
	public static Field<Object> maintainContestRank() {
		MaintainContestRank f = new MaintainContestRank();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_contest_rank_with_task</code>
	 */
	public static Object maintainContestRankWithTask(Configuration configuration) {
		MaintainContestRankWithTask f = new MaintainContestRankWithTask();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_contest_rank_with_task</code> as a field
	 */
	public static Field<Object> maintainContestRankWithTask() {
		MaintainContestRankWithTask f = new MaintainContestRankWithTask();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_contest_rank_with_time_extensions</code>
	 */
	public static Object maintainContestRankWithTimeExtensions(Configuration configuration) {
		MaintainContestRankWithTimeExtensions f = new MaintainContestRankWithTimeExtensions();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_contest_rank_with_time_extensions</code> as a field
	 */
	public static Field<Object> maintainContestRankWithTimeExtensions() {
		MaintainContestRankWithTimeExtensions f = new MaintainContestRankWithTimeExtensions();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_solution_score</code>
	 */
	public static Object maintainSolutionScore(Configuration configuration) {
		MaintainSolutionScore f = new MaintainSolutionScore();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_solution_score</code> as a field
	 */
	public static Field<Object> maintainSolutionScore() {
		MaintainSolutionScore f = new MaintainSolutionScore();

		return f.asField();
	}

	/**
	 * Call <code>public.update_contest</code>
	 */
	public static void updateContest(Configuration configuration, Long _Param1) {
		UpdateContest p = new UpdateContest();
		p.set_Param1(_Param1);

		p.execute(configuration);
	}

	/**
	 * Call <code>public.update_solution</code>
	 */
	public static void updateSolution(Configuration configuration, Long _Param1) {
		UpdateSolution p = new UpdateSolution();
		p.set_Param1(_Param1);

		p.execute(configuration);
	}

	/**
	 * Call <code>public.update_user_in_contest</code>
	 */
	public static void updateUserInContest(Configuration configuration, Long _Param1, Long _Param2) {
		UpdateUserInContest p = new UpdateUserInContest();
		p.set_Param1(_Param1);
		p.set_Param2(_Param2);

		p.execute(configuration);
	}
}