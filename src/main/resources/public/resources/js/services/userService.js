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

import Util from "oolutil";
import {
	property as _property,
	omit as _omit
} from "lodash";
import {
	services
} from "app";

class UserService {
	/* @ngInject*/
	constructor($http, $q, Upload, $rootScope) {
		this.$http = $http;
		this.$q = $q;
		this.$rootScope = $rootScope;
		this.Upload = Upload;
	}
	getCurrentUser() {
		return this.$http.get("/api/user/personalInfo", {})
			.then(_property("data"));
	}
	changePassword(passwordObj, userId) {
		const passwordPatchUrl = !userId ? "/api/user/changePassword" :
			`/api/admin/user/${userId}/changePassword`;
		return this.$http({
			method: "PATCH",
			url: passwordPatchUrl,
			data: _omit(Util.emptyToNull(passwordObj), "passwordConfirmation")
		})
			.then(_property("data"));
	}
	countPendingUsers() {
		return this.$http.get("/api/admin/pendingUsersCount")
			.then(_property("data"));
	}
	getPendingUsersPage(page) {
		return this.$http.get("/api/admin/pendingUsers", {
			params: {
				page
			}
		})
			.then(_property("data"));
	}
	countUsers() {
		return this.$http.get("/api/admin/usersCount")
			.then(_property("data"));
	}
	getUsersPage(page) {
		return this.$http.get("/api/admin/users", {
			params: {
				page
			}
		})
			.then(_property("data"));
	}

	getUserById(id) {
		return this.$http.get("/api/user", {
			params: {
				id
			}
		})
			.then(_property("data"));
	}

	approveUsers(users) {
		return this.$http.post("/api/admin/users/approve", users)
			.then(_property("data"));
	}
	deleteUsers(users) {
		return this.$http.post("/api/admin/users/deleteUsers", users)
			.then(_property("data"));
	}
	countArchiveUsers() {
		return this.$http.get("/api/archive/rankCount")
			.then(_property("data"));
	}
	getArchiveRankPage(page) {
		return this.$http.get("/api/archive/rank", {
			params: {
				page
			}
		})
			.then(_property("data"));
	}
	addUserToGroup(groupId, username) {
		const deferred = this.$q.defer();

		const formData = new FormData();
		formData.append("username", username);

		this.Upload.http({
			method: "POST",
			url: `/api/group/${groupId}/addUser`,
			headers: {
				"Content-Type": undefined,
				"X-Auth-Token": this.$rootScope.authToken
			},
			data: formData,
			transformRequest: angular.identity
		})
			.success((data) => {
				deferred.resolve(data);
			});

		return deferred.promise;
	}
	removeUserFromGroup(groupId, userId) {
		return this.$http.delete(`/api/group/${groupId}/removeUser`, {
			params: {
				user: userId
			}
		})
			.then(_property("data"));
	}
}

services.service("UserService", UserService);
