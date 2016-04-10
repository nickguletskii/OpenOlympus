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

import angular from "angular";
import {
	property as _property
} from "lodash";
import { services } from "app";
class GroupService {
	/* @ngInject*/
	constructor($http) {
		this.$http = $http;
	}
	getGroupsPage(page) {
		return this.$http.get("/api/groups", {
			params: {
				page
			}
		}).then(_property("data"));
	}
	countGroups() {
		return this.$http.get("/api/groupsCount")
			.then(_property("data"));
	}
	getMembersPage(contestId, page) {
		return this.$http.get(`/api/group/${contestId}`, {
			params: {
				page
			}
		}).then(_property("data"));
	}
	countMembers(contestId) {
		return this.$http.get(`/api/group/${contestId}/memberCount`)
			.then(_property("data"));
	}
	getGroupEditData(groupId) {
		return this.$http
			.get(`/api/group/${groupId}/edit`)
			.then(_property("data"));
	}
}
services
	.service("GroupService", GroupService);
