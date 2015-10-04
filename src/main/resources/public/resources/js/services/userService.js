/*
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
"use strict";

var Util = require("oolutil");
var _ = require("lodash");
var angular = require("angular");
angular.module("ool.services").factory("UserService", /*@ngInject*/ function($http, $q, Upload, $rootScope) {
	return {
		getCurrentUser: function() {
			return $http.get("api/user/personalInfo", {}).then(_.property("data"));
		},
		changePassword: function(passwordObj, userId) {
			var passwordPatchUrl = !userId ? "/api/user/changePassword" : "/api/admin/user/" + userId + "/changePassword";
			return $http({
				method: "PATCH",
				url: passwordPatchUrl,
				data: _.omit(Util.emptyToNull(passwordObj), "passwordConfirmation")
			}).then(_.property("data"));
		},
		countPendingUsers: function() {
			return $http.get("api/admin/pendingUsersCount").then(_.property("data"));
		},
		getPendingUsersPage: function(page) {
			return $http.get("api/admin/pendingUsers", {
				params: {
					page: page
				}
			}).then(_.property("data"));
		},
		countUsers: function() {
			return $http.get("api/admin/usersCount").then(_.property("data"));
		},
		getUsersPage: function(page) {
			return $http.get("api/admin/users", {
				params: {
					page: page
				}
			}).then(_.property("data"));
		},
		approveUsers: function(users) {
			return $http.post("/api/admin/users/approve", users).then(_.property("data"));
		},
		deleteUsers: function(users) {
			return $http.post("/api/admin/users/deleteUsers", users).then(_.property("data"));
		},
		countArchiveUsers: function() {
			return $http.get("api/archive/rankCount").then(_.property("data"));
		},
		getArchiveRankPage: function(page) {
			return $http.get("api/archive/rank", {
				params: {
					page: page
				}
			}).then(_.property("data"));
		},
		addUserToGroup: function(groupId, username) {
			let deferred = $q.defer();

			let formData = new FormData();
			formData.append("username", username);

			Upload.http({
				method: "POST",
				url: "/api/group/" + groupId + "/addUser",
				headers: {
					"X-Auth-Token": $rootScope.authToken,
					"Content-Type": undefined
				},
				data: formData,
				transformRequest: angular.identity
			}).success(function(data) {
				deferred.resolve(data);
			});

			return deferred.promise;
		},
		removeUserFromGroup: function(groupId, userId) {
			return $http.delete("/api/group/" + groupId + "/removeUser", {
				params: {
					user: userId
				}
			}).then(_.property("data"));
		}
	};
});
