/*
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

import each from "lodash/fp/each";
import map from "lodash/fp/map";
import flow from "lodash/fp/flow";
import filter from "lodash/fp/filter";
import find from "lodash/fp/find";
import assign from "lodash/fp/assign";

const controller = /* @ngInject*/ ($scope, $stateParams, UserService,
	users, userCount, $translate, $q) => {
	const page = $stateParams.page;

	$scope.page = $stateParams.page;

	$scope.users = users;
	$scope.userCount = userCount;

	const getUserInfoFromServer = () => $q.all({
		users: UserService.getPendingUsersPage(page),
		count: UserService.countPendingUsers()
	});

	const indicateLoading = (action) => {
		$scope.loading = true;
		action()
			.finally(() => {
				$scope.loading = false;
			});
	};

	const addUserApprovalResponseInfo = (userApprovals) =>
		({
			users,
			count
		}) =>
		({
			users: map((user) => {
				const oldUser = find({
					id: user.id
				})($scope.users);
				if (oldUser) {
					user = oldUser;
				}
				const userResponse = find({
					id: user.id
				})(userApprovals);
				if (!userResponse) {
					return user;
				}
				return assign({
					"checked": true,
					"statusMessage": userResponse.statusMessage,
					"resultType": userResponse.resultType
				})(user);
			})(users),
			count
		});

	const commitUsers = ({
		users,
		count
	}) => {
		$scope.users = users;
		$scope.userCount = count;
	};

	const updateUsers = () =>
		getUserInfoFromServer()
		.then(commitUsers);

	const handleApprovalResponse = (userApprovals) =>
		getUserInfoFromServer()
		.then(addUserApprovalResponseInfo)
		.then(commitUsers);

	const approveUsers = (usersToBeApproved) =>
		indicateLoading(
			() =>
			UserService.approveUsers(usersToBeApproved)
			.then(handleApprovalResponse)
		);

	const deleteUsers = (usersToBeDeleted) =>
		indicateLoading(
			() =>
			UserService.deleteUsers(usersToBeDeleted)
			.then(updateUsers)
		);


	const tooltipKeys = {
		"approvalEmailAlreadySent": "admin.unapprovedUsers.approvalEmailAlreadySent",
		"resendApprovalEmail": "admin.unapprovedUsers.resendApprovalEmail",
		"sendApprovalEmail": "admin.unapprovedUsers.sendApprovalEmail"
	};

	const tooltips = {};
	each((translationKey) => $translate(translationKey)
		.then(
			(translation) => {
				tooltips[translationKey] = translation;
			},

			() => {
				tooltips[translationKey] = `Missing localisation: ${translationKey}`;
			})
	)(tooltipKeys);


	$scope.getTooltipForUser = (user) => {
		if (user.statusMessage) {
			return user.statusMessage;
		}
		if (user.approvalEmailSent) {
			if (user.checked) {
				return tooltips[tooltipKeys["resendApprovalEmail"]];
			}
			return tooltips[tooltipKeys["approvalEmailAlreadySent"]];
		}

		if (user.checked) {
			return tooltips[tooltipKeys["sendApprovalEmail"]];
		}
		return "";
	};


	$scope.approveUsers = () =>
		approveUsers(flow(
			filter("checked"),
			map("id")
		)($scope.users));

	$scope.retryApprovingFailedUsers = () =>
		approveUsers(flow(
			flow(
				filter("error"),
				map("id")
			)($scope.users)
		)($scope.users));

	$scope.deleteUsersWithErrors = () => {
		deleteUsers(flow(
			filter((user) => user.checked && !!user.error),
			map((user) => user.id)
		)($scope.users));
	};

	$scope.getButtonClassForUser = (user) => {
		if (user.resultType === "FAILURE") {
			return "btn-danger";
		}
		if (user.resultType === "SUCCESS") {
			return "btn-success";
		}
		if (user.approvalEmailSent) {
			return "btn-warning";
		}
		if (user.checked) {
			return "btn-info";
		}
		return "btn-default";
	};

	$scope.getButtonIconClassForUser = (user) => {
		if (user.approvalEmailSent) {
			if (user.checked) {
				return "fa-refresh";
			}
			return "fa-hourglass-o";
		}
		if (user.checked) {
			return "fa-check-square";
		}
		return "fa-square-o";
	};
};

export default {
	"name": "pendingUsersList",
	"url": "/admin/pendingUsers?page",
	"templateUrl": "/partials/admin/pendingUsers.html",
	"customWidth": "wide",
	controller,
	"params": {
		"page": "1"
	},
	"resolve": {
		"users": /* @ngInject*/ (UserService, $stateParams) =>
			UserService.getPendingUsersPage($stateParams.page),
		"userCount": /* @ngInject*/ (UserService) => UserService.countPendingUsers()
	},
	"data": {
		canAccess: /* @ngInject*/ (SecurityService) =>
			SecurityService.hasPermission("approve_user_registrations")
	}
};
