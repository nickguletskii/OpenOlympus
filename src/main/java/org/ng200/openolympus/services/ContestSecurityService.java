/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import java.security.Principal;
import java.util.Optional;

import org.ng200.openolympus.exceptions.ForbiddenException;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.ContestParticipationRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestSecurityService {

	@Autowired
	private ContestService contestService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContestParticipationRepository contestParticipationRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PropertyService propertyService;

	private void assertLockdownDisabledOrSuperuser(final User user) {
		if (!this.propertyService.get("isOnLockdown", false).<Boolean> as()
				.orElse(false)) {
			return;
		}
		if (user == null // User not authenticated
				|| !this.isSuperuser(user) // Or not a superuser
		) {
			throw new ForbiddenException("security.contest.lockdown");
		}
	}

	public void assertSuperuserOrNoRestrictionMode(final Principal principal) {
		if (principal == null) {
			this.assertUserHasAccessThroughLockdownAndRunningContest((User) null);
		} else {
			this.assertUserHasAccessThroughLockdownAndRunningContest(this.userRepository
					.findByUsername(principal.getName()));
		}

	}

	public void assertSuperuserOrTaskAllowed(final Principal principal,
			final Task task) {
		this.assertSuperuserOrTaskAllowed(
				Optional.ofNullable(principal)
						.map(p -> this.userRepository.findByUsername(p
								.getName())).orElse(null), task);
	}

	private void assertSuperuserOrTaskAllowed(final User user, final Task task) {
		if (this.contestService.getRunningContest() == null) {
			this.assertLockdownDisabledOrSuperuser(user);
			return;
		}
		if (this.contestService.getRunningContest().getTasks().contains(task)) {
			return;
		}
		this.assertUserHasAccessThroughLockdownAndRunningContest(user);
	}

	public void assertUserHasAccessThroughLockdownAndRunningContest(
			final User user) {
		if (this.contestService.getRunningContest() == null) { // No running
			// contest
			this.assertLockdownDisabledOrSuperuser(user); // So check if
			// lockdown is
			// active
			return;
		}

		if (user == null// User not authenticated
				|| !this.isSuperuser(user)// Or not superuser
		) {
			throw new ForbiddenException(
					"security.contest.cantAccessDuringContest");
		}
	}

	public void assertUserIsInContestOrSuperuser(final Principal principal) {
		if (this.contestService.getRunningContest() == null) {
			if (principal == null) {
				this.assertLockdownDisabledOrSuperuser(null);
			} else {
				this.assertLockdownDisabledOrSuperuser(this.userRepository
						.findByUsername(principal.getName()));
			}
			return;
		}
		if (principal == null) {
			throw new ForbiddenException("security.contest.notInContest");
		}
		this.assertUserIsInContestOrSuperuser(this.userRepository
				.findByUsername(principal.getName()));
	}

	public void assertUserIsInContestOrSuperuser(final User user) {
		if (this.contestService.getRunningContest() == null) {// No running
			// contest
			this.assertLockdownDisabledOrSuperuser(user); // So check if
			// lockdown is
			// active
			return;
		}
		if (this.isSuperuser(user)) { // Superusers are guaranteed to have
			// access
			return;
		}
		if (this.contestParticipationRepository.findOneByContestAndUser(
				this.contestService.getRunningContest(), user) == null) { // User
			// is
			// not
			// participating
			// in
			// the
			// current
			// contest
			throw new ForbiddenException("security.contest.notInContest");
		}
	}

	public boolean isSuperuser(final Principal principal) {
		if (principal == null) {
			return false;
		}
		return this.isSuperuser(this.userRepository.findByUsername(principal
				.getName()));
	}

	private boolean isSuperuser(final User user) {
		if (user == null) {
			return false;
		}
		return user.hasRole(this.roleService.getRoleByName(Role.SUPERUSER));
	}
}
