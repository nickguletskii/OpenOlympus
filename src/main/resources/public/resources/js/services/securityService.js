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

import { services } from "app";
import moment from "moment";
import {
	property as _property,
	includes as _includes,
	some as _some
} from "lodash";
import $ from "jquery";


let dataInternal = null;
let lastUpdate = null;
let forceUpdate = false;

function paramTransform(data) {
	return $.param(data);
}

class SecurityService {
	constructor($rootScope, $http, $timeout, $state, $q, $cacheFactory,
		PromiseUtils) {
		this.$rootScope = $rootScope;
		this.$http = $http;
		this.$timeout = $timeout;
		this.$state = $state;
		this.$q = $q;
		this.$cacheFactory = $cacheFactory;
		this.securityStatusCache = this.$cacheFactory("securityStatusCache");
		this.PromiseUtils = PromiseUtils;
	}

	permissionFunc(url, permission) {
		const deferred = this.$q.defer();
		this.update()
			.then(
				() => {
					if (this.isSuperUserImmediate) {
						deferred.resolve(true);
						return;
					}
					if (!this.hasPrincipal) {
						deferred.resolve(false);
						return;
					}
					this.$http.get(url(this.user.id), {
						params: {
							permission
						}
					})
						.then(_property("data"))
						.then((val) => deferred.resolve(val));
				}
			);
		return deferred.promise;
	}


	get data() {
		return dataInternal;
	}

	get hasPrincipal() {
		return !!this.data && !!this.data.currentPrincipal;
	}

	get isLoggedIn() {
		return this.hasPrincipal;
	}

	get username() {
		if (!this.hasPrincipal) {
			return null;
		}
		return this.user.username;
	}

	get user() {
		if (!this.hasPrincipal) {
			return null;
		}
		return this.data.currentPrincipal;
	}

	get isSuperUser() {
		return this.update()
			.then(() => this.isSuperUserImmediate);
	}

	get isSuperUserImmediate() {
		if (!this.hasPrincipal) {
			return false;
		}
		return this.user.superuser;
	}

	hasContestPermission(contestId, permission) {
		return this.permissionFunc((principalId) =>
			`/api/contest/${contestId}/hasContestPermission/${principalId}`,
			permission);
	}

	hasTaskPermission(taskId, permission) {
		return this.permissionFunc((principalId) =>
			`/api/task/${taskId}/hasTaskPermission/${principalId}`, permission);
	}

	hasGroupPermission(groupId, permission) {
		return this.permissionFunc((principalId) =>
			`/api/group/${groupId}/hasGroupPermission/${principalId}`, permission);
	}

	isUserInCurrentContestOrNoContest() {
		return this.update()
			.then((data) => {
				// TODO: implement
				return true;
			});
	}

	hasPermission(permission) {
		return this.update()
			.then(() => this.hasPermissionImmediate(permission));
	}

	hasPermissionImmediate(permission) {
		if (permission === "approved") {
			return this.hasPrincipal && this.user.approved;
		}

		if (!this.hasPrincipal) {
			return false;
		}

		if (this.isSuperUserImmediate) {
			return true;
		}
		return _includes(this.user.permissions, permission);
	}

	hasAnyPermissionImmediate(...args) {
		return _some(args, (permission) => this.hasPermissionImmediate(
			permission));
	}


	noCurrentContest() {
		return this.update()
			.then(() => this.noCurrentContestImmediate);
	}

	noCurrentContestImmediate() {
		return !this.data || !this.data.currentContest;
	}

	isContestOver(contestId) {
		// TODO: implement
		return this.$q.when(false);
	}


	update() {
		if (forceUpdate ||
			(lastUpdate && lastUpdate.isBefore(moment()
				.subtract(5, "seconds")))) {
			this.securityStatusCache.removeAll();
		}

		forceUpdate = false;

		if (this.securityStatusCache.get("data")) {
			return this.$q.when(dataInternal);
		}

		if (!forceUpdate && this.updatePromise) {
			return this.updatePromise;
		}

		this.updatePromise = this.$http.get("/api/security/status")
			.then((r) => {
				dataInternal = r.data;
				this.securityStatusCache.put("data", dataInternal);
				lastUpdate = moment();
				this.updatePromise = null;
				this.$rootScope.$broadcast("securityInfoChanged");
				return dataInternal;
			});

		return this.updatePromise;
	}

	login(username, password, recaptchaResponse) {
		return this.$http({
			headers: {
				"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
			},
			method: "POST",
			url: "/api/login",
			transformRequest: paramTransform,
			data: {
				username,
				password,
				recaptchaResponse
			}
		})
			.then((x) => {
				forceUpdate = true;
				this.update();
				return x;
			});
	}

	logout() {
		this.$http({
			headers: {
				"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
			},
			method: "POST",
			url: "/api/logout",
			transformRequest: paramTransform,
			data: {}
		})
			.then(() => {
				forceUpdate = true;

				this.$state.go("home", {}, {
					reload: true,
					location: true
				});
				this.update();
			});
	}

}
SecurityService.$inject = ["$rootScope", "$http", "$timeout", "$state", "$q",
	"$cacheFactory", "PromiseUtils"
];

services
	.service("SecurityService", SecurityService)
	.run(/* @ngInject */ (SecurityService, $timeout) => {
		function poller() {
			SecurityService.update()
				.then(() => {
					$timeout(poller, 60000);
				});
		}
		poller();
	});
