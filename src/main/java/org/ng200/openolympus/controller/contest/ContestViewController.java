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
package org.ng200.openolympus.controller.contest;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.UserKnowsAboutContestSecurityPredicate;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.services.contest.ContestTasksService;
import org.ng200.openolympus.services.contest.ContestTimingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")

@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER)
		})
})
public class ContestViewController {

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = UserKnowsAboutContestSecurityPredicate.class)
	public static class ContestDTO {
		private String name;
		private TimingDTO timings;
		private List<Task> tasks;

		public ContestDTO(String name, TimingDTO timings, List<Task> tasks) {
			super();
			this.name = name;
			this.timings = timings;
			this.tasks = tasks;
		}

		public String getName() {
			return this.name;
		}

		public List<Task> getTasks() {
			return this.tasks;
		}

		public TimingDTO getTimings() {
			return this.timings;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setTasks(List<Task> tasks) {
			this.tasks = tasks;
		}

		public void setTimings(TimingDTO timings) {
			this.timings = timings;
		}
	}

	public static class TimingDTO {
		private OffsetDateTime startTime;
		private OffsetDateTime endTime;
		private OffsetDateTime endTimeIncludingTimeExtensions;

		public TimingDTO(OffsetDateTime startTime, OffsetDateTime endTime,
				OffsetDateTime endTimeIncludingTimeExtensions) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.endTimeIncludingTimeExtensions = endTimeIncludingTimeExtensions;
		}

		public OffsetDateTime getEndTime() {
			return this.endTime;
		}

		public OffsetDateTime getEndTimeIncludingTimeExtensions() {
			return this.endTimeIncludingTimeExtensions;
		}

		public OffsetDateTime getStartTime() {
			return this.startTime;
		}

		public void setEndTime(OffsetDateTime endTime) {
			this.endTime = endTime;
		}

		public void setEndTimeIncludingTimeExtensions(
				OffsetDateTime endTimeIncludingTimeExtensions) {
			this.endTimeIncludingTimeExtensions = endTimeIncludingTimeExtensions;
		}

		public void setStartTime(OffsetDateTime startTime) {
			this.startTime = startTime;
		}

	}

	private static final Logger logger = LoggerFactory
			.getLogger(ContestViewController.class);

	@Autowired
	private ContestTasksService contestTasksService;
	@Autowired
	private ContestTimingService contestTimingService;
	@Autowired
	private UserService userService;
	@Autowired
	private AclService aclService;

	private boolean canViewTasks(User user, Contest contest) {
		if (user.getSuperuser()) {
			return true;
		}
		if (this.aclService.hasContestPermission(contest, user,
				ContestPermissionType.list_tasks,
				ContestPermissionType.manage_acl)) {
			return true;
		}
		if (this.contestTimingService.isContestInProgressForUser(contest, user)
				&& this.aclService.hasContestPermission(contest, user,
						ContestPermissionType.participate)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/api/contest/{contest}", method = RequestMethod.GET)
	public ContestDTO showContestHub(
			@PathVariable(value = "contest") final Contest contest,
			final Principal principal) {
		final User user = this.userService
				.getUserByUsername(principal.getName());

		final List<Task> tasks = this.canViewTasks(user, contest)
				? this.contestTasksService.getContestTasks(contest)
				: null;
		return new ContestDTO(contest.getName(),
				new TimingDTO(this.contestTimingService
						.getContestStartIncludingAllTimeExtensions(contest),
						this.contestTimingService
								.getContestEndIncludingAllTimeExtensions(
										contest),
						this.contestTimingService.getContestStartTimeForUser(
								contest,
								user)),
				tasks);
	}

}
