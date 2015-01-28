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

import java.security.Principal;

import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.ContestParticipationRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oolsec")
public class SecurityService {

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

	public boolean isOnLockdown() {
		return this.propertyService.get("isOnLockdown", false).<Boolean> as()
				.orElse(false);
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

	public boolean noContest() {
		return this.contestService.getRunningContest() == null;
	}

	public boolean noLockdown() {
		return !this.isOnLockdown();
	}
}
