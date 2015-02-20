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
package org.ng200.openolympus.services;

import static org.ng200.openolympus.SecurityExpressionConstants.AND;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_USER;
import static org.ng200.openolympus.SecurityExpressionConstants.NO_CONTEST_CURRENTLY;
import static org.ng200.openolympus.SecurityExpressionConstants.OR;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.ContestParticipation;
import org.ng200.openolympus.model.ContestPerUserTimeExtension;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.ContestParticipationRepository;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.repositories.ContestTimeExtensionRepository;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestService {

	private static final Logger logger = LoggerFactory
			.getLogger(ContestService.class);
	private static final int CONTEST_RESULTS_PAGE_LENGTH = 10;
	private static final int CONTEST_PARTICIPANTS_PAGE_LENGTH = 10;
	@Autowired
	private ContestRepository contestRepository;
	@Autowired
	private ContestParticipationRepository contestParticipationRepository;
	@Autowired
	private ContestTimeExtensionRepository contestTimeExtensionRepository;

	@Autowired
	private SolutionRepository solutionRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	@PreAuthorize(IS_ADMIN)
	public void addContestParticipant(final Contest contest, final User user) {
		this.contestParticipationRepository.save(new ContestParticipation(
				contest, user));
	}

	@PreAuthorize(IS_USER)
	public long countContests() {
		return this.contestRepository.count();
	}

	@PreAuthorize(IS_ADMIN)
	@Transactional
	public void deleteContest(Contest contest) {
		contest = this.contestRepository.findOne(contest.getId());
		contest.getTasks().clear();
		contest = this.contestRepository.save(contest);
		this.contestParticipationRepository
				.delete(this.contestParticipationRepository
						.findByContest(contest));
		this.contestTimeExtensionRepository
				.delete(this.contestTimeExtensionRepository
						.findByContest(contest));
		this.contestRepository.delete(contest);
	}

	@PreAuthorize(IS_ADMIN)
	public void extendTimeForUser(final Contest contest, final User user,
			final long time) {
		this.contestTimeExtensionRepository
				.save(new ContestPerUserTimeExtension(contest, user, time));
	}

	@PreAuthorize(IS_ADMIN)
	public Contest getContestByName(final String name) {
		return this.contestRepository.findByName(name);
	}

	@PreAuthorize(IS_USER)
	public Instant getContestEndIncludingAllTimeExtensions(final Contest contest) {
		return contest
				.getStartTime()
				.toInstant()
				.plusMillis(contest.getDuration())
				.plusMillis(
						this.contestTimeExtensionRepository
								.findByContest(contest)
								.stream()
								.collect(
										Collectors
												.groupingBy(timeExtension -> timeExtension
														.getContest()))
								.values()
								.stream()
								.map(group -> group
										.stream()
										.map(timeExtension -> timeExtension
												.getDuration())
										.reduce((l, r) -> l + r))
								.max((l, r) -> l.orElse(0l).compareTo(
										r.orElse(0l))).orElse(Optional.of(0l))
								.orElse(0l));
	}

	@PreAuthorize(IS_USER)
	public Date getContestEndTime(final Contest contest) {
		return Date.from(contest.getStartTime().toInstant()
				.plusMillis(contest.getDuration()));
	}

	@PreAuthorize(IS_ADMIN + OR + '('
			+ " #user.username == authentication.name " + ')')
	public Date getContestEndTimeForUser(final Contest contest, final User user) {
		return Date.from(contest
				.getStartTime()
				.toInstant()
				.plusMillis(
						contest.getDuration()
								+ this.getTotalTimeExtensionTimeForUser(
										contest, user)));
	}

	@PreAuthorize(IS_ADMIN)
	public List<UserRanking> getContestResults(Contest contest) {
		return this.userRepository
				.getContestResults(contest.getId(), contest.getStartTime(),
						this.getContestEndTime(contest))
				.stream()
				.filter(arr -> arr != null && arr[0] != null)
				.map(arr -> new UserRanking((BigInteger) arr[2],
						this.userRepository.findOne(((BigInteger) arr[0])
								.longValue()), (BigDecimal) arr[1]))
				.collect(Collectors.toList());
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public boolean hasContestTestingFinished(Contest contest) {
		return this.contestRepository.hasContestTestingFinished(
				contest.getId(), contest.getStartTime(),
				this.getContestEndTime(contest));
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public List<UserRanking> getContestResultsPage(Contest contest, int page) {
		return this.userRepository
				.getContestResultsPage(contest.getId(), contest.getStartTime(),
						this.getContestEndTime(contest),
						ContestService.CONTEST_RESULTS_PAGE_LENGTH,
						(page - 1) * ContestService.CONTEST_RESULTS_PAGE_LENGTH)
				.stream()
				.map(arr -> new UserRanking((BigInteger) arr[2],
						this.userRepository.findOne(((BigInteger) arr[0])
								.longValue()), (BigDecimal) arr[1]))
				.collect(Collectors.toList());
	}

	@PreAuthorize(IS_USER)
	public List<Contest> getContestsOrderedByTime(final Integer pageNumber,
			final int pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize,
				Direction.DESC, "startTime");
		return this.contestRepository.findAll(request).getContent();
	}

	@PreAuthorize(IS_USER)
	public Contest getContestThatIntersects(final Date startDate,
			final Date endDate) {
		return this.contestRepository.findIntersects(startDate, endDate);
	}

	@PreAuthorize(IS_ADMIN)
	public List<User> getPariticipantsPage(Contest contest, Integer pageNumber) {
		return this.userRepository.findPartiticpants(contest,
				new PageRequest(pageNumber - 1,
						ContestService.CONTEST_PARTICIPANTS_PAGE_LENGTH));
	}

	public Contest getRunningContest() {
		return this.getContestThatIntersects(Date.from(Instant.now()),
				Date.from(Instant.now()));
	}

	@PreAuthorize(IS_USER)
	public long getTotalTimeExtensionTimeForUser(final Contest contest,
			final User user) {
		return this.contestTimeExtensionRepository
				.findByUserAndContest(user, contest).stream()
				.map((timeExtension) -> timeExtension.getDuration())
				.reduce((x, y) -> (x + y)).orElse(0l);
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public BigDecimal getUserTaskScoreInContest(final Contest contest,
			final User user, final Task task) {
		return Lists.first(
				this.taskRepository.getTaskScoreForContest(contest
						.getStartTime(), Date.from(this
						.getContestEndIncludingAllTimeExtensions(contest)),
						task.getId(), user.getId())).orElse(BigDecimal.ZERO);
	}

	public boolean hasContestStarted(final Contest contest) {
		if (contest == null) {
			return false;
		}
		return !contest.getStartTime().toInstant().isAfter(Instant.now());
	}

	@PreAuthorize(IS_USER)
	public boolean isContestInProgressForUser(final Contest contest,
			final User user) {
		if (Instant.now().isBefore(contest.getStartTime().toInstant())) {
			return false;
		}
		return !this.getContestEndTimeForUser(contest, user).toInstant()
				.isAfter(Instant.now());
	}

	@PreAuthorize(IS_USER)
	public boolean isContestOverIncludingAllTimeExtensions(final Contest contest) {
		return this.getContestEndIncludingAllTimeExtensions(contest).isBefore(
				Instant.now());
	}

	@PreAuthorize(IS_USER)
	public boolean isUserParticipatingIn(final User user, final Contest contest) {
		return this.contestParticipationRepository.findOneByContestAndUser(
				contest, user) != null;
	}

	@PreAuthorize(IS_ADMIN)
	public Contest saveContest(Contest contest) {
		return contest = this.contestRepository.save(contest);
	}

}
