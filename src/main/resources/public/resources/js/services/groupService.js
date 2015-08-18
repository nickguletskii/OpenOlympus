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

var _ = require("lodash");
var angular = require("angular");
angular.module("ool.services").factory("GroupService", /*@ngInject*/ function($http) {
	return {
		getGroupsPage: function(page) {
			return $http.get("/api/groups", {
				params: {
					page: page
				}
			}).then(_.property("data"));
		},
		countGroups: function() {
			return $http.get("/api/groupsCount").then(_.property("data"));
		},
		getMembersPage: function(contestId, page) {
			return $http.get("/api/group/" + contestId, {
				params: {
					page: page
				}
			}).then(_.property("data"));
		},
		countMembers: function(contestId) {
			return $http.get("/api/group/" + contestId + "/memberCount").then(_.property("data"));
		},
		getGroupEditData: function(groupId) {
			return $http
				.get("/api/group/" + groupId + "/edit")
				.then(_.property("data"));
		}
	};
});
