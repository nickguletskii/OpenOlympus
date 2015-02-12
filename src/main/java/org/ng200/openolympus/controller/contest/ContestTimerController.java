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
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.SecurityService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/contestTimeRemaining/{contest}")
public class ContestTimerController {

	@Autowired
	private ContestService contestService;

	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getTimeRemaining(
			@PathVariable("contest") final Contest contest,
			final Principal principal) {
		Assertions.resourceExists(contest);

		Instant end = contest.getStartTime().toInstant();
		final boolean isInProcess = contest.getStartTime().before(
				java.util.Date.from(Instant.now()));
		if (isInProcess) {
			if (this.securityService.isSuperuser(principal)) {
				end = this.contestService
						.getContestEndIncludingAllTimeExtensions(contest);
			} else if (principal != null) {
				final User user = this.userService.getUserByUsername(principal
						.getName());
				if (user != null) {
					end = end.plusMillis(contest.getDuration()).plusMillis(
							this.contestService
									.getTotalTimeExtensionTimeForUser(contest,
											user));
				}
			}
		}
		if (principal != null) {
			this.contestService.getTotalTimeExtensionTimeForUser(contest,
					this.userService.getUserByUsername(principal.getName()));
		}
		final Instant cur = Instant.now();
		if (end.isBefore(cur)) {
			return new HashMap<String, String>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 5419629757758058933L;

				{
					this.put("timer", "00:00:00");
					this.put("status", "ended");
				}
			};
		}
		final Duration duration = Duration.between(cur, end);
		final DecimalFormat format = new DecimalFormat("00");
		final long hours = duration.toHours();
		final long minutes = duration.minusHours(hours).toMinutes();
		final long seconds = duration.minusHours(hours).minusMinutes(minutes)
				.getSeconds();
		return new HashMap<String, String>() {
			/**
			 *
			 */
			private static final long serialVersionUID = -4698243010184691932L;

			{
				this.put("timer",
						format.format(hours) + ":" + format.format(minutes)
								+ ":" + format.format(seconds));

				this.put("status", isInProcess ? "inProgress" : "notStartedYet");
			}
		};
	}
}
