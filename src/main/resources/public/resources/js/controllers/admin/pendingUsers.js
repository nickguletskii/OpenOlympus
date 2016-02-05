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
"use strict";

var _ = require("lodash");

const controller = /*@ngInject*/ function($scope, $stateParams, UserService, users, userCount, $translate) {
	var page = $stateParams.page;

	$scope.page = $stateParams.page;

	$scope.users = users;
	$scope.userCount = userCount;

	let tooltipKeys = {
		"approvalEmailAlreadySent": "admin.unapprovedUsers.approvalEmailAlreadySent",
		"resendApprovalEmail": "admin.unapprovedUsers.resendApprovalEmail",
		"sendApprovalEmail": "admin.unapprovedUsers.sendApprovalEmail"
	};

	let tooltips = {};
	_.forEach(tooltipKeys, (translationKey) =>
		$translate(translationKey).then(
			(translation) => {
				tooltips[translationKey] = translation;
			},

			() => {
				tooltips[translationKey] = "Missing localisation: " + translationKey;
			})
	);


	$scope.getTooltipForUser = function(user) {
		if (user.statusMessage) {
			return user.statusMessage;
		}
		if (user.approvalEmailSent) {
			if (user.checked) {
				return tooltips[tooltipKeys["resendApprovalEmail"]];
			} else {
				return tooltips[tooltipKeys["approvalEmailAlreadySent"]];
			}
		}
		if (user.checked) {
			return tooltips[tooltipKeys["sendApprovalEmail"]];
		} else {
			return "";
		}
	}

	function updateUsers() {
		UserService.getPendingUsersPage(page).then(function(users) {
			$scope.users = user;
		});
		UserService.countPendingUsers().then(function(count) {
			$scope.userCount = count;
		});
	}

	function handleApprovalResponse(userApprovals) {
		UserService.getPendingUsersPage(page).then(function(users) {
			$scope.users = _.map(users, function(user) {
				var oldUser = _.find($scope.users, function(oldUser) {
					return oldUser.id === user.id;
				});
				if (oldUser) {
					user = oldUser;
				}
				var userResponse = _.find(userApprovals, function(userResponse) {
					return userResponse.id === user.id;
				});
				if (!userResponse) {
					return user;
				}
				return _.assign(user, {
					"checked": true,
					"statusMessage": userResponse.statusMessage,
					"resultType": userResponse.resultType
				});
			});
			$scope.loading = false;
		});
		UserService.countPendingUsers().then(function(count) {
			$scope.userCount = count;
		});
	}
	$scope.approveUsers = function() {
		$scope.loading = true;
		UserService.approveUsers(_($scope.users).filter(function(user) {
			return user.checked;
		}).map(function(user) {
			return user.id;
		}).value()).then(handleApprovalResponse);
	};

	$scope.retryApprovingFailedUsers = function() {
		$scope.loading = true;
		UserService.approveUsers(_($scope.users).filter(function(user) {
			return !!user.error;
		}).map(function(user) {
			return user.id;
		}).value()).then(handleApprovalResponse);
	};

	$scope.deleteUsersWithErrors = function() {
		UserService.deleteUsers(_($scope.users).filter(function(user) {
			return user.checked && !!user.error;
		}).map(function(user) {
			return user.id;
		}).value()).then(function() {
			updateUsers();
		});
	};

	$scope.getButtonClassForUser = function(user) {
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

	$scope.getButtonIconClassForUser = function(user) {
		if (user.approvalEmailSent) {
			if (user.checked) {
				return "fa-refresh";
			} else {
				return "fa-hourglass-o";
			}
		}
		if (user.checked) {
			return "fa-check-square";
		} else {
			return "fa-square-o";
		}
	};


};

module.exports = {
	"name": "pendingUsersList",
	"url": "/admin/pendingUsers?page",
	"templateUrl": "/partials/admin/pendingUsers.html",
	"customWidth": "wide",
	"controller": controller,
	"params": {
		"page": "1"
	},
	"resolve": {
		"users": function(UserService, $stateParams) {
			return UserService.getPendingUsersPage($stateParams.page);
		},
		"userCount": function(UserService) {
			return UserService.countPendingUsers();
		}
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService) {
			return SecurityService.hasPermission("approve_user_registrations");
		}
	}
};
