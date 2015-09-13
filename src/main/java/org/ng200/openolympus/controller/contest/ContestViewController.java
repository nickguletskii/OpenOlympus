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
package org.ng200.openolympus.controller.contest;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.annotations.ContestPermissionRequired;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.predicates.UserContestViewSecurityPredicate;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")

@SecurityOr({
              @SecurityAnd({
                             @SecurityLeaf(
                                     value = SecurityClearanceType.APPROVED_USER,
                                     predicates = UserContestViewSecurityPredicate.class)
		})
})
@ContestPermissionRequired(ContestPermissionType.participate)
public class ContestViewController {

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
		private Date startTime;
		private Date endTime;
		private Date endTimeIncludingTimeExtensions;

		public TimingDTO(Date startTime, Date endTime,
		        Date endTimeIncludingTimeExtensions) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.endTimeIncludingTimeExtensions = endTimeIncludingTimeExtensions;
		}

		public Date getEndTime() {
			return this.endTime;
		}

		public Date getEndTimeIncludingTimeExtensions() {
			return this.endTimeIncludingTimeExtensions;
		}

		public Date getStartTime() {
			return this.startTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

		public void setEndTimeIncludingTimeExtensions(
		        Date endTimeIncludingTimeExtensions) {
			this.endTimeIncludingTimeExtensions = endTimeIncludingTimeExtensions;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

	}

	@Autowired
	private ContestService contestService;
	@Autowired
	private UserService userService;

	@Cacheable(value = "contests",
	        key = "#contest.id",
	        unless = "#result == null")
	@RequestMapping(value = "/api/contest/{contest}",
	        method = RequestMethod.GET)

	public ContestDTO showContestHub(
	        @PathVariable(value = "contest") final Contest contest,
	        final Principal principal) {
		final User user = this.userService
		        .getUserByUsername(principal.getName());
		final List<Task> tasks = this.contestService.getContestTasks(contest);
		return new ContestDTO(contest.getName(), new TimingDTO(
		        contest.getStartTime(), Date.from(contest.getStartTime()
		                .toInstant().plus(contest.getDuration())),
		        this.contestService.getContestEndTimeForUser(contest, user)),
		        tasks);
	}

}
