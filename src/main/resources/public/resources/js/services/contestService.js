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

import {
	property as _property
} from "lodash";
import { services } from "app";

class ContestService {
	/* @ngInject*/
	constructor($http) {
		this.$http = $http;
	}
	getContestsPage(page) {
		return this.$http.get("/api/contests", {
			params: {
				page
			}
		}).then(_property("data"));
	}
	countContests() {
		return this.$http.get("/api/contestsCount")
			.then(_property("data"));
	}
	getContestInfo(contestId) {
		return this.$http.get(`/api/contest/${contestId}`)
			.then(_property("data"));
	}
	countContestParticipants(contestId) {
		return this.$http.get(`api/contest/${contestId}/participantsCount`)
			.then(_property("data"));
	}
	getContestParticipantsPage(contestId, page) {
		return this.$http.get(`api/contest/${contestId}/participants`, {
			params: {
				page
			}
		}).then(_property("data"));
	}
	getContestResultsPage(contestId, page) {
		return this.$http.get(`api/contest/${contestId}/results`, {
			params: {
				page
			}
		}).then(_property("data"));
	}
	getContestEditData(contestId) {
		return this.$http.get(`/api/contest/${contestId}/edit`)
			.then(_property("data"));
	}
	removeParticipant(contestId, id) {
		return this.$http.delete(`/api/contest/${contestId}/removeUser`, {
			params: {
				user: id
			}
		});
	}
	removeTask(contestId, id) {
		return this.$http.delete(`/api/contest/${contestId}/removeTask`, {
			params: {
				task: id
			}
		});
	}
}

services.service("ContestService", ContestService);
