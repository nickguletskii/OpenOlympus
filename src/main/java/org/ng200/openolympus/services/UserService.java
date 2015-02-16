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
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.ContestParticipationRepository;
import org.ng200.openolympus.repositories.ContestTimeExtensionRepository;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.ng200.openolympus.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerdictRepository verdictRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private SolutionRepository solutionRepository;

	@Autowired
	private ContestParticipationRepository contestParticipationRepository;
	@Autowired
	private ContestTimeExtensionRepository contestTimeExtensionRepository;

	public UserService() {

	}

	@PreAuthorize(IS_ADMIN)
	public long countUnapprovedUsers() {
		return this.userRepository.countUnapproved();
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public long countUsers() {
		return this.userRepository.count();
	}

	@PreAuthorize(IS_ADMIN)
	public void deleteUser(final User user) {
		// TODO: use SQL
		this.solutionRepository.findByUser(user).forEach(
				solution -> {
					this.verdictRepository.delete(this.verdictRepository
							.findBySolution(solution));
					this.solutionRepository.delete(solution);
				});
		this.contestParticipationRepository
				.delete(this.contestParticipationRepository.findByUser(user));
		this.contestTimeExtensionRepository
				.delete(this.contestTimeExtensionRepository.findByUser(user));
		this.userRepository.delete(user);
	}

	@PreAuthorize(IS_ADMIN)
	public void deleteUsers(List<User> users) {
		// TODO: use SQL
		users.forEach(this::deleteUser);
	}

	@PreAuthorize(IS_ADMIN)
	public List<User> findAFewUsersWithNameContaining(final String name) {
		return this.userRepository.findFirst30Like("%" + name + "%");
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public List<UserRanking> getArchiveRankPage(final long page,
			final long pageSize) {
		return this.userRepository
				.getRankPage(pageSize, (page - 1) * pageSize)
				.stream()
				.map(arr -> new UserRanking((BigInteger) arr[2],
						this.userRepository.findOne(((BigInteger) arr[0])
								.longValue()), (BigDecimal) arr[1]))
				.collect(Collectors.toList());
	}

	@PreAuthorize(IS_ADMIN)
	public List<User> getUnapprovedUsers(int pageNumber, int pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize,
				Sort.Direction.DESC, "firstNameMain");
		return this.userRepository.findUnapproved(request);
	}

	@PreAuthorize(IS_ADMIN)
	public User getUserById(final Long id) {
		return this.userRepository.findOne(id);
	}

	public User getUserByUsername(final String username) {
		return this.userRepository.findByUsername(username);
	}

	@PreAuthorize(IS_ADMIN)
	public List<User> getUsersAlphabetically(final Integer pageNumber,
			final int pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize,
				Sort.Direction.DESC, "firstNameMain");
		return this.userRepository.findAll(request).getContent();
	}

	@PreAuthorize(IS_ADMIN)
	public boolean isUserApproved(User user) {
		return user.getRoles().contains(
				this.roleService.getRoleByName(Role.USER));
	}

	public User saveUser(User user) {
		return user = this.userRepository.save(user);
	}
}
