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
package org.ng200.openolympus.jooq;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Field;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.routines.GetContestEnd;
import org.ng200.openolympus.jooq.routines.GetContestEndForUser;
import org.ng200.openolympus.jooq.routines.GetContestStart;
import org.ng200.openolympus.jooq.routines.GetContestStartForUser;
import org.ng200.openolympus.jooq.routines.GetSolutionAuthor;
import org.ng200.openolympus.jooq.routines.GetSolutionTimeAdded;
import org.ng200.openolympus.jooq.routines.GinExtractQueryTrgm;
import org.ng200.openolympus.jooq.routines.GinExtractValueTrgm;
import org.ng200.openolympus.jooq.routines.GinTrgmConsistent;
import org.ng200.openolympus.jooq.routines.GtrgmCompress;
import org.ng200.openolympus.jooq.routines.GtrgmConsistent;
import org.ng200.openolympus.jooq.routines.GtrgmDecompress;
import org.ng200.openolympus.jooq.routines.GtrgmDistance;
import org.ng200.openolympus.jooq.routines.GtrgmIn;
import org.ng200.openolympus.jooq.routines.GtrgmOut;
import org.ng200.openolympus.jooq.routines.GtrgmPenalty;
import org.ng200.openolympus.jooq.routines.GtrgmPicksplit;
import org.ng200.openolympus.jooq.routines.GtrgmSame;
import org.ng200.openolympus.jooq.routines.GtrgmUnion;
import org.ng200.openolympus.jooq.routines.HasContestPermission;
import org.ng200.openolympus.jooq.routines.HasGeneralPermission;
import org.ng200.openolympus.jooq.routines.HasGroupPermission;
import org.ng200.openolympus.jooq.routines.HasTaskPermission;
import org.ng200.openolympus.jooq.routines.KeepGroupAsPrincipal;
import org.ng200.openolympus.jooq.routines.KeepUserAsMemberOfGroups;
import org.ng200.openolympus.jooq.routines.KeepUserAsPrincipal;
import org.ng200.openolympus.jooq.routines.MaintainContestRank;
import org.ng200.openolympus.jooq.routines.MaintainContestRankWithTask;
import org.ng200.openolympus.jooq.routines.MaintainContestRankWithTimeExtensions;
import org.ng200.openolympus.jooq.routines.MaintainSolutionScore;
import org.ng200.openolympus.jooq.routines.PermissionAppliesToPrincipal;
import org.ng200.openolympus.jooq.routines.RaiseContestIntersectsError;
import org.ng200.openolympus.jooq.routines.SetLimit;
import org.ng200.openolympus.jooq.routines.ShowLimit;
import org.ng200.openolympus.jooq.routines.ShowTrgm;
import org.ng200.openolympus.jooq.routines.Similarity;
import org.ng200.openolympus.jooq.routines.SimilarityDist;
import org.ng200.openolympus.jooq.routines.SimilarityOp;
import org.ng200.openolympus.jooq.routines.UpdateContest;
import org.ng200.openolympus.jooq.routines.UpdateSolution;
import org.ng200.openolympus.jooq.routines.UpdateUserInContest;
import org.ng200.openolympus.jooq.tables.GetContestsThatIntersect;

/**
 * Convenience access to all stored procedures and functions in public
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
public class Routines {

	/**
	 * Call <code>public.get_contest_end</code>
	 */
	public static Timestamp getContestEnd(Configuration configuration,
			Integer contestId) {
		final GetContestEnd f = new GetContestEnd();
		f.setContestId(contestId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_end</code> as a field
	 */
	public static Field<Timestamp> getContestEnd(Field<Integer> contestId) {
		final GetContestEnd f = new GetContestEnd();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_end</code> as a field
	 */
	public static Field<Timestamp> getContestEnd(Integer contestId) {
		final GetContestEnd f = new GetContestEnd();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_contest_end_for_user</code>
	 */
	public static Timestamp getContestEndForUser(Configuration configuration,
			Integer contestId, Long userId) {
		final GetContestEndForUser f = new GetContestEndForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_end_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestEndForUser(
			Field<Integer> contestId, Field<Long> userId) {
		final GetContestEndForUser f = new GetContestEndForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_end_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestEndForUser(Integer contestId,
			Long userId) {
		final GetContestEndForUser f = new GetContestEndForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_contest_start</code>
	 */
	public static Timestamp getContestStart(Configuration configuration,
			Integer contestId) {
		final GetContestStart f = new GetContestStart();
		f.setContestId(contestId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_start</code> as a field
	 */
	public static Field<Timestamp> getContestStart(Field<Integer> contestId) {
		final GetContestStart f = new GetContestStart();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_start</code> as a field
	 */
	public static Field<Timestamp> getContestStart(Integer contestId) {
		final GetContestStart f = new GetContestStart();
		f.setContestId(contestId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_contest_start_for_user</code>
	 */
	public static Timestamp getContestStartForUser(Configuration configuration,
			Integer contestId, Long userId) {
		final GetContestStartForUser f = new GetContestStartForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_contest_start_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestStartForUser(
			Field<Integer> contestId, Field<Long> userId) {
		final GetContestStartForUser f = new GetContestStartForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contest_start_for_user</code> as a field
	 */
	public static Field<Timestamp> getContestStartForUser(Integer contestId,
			Long userId) {
		final GetContestStartForUser f = new GetContestStartForUser();
		f.setContestId(contestId);
		f.setUserId(userId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_contests_that_intersect</code> as a field
	 */
	public static GetContestsThatIntersect getContestsThatIntersect(
			Field<Timestamp> timeRangeStart, Field<Timestamp> timeRangeEnd) {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT
				.call(timeRangeStart, timeRangeEnd);
	}

	/**
	 * Get <code>public.get_contests_that_intersect</code> as a field
	 */
	public static GetContestsThatIntersect getContestsThatIntersect(
			Timestamp timeRangeStart, Timestamp timeRangeEnd) {
		return GetContestsThatIntersect.GET_CONTESTS_THAT_INTERSECT
				.call(timeRangeStart, timeRangeEnd);
	}

	/**
	 * Call <code>public.get_solution_author</code>
	 */
	public static Long getSolutionAuthor(Configuration configuration,
			Long solutionId) {
		final GetSolutionAuthor f = new GetSolutionAuthor();
		f.setSolutionId(solutionId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_solution_author</code> as a field
	 */
	public static Field<Long> getSolutionAuthor(Field<Long> solutionId) {
		final GetSolutionAuthor f = new GetSolutionAuthor();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_solution_author</code> as a field
	 */
	public static Field<Long> getSolutionAuthor(Long solutionId) {
		final GetSolutionAuthor f = new GetSolutionAuthor();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Call <code>public.get_solution_time_added</code>
	 */
	public static Timestamp getSolutionTimeAdded(Configuration configuration,
			Long solutionId) {
		final GetSolutionTimeAdded f = new GetSolutionTimeAdded();
		f.setSolutionId(solutionId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.get_solution_time_added</code> as a field
	 */
	public static Field<Timestamp> getSolutionTimeAdded(
			Field<Long> solutionId) {
		final GetSolutionTimeAdded f = new GetSolutionTimeAdded();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Get <code>public.get_solution_time_added</code> as a field
	 */
	public static Field<Timestamp> getSolutionTimeAdded(Long solutionId) {
		final GetSolutionTimeAdded f = new GetSolutionTimeAdded();
		f.setSolutionId(solutionId);

		return f.asField();
	}

	/**
	 * Call <code>public.gin_extract_query_trgm</code>
	 */
	public static Object ginExtractQueryTrgm(Configuration configuration,
			String __1, Object __2, Short __3, Object __4, Object __5,
			Object __6, Object __7) {
		final GinExtractQueryTrgm f = new GinExtractQueryTrgm();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);
		f.set__6(__6);
		f.set__7(__7);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gin_extract_query_trgm</code> as a field
	 */
	public static Field<Object> ginExtractQueryTrgm(Field<String> __1,
			Field<Object> __2, Field<Short> __3, Field<Object> __4,
			Field<Object> __5, Field<Object> __6, Field<Object> __7) {
		final GinExtractQueryTrgm f = new GinExtractQueryTrgm();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);
		f.set__6(__6);
		f.set__7(__7);

		return f.asField();
	}

	/**
	 * Get <code>public.gin_extract_query_trgm</code> as a field
	 */
	public static Field<Object> ginExtractQueryTrgm(String __1, Object __2,
			Short __3, Object __4, Object __5, Object __6, Object __7) {
		final GinExtractQueryTrgm f = new GinExtractQueryTrgm();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);
		f.set__6(__6);
		f.set__7(__7);

		return f.asField();
	}

	/**
	 * Call <code>public.gin_extract_value_trgm</code>
	 */
	public static Object ginExtractValueTrgm(Configuration configuration,
			String __1, Object __2) {
		final GinExtractValueTrgm f = new GinExtractValueTrgm();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gin_extract_value_trgm</code> as a field
	 */
	public static Field<Object> ginExtractValueTrgm(Field<String> __1,
			Field<Object> __2) {
		final GinExtractValueTrgm f = new GinExtractValueTrgm();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Get <code>public.gin_extract_value_trgm</code> as a field
	 */
	public static Field<Object> ginExtractValueTrgm(String __1, Object __2) {
		final GinExtractValueTrgm f = new GinExtractValueTrgm();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.gin_trgm_consistent</code>
	 */
	public static Boolean ginTrgmConsistent(Configuration configuration,
			Object __1, Short __2, String __3, Integer __4, Object __5,
			Object __6, Object __7, Object __8) {
		final GinTrgmConsistent f = new GinTrgmConsistent();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);
		f.set__6(__6);
		f.set__7(__7);
		f.set__8(__8);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gin_trgm_consistent</code> as a field
	 */
	public static Field<Boolean> ginTrgmConsistent(Field<Object> __1,
			Field<Short> __2, Field<String> __3, Field<Integer> __4,
			Field<Object> __5, Field<Object> __6, Field<Object> __7,
			Field<Object> __8) {
		final GinTrgmConsistent f = new GinTrgmConsistent();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);
		f.set__6(__6);
		f.set__7(__7);
		f.set__8(__8);

		return f.asField();
	}

	/**
	 * Get <code>public.gin_trgm_consistent</code> as a field
	 */
	public static Field<Boolean> ginTrgmConsistent(Object __1, Short __2,
			String __3, Integer __4, Object __5, Object __6, Object __7,
			Object __8) {
		final GinTrgmConsistent f = new GinTrgmConsistent();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);
		f.set__6(__6);
		f.set__7(__7);
		f.set__8(__8);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_compress</code>
	 */
	public static Object gtrgmCompress(Configuration configuration,
			Object __1) {
		final GtrgmCompress f = new GtrgmCompress();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_compress</code> as a field
	 */
	public static Field<Object> gtrgmCompress(Field<Object> __1) {
		final GtrgmCompress f = new GtrgmCompress();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_compress</code> as a field
	 */
	public static Field<Object> gtrgmCompress(Object __1) {
		final GtrgmCompress f = new GtrgmCompress();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_consistent</code>
	 */
	public static Boolean gtrgmConsistent(Configuration configuration,
			Object __1, String __2, Integer __3, Long __4, Object __5) {
		final GtrgmConsistent f = new GtrgmConsistent();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_consistent</code> as a field
	 */
	public static Field<Boolean> gtrgmConsistent(Field<Object> __1,
			Field<String> __2, Field<Integer> __3, Field<Long> __4,
			Field<Object> __5) {
		final GtrgmConsistent f = new GtrgmConsistent();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_consistent</code> as a field
	 */
	public static Field<Boolean> gtrgmConsistent(Object __1, String __2,
			Integer __3, Long __4, Object __5) {
		final GtrgmConsistent f = new GtrgmConsistent();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);
		f.set__5(__5);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_decompress</code>
	 */
	public static Object gtrgmDecompress(Configuration configuration,
			Object __1) {
		final GtrgmDecompress f = new GtrgmDecompress();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_decompress</code> as a field
	 */
	public static Field<Object> gtrgmDecompress(Field<Object> __1) {
		final GtrgmDecompress f = new GtrgmDecompress();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_decompress</code> as a field
	 */
	public static Field<Object> gtrgmDecompress(Object __1) {
		final GtrgmDecompress f = new GtrgmDecompress();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_distance</code>
	 */
	public static Double gtrgmDistance(Configuration configuration, Object __1,
			String __2, Integer __3, Long __4) {
		final GtrgmDistance f = new GtrgmDistance();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_distance</code> as a field
	 */
	public static Field<Double> gtrgmDistance(Field<Object> __1,
			Field<String> __2, Field<Integer> __3, Field<Long> __4) {
		final GtrgmDistance f = new GtrgmDistance();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_distance</code> as a field
	 */
	public static Field<Double> gtrgmDistance(Object __1, String __2,
			Integer __3, Long __4) {
		final GtrgmDistance f = new GtrgmDistance();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);
		f.set__4(__4);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_in</code>
	 */
	public static Object gtrgmIn(Configuration configuration, Object __1) {
		final GtrgmIn f = new GtrgmIn();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_in</code> as a field
	 */
	public static Field<Object> gtrgmIn(Field<Object> __1) {
		final GtrgmIn f = new GtrgmIn();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_in</code> as a field
	 */
	public static Field<Object> gtrgmIn(Object __1) {
		final GtrgmIn f = new GtrgmIn();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_out</code>
	 */
	public static Object gtrgmOut(Configuration configuration, Object __1) {
		final GtrgmOut f = new GtrgmOut();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_out</code> as a field
	 */
	public static Field<Object> gtrgmOut(Field<Object> __1) {
		final GtrgmOut f = new GtrgmOut();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_out</code> as a field
	 */
	public static Field<Object> gtrgmOut(Object __1) {
		final GtrgmOut f = new GtrgmOut();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_penalty</code>
	 */
	public static Object gtrgmPenalty(Configuration configuration, Object __1,
			Object __2, Object __3) {
		final GtrgmPenalty f = new GtrgmPenalty();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_penalty</code> as a field
	 */
	public static Field<Object> gtrgmPenalty(Field<Object> __1,
			Field<Object> __2, Field<Object> __3) {
		final GtrgmPenalty f = new GtrgmPenalty();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_penalty</code> as a field
	 */
	public static Field<Object> gtrgmPenalty(Object __1, Object __2,
			Object __3) {
		final GtrgmPenalty f = new GtrgmPenalty();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_picksplit</code>
	 */
	public static Object gtrgmPicksplit(Configuration configuration, Object __1,
			Object __2) {
		final GtrgmPicksplit f = new GtrgmPicksplit();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_picksplit</code> as a field
	 */
	public static Field<Object> gtrgmPicksplit(Field<Object> __1,
			Field<Object> __2) {
		final GtrgmPicksplit f = new GtrgmPicksplit();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_picksplit</code> as a field
	 */
	public static Field<Object> gtrgmPicksplit(Object __1, Object __2) {
		final GtrgmPicksplit f = new GtrgmPicksplit();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_same</code>
	 */
	public static Object gtrgmSame(Configuration configuration, Object __1,
			Object __2, Object __3) {
		final GtrgmSame f = new GtrgmSame();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_same</code> as a field
	 */
	public static Field<Object> gtrgmSame(Field<Object> __1, Field<Object> __2,
			Field<Object> __3) {
		final GtrgmSame f = new GtrgmSame();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_same</code> as a field
	 */
	public static Field<Object> gtrgmSame(Object __1, Object __2, Object __3) {
		final GtrgmSame f = new GtrgmSame();
		f.set__1(__1);
		f.set__2(__2);
		f.set__3(__3);

		return f.asField();
	}

	/**
	 * Get <code>public.gtrgm_union</code> as a field
	 */
	public static Field<Integer[]> gtrgmUnion(byte[] __1, Object __2) {
		final GtrgmUnion f = new GtrgmUnion();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.gtrgm_union</code>
	 */
	public static Integer[] gtrgmUnion(Configuration configuration, byte[] __1,
			Object __2) {
		final GtrgmUnion f = new GtrgmUnion();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.gtrgm_union</code> as a field
	 */
	public static Field<Integer[]> gtrgmUnion(Field<byte[]> __1,
			Field<Object> __2) {
		final GtrgmUnion f = new GtrgmUnion();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.has_contest_permission</code>
	 */
	public static Boolean hasContestPermission(Configuration configuration,
			Integer contestIdP, Long principalIdP,
			ContestPermissionType permissionP) {
		final HasContestPermission f = new HasContestPermission();
		f.setContestIdP(contestIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.has_contest_permission</code> as a field
	 */
	public static Field<Boolean> hasContestPermission(Field<Integer> contestIdP,
			Field<Long> principalIdP,
			Field<ContestPermissionType> permissionP) {
		final HasContestPermission f = new HasContestPermission();
		f.setContestIdP(contestIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Get <code>public.has_contest_permission</code> as a field
	 */
	public static Field<Boolean> hasContestPermission(Integer contestIdP,
			Long principalIdP, ContestPermissionType permissionP) {
		final HasContestPermission f = new HasContestPermission();
		f.setContestIdP(contestIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Call <code>public.has_general_permission</code>
	 */
	public static Boolean hasGeneralPermission(Configuration configuration,
			Long principalIdP, GeneralPermissionType permissionP) {
		final HasGeneralPermission f = new HasGeneralPermission();
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.has_general_permission</code> as a field
	 */
	public static Field<Boolean> hasGeneralPermission(Field<Long> principalIdP,
			Field<GeneralPermissionType> permissionP) {
		final HasGeneralPermission f = new HasGeneralPermission();
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Get <code>public.has_general_permission</code> as a field
	 */
	public static Field<Boolean> hasGeneralPermission(Long principalIdP,
			GeneralPermissionType permissionP) {
		final HasGeneralPermission f = new HasGeneralPermission();
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Call <code>public.has_group_permission</code>
	 */
	public static Boolean hasGroupPermission(Configuration configuration,
			Long groupIdP, Long principalIdP, GroupPermissionType permissionP) {
		final HasGroupPermission f = new HasGroupPermission();
		f.setGroupIdP(groupIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.has_group_permission</code> as a field
	 */
	public static Field<Boolean> hasGroupPermission(Field<Long> groupIdP,
			Field<Long> principalIdP, Field<GroupPermissionType> permissionP) {
		final HasGroupPermission f = new HasGroupPermission();
		f.setGroupIdP(groupIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Get <code>public.has_group_permission</code> as a field
	 */
	public static Field<Boolean> hasGroupPermission(Long groupIdP,
			Long principalIdP, GroupPermissionType permissionP) {
		final HasGroupPermission f = new HasGroupPermission();
		f.setGroupIdP(groupIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Call <code>public.has_task_permission</code>
	 */
	public static Boolean hasTaskPermission(Configuration configuration,
			Integer taskIdP, Long principalIdP,
			TaskPermissionType permissionP) {
		final HasTaskPermission f = new HasTaskPermission();
		f.setTaskIdP(taskIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.has_task_permission</code> as a field
	 */
	public static Field<Boolean> hasTaskPermission(Field<Integer> taskIdP,
			Field<Long> principalIdP, Field<TaskPermissionType> permissionP) {
		final HasTaskPermission f = new HasTaskPermission();
		f.setTaskIdP(taskIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Get <code>public.has_task_permission</code> as a field
	 */
	public static Field<Boolean> hasTaskPermission(Integer taskIdP,
			Long principalIdP, TaskPermissionType permissionP) {
		final HasTaskPermission f = new HasTaskPermission();
		f.setTaskIdP(taskIdP);
		f.setPrincipalIdP(principalIdP);
		f.setPermissionP(permissionP);

		return f.asField();
	}

	/**
	 * Get <code>public.keep_group_as_principal</code> as a field
	 */
	public static Field<Object> keepGroupAsPrincipal() {
		final KeepGroupAsPrincipal f = new KeepGroupAsPrincipal();

		return f.asField();
	}

	/**
	 * Call <code>public.keep_group_as_principal</code>
	 */
	public static Object keepGroupAsPrincipal(Configuration configuration) {
		final KeepGroupAsPrincipal f = new KeepGroupAsPrincipal();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.keep_user_as_member_of_groups</code> as a field
	 */
	public static Field<Object> keepUserAsMemberOfGroups() {
		final KeepUserAsMemberOfGroups f = new KeepUserAsMemberOfGroups();

		return f.asField();
	}

	/**
	 * Call <code>public.keep_user_as_member_of_groups</code>
	 */
	public static Object keepUserAsMemberOfGroups(Configuration configuration) {
		final KeepUserAsMemberOfGroups f = new KeepUserAsMemberOfGroups();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.keep_user_as_principal</code> as a field
	 */
	public static Field<Object> keepUserAsPrincipal() {
		final KeepUserAsPrincipal f = new KeepUserAsPrincipal();

		return f.asField();
	}

	/**
	 * Call <code>public.keep_user_as_principal</code>
	 */
	public static Object keepUserAsPrincipal(Configuration configuration) {
		final KeepUserAsPrincipal f = new KeepUserAsPrincipal();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_contest_rank</code> as a field
	 */
	public static Field<Object> maintainContestRank() {
		final MaintainContestRank f = new MaintainContestRank();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_contest_rank</code>
	 */
	public static Object maintainContestRank(Configuration configuration) {
		final MaintainContestRank f = new MaintainContestRank();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_contest_rank_with_task</code> as a field
	 */
	public static Field<Object> maintainContestRankWithTask() {
		final MaintainContestRankWithTask f = new MaintainContestRankWithTask();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_contest_rank_with_task</code>
	 */
	public static Object maintainContestRankWithTask(
			Configuration configuration) {
		final MaintainContestRankWithTask f = new MaintainContestRankWithTask();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_contest_rank_with_time_extensions</code> as a
	 * field
	 */
	public static Field<Object> maintainContestRankWithTimeExtensions() {
		final MaintainContestRankWithTimeExtensions f = new MaintainContestRankWithTimeExtensions();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_contest_rank_with_time_extensions</code>
	 */
	public static Object maintainContestRankWithTimeExtensions(
			Configuration configuration) {
		final MaintainContestRankWithTimeExtensions f = new MaintainContestRankWithTimeExtensions();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.maintain_solution_score</code> as a field
	 */
	public static Field<Object> maintainSolutionScore() {
		final MaintainSolutionScore f = new MaintainSolutionScore();

		return f.asField();
	}

	/**
	 * Call <code>public.maintain_solution_score</code>
	 */
	public static Object maintainSolutionScore(Configuration configuration) {
		final MaintainSolutionScore f = new MaintainSolutionScore();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Call <code>public.permission_applies_to_principal</code>
	 */
	public static Boolean permissionAppliesToPrincipal(
			Configuration configuration, Long principalIdP,
			Long permissionPrincipalIdP) {
		final PermissionAppliesToPrincipal f = new PermissionAppliesToPrincipal();
		f.setPrincipalIdP(principalIdP);
		f.setPermissionPrincipalIdP(permissionPrincipalIdP);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.permission_applies_to_principal</code> as a field
	 */
	public static Field<Boolean> permissionAppliesToPrincipal(
			Field<Long> principalIdP, Field<Long> permissionPrincipalIdP) {
		final PermissionAppliesToPrincipal f = new PermissionAppliesToPrincipal();
		f.setPrincipalIdP(principalIdP);
		f.setPermissionPrincipalIdP(permissionPrincipalIdP);

		return f.asField();
	}

	/**
	 * Get <code>public.permission_applies_to_principal</code> as a field
	 */
	public static Field<Boolean> permissionAppliesToPrincipal(Long principalIdP,
			Long permissionPrincipalIdP) {
		final PermissionAppliesToPrincipal f = new PermissionAppliesToPrincipal();
		f.setPrincipalIdP(principalIdP);
		f.setPermissionPrincipalIdP(permissionPrincipalIdP);

		return f.asField();
	}

	/**
	 * Get <code>public.raise_contest_intersects_error</code> as a field
	 */
	public static Field<Object> raiseContestIntersectsError() {
		final RaiseContestIntersectsError f = new RaiseContestIntersectsError();

		return f.asField();
	}

	/**
	 * Call <code>public.raise_contest_intersects_error</code>
	 */
	public static Object raiseContestIntersectsError(
			Configuration configuration) {
		final RaiseContestIntersectsError f = new RaiseContestIntersectsError();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Call <code>public.set_limit</code>
	 */
	public static Float setLimit(Configuration configuration, Float __1) {
		final SetLimit f = new SetLimit();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.set_limit</code> as a field
	 */
	public static Field<Float> setLimit(Field<Float> __1) {
		final SetLimit f = new SetLimit();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.set_limit</code> as a field
	 */
	public static Field<Float> setLimit(Float __1) {
		final SetLimit f = new SetLimit();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.show_limit</code> as a field
	 */
	public static Field<Float> showLimit() {
		final ShowLimit f = new ShowLimit();

		return f.asField();
	}

	/**
	 * Call <code>public.show_limit</code>
	 */
	public static Float showLimit(Configuration configuration) {
		final ShowLimit f = new ShowLimit();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Call <code>public.show_trgm</code>
	 */
	public static String[] showTrgm(Configuration configuration, String __1) {
		final ShowTrgm f = new ShowTrgm();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.show_trgm</code> as a field
	 */
	public static Field<String[]> showTrgm(Field<String> __1) {
		final ShowTrgm f = new ShowTrgm();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>public.show_trgm</code> as a field
	 */
	public static Field<String[]> showTrgm(String __1) {
		final ShowTrgm f = new ShowTrgm();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Call <code>public.similarity</code>
	 */
	public static Float similarity(Configuration configuration, String __1,
			String __2) {
		final Similarity f = new Similarity();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.similarity</code> as a field
	 */
	public static Field<Float> similarity(Field<String> __1,
			Field<String> __2) {
		final Similarity f = new Similarity();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Get <code>public.similarity</code> as a field
	 */
	public static Field<Float> similarity(String __1, String __2) {
		final Similarity f = new Similarity();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.similarity_dist</code>
	 */
	public static Float similarityDist(Configuration configuration, String __1,
			String __2) {
		final SimilarityDist f = new SimilarityDist();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.similarity_dist</code> as a field
	 */
	public static Field<Float> similarityDist(Field<String> __1,
			Field<String> __2) {
		final SimilarityDist f = new SimilarityDist();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Get <code>public.similarity_dist</code> as a field
	 */
	public static Field<Float> similarityDist(String __1, String __2) {
		final SimilarityDist f = new SimilarityDist();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.similarity_op</code>
	 */
	public static Boolean similarityOp(Configuration configuration, String __1,
			String __2) {
		final SimilarityOp f = new SimilarityOp();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>public.similarity_op</code> as a field
	 */
	public static Field<Boolean> similarityOp(Field<String> __1,
			Field<String> __2) {
		final SimilarityOp f = new SimilarityOp();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Get <code>public.similarity_op</code> as a field
	 */
	public static Field<Boolean> similarityOp(String __1, String __2) {
		final SimilarityOp f = new SimilarityOp();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>public.update_contest</code>
	 */
	public static void updateContest(Configuration configuration,
			Long _Param1) {
		final UpdateContest p = new UpdateContest();
		p.set_Param1(_Param1);

		p.execute(configuration);
	}

	/**
	 * Call <code>public.update_solution</code>
	 */
	public static void updateSolution(Configuration configuration,
			Long _Param1) {
		final UpdateSolution p = new UpdateSolution();
		p.set_Param1(_Param1);

		p.execute(configuration);
	}

	/**
	 * Call <code>public.update_user_in_contest</code>
	 */
	public static void updateUserInContest(Configuration configuration,
			Long _Param1, Long _Param2) {
		final UpdateUserInContest p = new UpdateUserInContest();
		p.set_Param1(_Param1);
		p.set_Param2(_Param2);

		p.execute(configuration);
	}
}
