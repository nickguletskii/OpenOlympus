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

let _ = require("lodash");

const controller = /*@ngInject*/ function($scope, $rootScope,
	$stateParams, $q, $http, Upload, $translate, members, membersCount, GroupService, UserService) {

	$scope.page = $stateParams.page;

	$scope.groupId = $stateParams.groupId;

	$scope.members = members;
	$scope.membersCount = membersCount;

	$scope.user = {};

	function updateUsers() {
		GroupService
			.getMembersPage($stateParams.groupId, $stateParams.page)
			.then(
				(newMembers) =>
				$scope.members = newMembers);
	}


	$scope.userOptionProvider = function(filterText) {
		var deferred = $q.defer();
		$http.get("/api/userCompletion", {
			params: {
				term: filterText
			}
		}).then((response) => {
			deferred.resolve(response.data);
		}, () => {
			deferred.reject();
			throw new Error("Principal completion failed.");
		});
		return deferred.promise;
	};


	class ButtonStatus {
		get isSuccess() {
			return this.lastAdded && (
				(this.lastAdded === $scope.user.user) ||
				(this.lastAdded === $scope.user.user.username)
			);
		}
		get buttonMessage() {
			if (this.isSuccess) {
				return "groups.groupView.addUserButton.success";
			}
			return "groups.groupView.addUserButton";
		}
	}
	$scope.buttonStatus = new ButtonStatus();

	$scope.addUser = function(form) {
		let deferred = $q.defer();
		let username = form.user.username || form.user;
		UserService.addUserToGroup($stateParams.groupId, username).then(function(data) {
			let errorMapping = {
				"USER_ALREADY_IN_GROUP": "groups.groupView.addUser.userAlreadyInGroup",
				"NO_SUCH_USER": "groups.groupView.addUser.noSuchUser"
			};

			if (_.has(errorMapping, data)) {
				$translate([errorMapping[data]]).then(map => {
					deferred.reject({
						user: map[errorMapping[data]]
					});
				});
			} else {
				$scope.buttonStatus.data = {
					username: username
				};
				$scope.buttonStatus.lastAdded = username;
				deferred.resolve();

				updateUsers();
			}
		});
		return deferred.promise;
	};

	$scope.removeUser = function(user) {
		UserService.removeUserFromGroup($stateParams.groupId, user.id)
			.then(updateUsers);
	};
};
module.exports = {
	"name": "groupView",
	"url": "/groups/{groupId:[0-9]+}/",
	"templateUrl": "/partials/groups/group.html",
	"controller": controller,
	"params": {
		"page": "1"
	},
	"resolve": {
		"members": function(GroupService, $stateParams) {
			return GroupService.getMembersPage($stateParams.groupId, $stateParams.page);
		},
		"membersCount": function(GroupService, $stateParams) {
			return GroupService.countMembers($stateParams.groupId);
		}
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService, $refStateParams) {
			return PromiseUtils.and(
				SecurityService.hasPermission("list_groups"),
				SecurityService.hasGroupPermission($refStateParams.groupId, "view_members")
			);
		}
	}
};
